package com.davidlopez.notestore10.Log

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.davidlopez.notestore10.R
import com.google.firebase.auth.FirebaseAuth

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        setup()
    }

    // funcion setup()----------------------------------------------------------------------------------
    private fun setup(){

        val textEmail = findViewById<EditText>(R.id.email)
        val textContraseña = findViewById<EditText>(R.id.contraseña)
        val botonAceptar=findViewById<Button>(R.id.buttonAceptar)
        val botonLogin=findViewById<Button>(R.id.buttonAtrasLog)
        val botonSalir=findViewById<Button>(R.id.buttonSalir2)

        //Funciones de boton REGISTRARSE
        botonAceptar.setOnClickListener {
            if (textEmail.text.isNotEmpty() && textContraseña.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        textEmail.text.toString(),
                        textContraseña.text.toString()
                    )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
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
            onBackPressed()
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
    }

    //creamos una funcion que mostrara un  mensaje de alerta mediante un cuadro de dialogo---------
    private fun showAlert(){

        val builder= AlertDialog.Builder(this)// creamos un cuadro de dialogo

        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario.")
        builder.setPositiveButton("aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }//ff
}