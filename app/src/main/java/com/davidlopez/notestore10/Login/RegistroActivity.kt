package com.davidlopez.notestore10.Login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.davidlopez.notestore10.UI.MenuPrincipalActivity
import com.davidlopez.notestore10.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
    }

    // funcion setup()----------------------------------------------------------------------------------
    private fun setup(){

        val textEmail = binding.email
        val textContrasena = binding.contraseA
        val botonAceptar=binding.buttonAceptar
        val botonLogin=binding.buttonAtrasLog
        val botonSalir=binding.buttonSalir2

        //Funciones de boton REGISTRARSE
        botonAceptar.setOnClickListener {
            if (textEmail.text!!.isNotEmpty() && textContrasena.text!!.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        textEmail.text.toString(),
                        textContrasena.text.toString()
                    )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            //ocultar teclado ??
                            showRegistro()

                        } else {
                            showAlert()// mensaje de alerta
                        }

                    }
            } else {
                showRellenar()
            }
        }//--------------------------------------------------------

        // funciones del boton LOGIN.........

        botonLogin.setOnClickListener(){
            val logIntent= Intent(this, LoginActivity::class.java)
            startActivity(logIntent)
            finish()
        }

        //Boton Salir

        botonSalir.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            onBackPressedDispatcher.onBackPressed()
            finish()

        }

    }// Fin setup()---------------------------------------------------------------------------------


    //creamos una funcion que mostrara un  mensaje de alerta mediante un cuadro de dialogo----------
    private  fun showRellenar() {
        val builder= AlertDialog.Builder(this)// creamos un cuadro de dialogo

        builder.setTitle("ERROR DE AUTENTICACION!!")
        builder.setMessage("Deves de rellenar todos los campòs correctamente.")
        builder.setPositiveButton("aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }

    //creamos una funcion que mostrara un  mensaje de alerta mediante un cuadro de dialogo----------
    private fun showRegistro() {

        val builder= AlertDialog.Builder(this)// creamos un cuadro de dialogo
        builder.setTitle("Registro")
        builder.setMessage("Te has registrado correctamente.")
        builder.setPositiveButton("aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()

        //Toast.makeText(this,"Te has registrado correctamente",Toast.LENGTH_LONG)
        Thread.sleep(3000L)
        startActivity(Intent(this,MenuPrincipalActivity::class.java))
    }

    //creamos una funcion que mostrara un  mensaje de alerta mediante un cuadro de dialogo---------
    private fun showAlert(){

        val builder= AlertDialog.Builder(this)// creamos un cuadro de dialogo

        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error REGISTRANDO al usuario.")
        builder.setPositiveButton("aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }//ff
}