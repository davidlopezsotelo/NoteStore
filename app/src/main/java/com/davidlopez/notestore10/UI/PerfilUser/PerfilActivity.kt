package com.davidlopez.notestore10.UI.PerfilUser


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.Contactos.EditContactFragment
import com.davidlopez.notestore10.UI.MenuPrincipalActivity
import com.davidlopez.notestore10.databinding.ActivityPerfilBinding

class PerfilActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityPerfilBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btEditarContacto.setOnClickListener {
            startActivity(Intent(this,AddPerfilActivity::class.java))
        }






    }

    private fun launchPerfilFragment(args: Bundle?=null) {
        // creamos una instancia al fragment
        val fragment= PerfilFragment()

        if (args!=null) fragment.arguments=args//verificar el id

        val fragmentManager =supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container_perfil,fragment)

        fragmentTransaction.commit()

        //retroceder al pulsar el boton atras
        fragmentTransaction.addToBackStack(null)

    }

    //configuramos boton atras---------------------------------------
      override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MenuPrincipalActivity::class.java))
    }




}


