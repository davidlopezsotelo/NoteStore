package com.davidlopez.notestore10.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.davidlopez.notestore10.R

class DocumentosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documentos)
    }

    //configuramos boton atras---------------------------------------
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MenuPrincipalActivity::class.java))
    }

}
