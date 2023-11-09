package com.davidlopez.notestore10.UI.PerfilUser


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.davidlopez.notestore10.DataBase.Entities.UserEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.Contactos.EditContactFragment
import com.davidlopez.notestore10.UI.MenuPrincipalActivity
import com.davidlopez.notestore10.databinding.ActivityPerfilBinding

class PerfilActivity : AppCompatActivity(){

    lateinit var mBinding: ActivityPerfilBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        mBinding.btEditarContacto.setOnClickListener {
            startActivity(Intent(this,AddPerfilActivity::class.java))
        }

        cagardatos()


    }

    private fun cagardatos() {

    }


    //configuramos boton atras---------------------------------------
      override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MenuPrincipalActivity::class.java))
    }


}


