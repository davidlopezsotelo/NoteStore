package com.davidlopez.notestore10.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davidlopez.notestore10.R

class NotasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)
    }

    //configuramos boton atras---------------------------------------
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MenuPrincipalActivity::class.java))
    }
}