package com.davidlopez.notestore10.UI.Contactos


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.davidlopez.notestore10.databinding.ActivityContactDetailBinding

class ContactDetailActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityContactDetailBinding

   /* private val requestPermissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
        isGranted -> val message = if (isGranted) "Permiso Otorgado" else "Permiso Rechazado"
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        datos()

       // requestPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE)

        mBinding.btLlamar.setOnClickListener {

            //accion de llamada


        }

    }



    //funcion que muestra los datos recibidos del ...

    private fun datos(){
        val datos=intent.extras
        val nombre= datos?.getString("nombre")
        val phone= datos?.getInt("telefono")
        val email=datos?.getString("email")

        mBinding.tvNombre.text=nombre.toString()
        mBinding.tvTelefono.text=phone.toString()
        mBinding.tvEmail.text=email.toString()
    }


}




