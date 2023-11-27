package com.davidlopez.notestore10

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.davidlopez.notestore10.Login.LoginActivity
import com.davidlopez.notestore10.UI.MenuPrincipalActivity
import com.davidlopez.notestore10.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Declaramos Firebase Analytics
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Inicializamos Firebase Analytics
        analytics = Firebase.analytics
        auth=Firebase.auth

        //inicializamos la app, llamando al login activity
        binding.buttonEntrar.setOnClickListener {
            //verificar usuario
            VerificarUsuario()
       }

        binding.buttonSalirMain.setOnClickListener{
            //creamos el alert Dialog
            val dialog= AlertDialog.Builder(this)
            //creamos el mensaje que aparecera
            dialog.setMessage("Quieres salir de la aplicacion ???")
                //si el dialog es cancelable
                .setCancelable(false)
                //accion y texto del boton positivo
                .setPositiveButton("SI") { _, _ -> salir() }
                //texto y accion del boton negativo
                .setNegativeButton("NO", DialogInterface.OnClickListener{ dialog, id -> dialog.cancel()})
            //creamos la caja de dialogo
            val alert=dialog.create()
            //ponemos el titulo a la caja de dialogo
            alert.setTitle("SALIR!!!!!")
            //mostrar
            alert.show()
        }

    }

    //si el usuario tiene sesion abierta, te manda al menu principal

    fun VerificarUsuario(){
        //FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
            finish()
        } else {
            // No user is signed in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun salir() {
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}