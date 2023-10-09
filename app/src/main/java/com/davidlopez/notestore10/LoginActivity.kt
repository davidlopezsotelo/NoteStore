package com.davidlopez.notestore10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    //Creamos variable Firebase
    private lateinit var auth: FirebaseAuth


    var Email=""
    var Password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        //inicializamos Firebase
        auth = Firebase.auth

        title = "Autenticacion"

        val botonIniciar = findViewById<Button>(R.id.button_Iniciar)
        val botonregistrar = findViewById<Button>(R.id.button_Registrar)
        val botonSalir = findViewById<Button>(R.id.buttonSalir)
        val textEmail = findViewById<EditText>(R.id.editTextEmail)
        val textContraseña = findViewById<EditText>(R.id.editTextContraseña)

        // Funcionalidad del boton REGISTRAR, que nos manda al registroActivity

        botonregistrar.setOnClickListener {
            val i = Intent(this, RegistroActivity::class.java)
            startActivity(i)
        }


        //funciones de boton INICIAR SESION--------------------------------------------

        botonIniciar.setOnClickListener {

            /*
            if (textEmail.text.isNotEmpty() && textContraseña.text.isNotEmpty()) {//si no estan vacios....



                //acccedemos a los metodos de autenticacion de Firebase
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        textEmail.text.toString(), textContraseña.text.toString()
                    )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showHome() //lo que quieres que haga...
                        } else {
                            showAlert()// mensaje de alerta
                        }
                    }
            } else {
                showRellenar()
            }
            */

            //hacer login en firebase
            ValidarDatos()
        }// fin boton INICIAR

        //Boton SALIR
        botonSalir.setOnClickListener() {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
            finish()
        }
    }



    private fun ValidarDatos() {

        val TextoEmailLog = findViewById<EditText>(R.id.editTextEmail)
        val TextoContrasenaLog = findViewById<EditText>(R.id.editTextContraseña)

        Email=TextoEmailLog.text.toString()
        Password=TextoContrasenaLog.text.toString()

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

        //Iniciamos sesion con firebase

        FirebaseAuth.getInstance().signInWithEmailAndPassword(Email,Password)
            .addOnCompleteListener(this, OnCompleteListener { task ->

                if (task.isSuccessful){
                    val user=auth.currentUser
                    Toast.makeText(this,"Te has identificado correctamente.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MenuPrincipalActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this,"Registro fallido.", Toast.LENGTH_SHORT).show()
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



    //creamos una funcion que mostrara un  mensaje de alerta mediante un cuadro de dialogo--------------------------
    private fun showAlert() {

        val builder = AlertDialog.Builder(this)// creamos un cuadro de dialogo

        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario.")
        builder.setPositiveButton("aceptar", null)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    //creamos una funcion que dará acceso a la aplicacion-------------------------------------------------------
    private fun showHome() {

        val i = Intent(this, MenuPrincipalActivity::class.java).apply {

            //showRegistro()
        }
        startActivity(i)
    }
}//END Class commit