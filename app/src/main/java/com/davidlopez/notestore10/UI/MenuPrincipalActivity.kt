package com.davidlopez.notestore10.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davidlopez.notestore10.UI.Contactos.ContactosActivity
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
        title="Menu Principal"

        // boton contactos-----------------------------------------------------
        binding.btnContactos.setOnClickListener {
           startActivity(Intent(this, ContactosActivity::class.java))
            finish()
        }
        // boton notas-----------------------------------------------------
        binding.btnNotas.setOnClickListener {
            startActivity(Intent(this,NotasActivity::class.java))
            finish()
        }
        // boton documentos-----------------------------------------------------
        binding.btnDocs.setOnClickListener {
            startActivity(Intent(this,DocumentosActivity::class.java))
            finish()
        }
        // boton about-----------------------------------------------------
        binding.btnAbout.setOnClickListener {
            startActivity(Intent(this,AboutActivity::class.java))
            finish()
        }


        //botn salir----------------------------------------------------------
        binding.buttonSalir3.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed() }
    }


}//end class