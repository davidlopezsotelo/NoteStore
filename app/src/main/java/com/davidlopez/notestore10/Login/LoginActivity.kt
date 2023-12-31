package com.davidlopez.notestore10.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.davidlopez.notestore10.DataBase.Entities.UserEntity
import com.davidlopez.notestore10.UI.MenuPrincipalActivity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.Contactos.ContactDetailActivity
import com.davidlopez.notestore10.UI.PerfilUser.PerfilActivity
import com.davidlopez.notestore10.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    //Creamos variable Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    var Email=""
    var Password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //inicializamos Firebase
        auth = Firebase.auth
        title = "Autenticacion"


        // Funcionalidad del boton REGISTRAR, que nos manda al registroActivity

        binding.buttonRegistrar.setOnClickListener {
            val i = Intent(this, RegistroActivity::class.java)
            startActivity(i)
        }

        //funciones de boton INICIAR SESION--------------------------------------------

        binding.buttonIniciar.setOnClickListener {
            //hacer login en firebase
            ValidarDatos()
        }// fin boton INICIAR

        //Boton restablecer

        binding.btnRestaurar.setOnClickListener {
            startActivity(Intent(this,RestablecePassword::class.java))
            finish()
        }

        //Boton SALIR
        binding.buttonSalir.setOnClickListener() {
            FirebaseAuth.getInstance().signOut()
            onBackPressedDispatcher
            finish()
        }
    }

    private fun ValidarDatos() {


        Email=binding.editTextEmail.text.toString()
        Password=binding.editTextContraseA.text.toString()


        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){// verifica que se introduce un correo
            Toast.makeText(this,"Correo no valido.", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(Password)){//verifica que se introduce un texto y no esta vacio
            Toast.makeText(this,"Ingrese contraseña.", Toast.LENGTH_SHORT).show()
        }
        else{//si las dos anteriores condiciones se cumplen ejecuta la funcion
            LoginUsuario()
        }

    }

    //Funcion que verifica si el usuario tiene cuenta.
    private fun LoginUsuario() {

        //comprovar conexion internet.

        //Iniciamos sesion con firebase

        FirebaseAuth.getInstance().signInWithEmailAndPassword(Email,Password)
            .addOnCompleteListener(this, OnCompleteListener { task ->

                if (task.isSuccessful){
                    auth.currentUser
                    Toast.makeText(this,"Te has identificado correctamente.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MenuPrincipalActivity::class.java))
                    finish()
                }else{
                    showRellenar()
                }
            })
    }
    //creamos una funcion que mostrara un  mensaje de alerta mediante un cuadro de dialogo--------------------
    private fun showRellenar() {

        val builder = AlertDialog.Builder(this)// creamos un cuadro de dialogo

        builder.setTitle("ERROR DE AUTENTICACION!!")
        builder.setMessage("Deves de rellenar todos los campos correctamente.")
        builder.setPositiveButton("aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
}//END Class