package com.davidlopez.notestore10.UI.PerfilUser


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.MenuPrincipalActivity

class PerfilActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)





    }

    //configuramos boton atras---------------------------------------
      override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MenuPrincipalActivity::class.java))
    }




}


