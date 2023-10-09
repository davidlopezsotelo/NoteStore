package com.davidlopez.notestore10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        //mensaje()
        setup()
    }

    // creamos un cuadro de dialogo-----------------------------------------------------------------
    private fun mensaje() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Hola, Usuario")
        builder.setMessage("Te has identificado correctamente.")
        builder.setPositiveButton("aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // funcion setup()------------------------------------------------------------------------------
    private fun setup(){
        title="Menu Principal"

        val BotonSalir=findViewById<Button>(R.id.button_salir3)

        BotonSalir.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }


}