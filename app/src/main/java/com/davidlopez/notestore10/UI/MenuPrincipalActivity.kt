package com.davidlopez.notestore10.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AlertDialog
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.databinding.ActivityMenuPrincipalBinding
import com.google.firebase.auth.FirebaseAuth

class MenuPrincipalActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMenuPrincipalBinding

    //private lateinit var adapter: Adapter
    //private lateinit var gridLayout: GridLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    setup()

//CONTACTOS-----------------------------------------------------------------------------------------


    }
    // funcion setup()------------------------------------------------------------------------------
    private fun setup(){
        title="Menu Principal"

        binding.buttonSalir3.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed() }

        /*val BotonSalir=findViewById<Button>(R.id.button_salir3)
        BotonSalir.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }*/
    }


}//end class