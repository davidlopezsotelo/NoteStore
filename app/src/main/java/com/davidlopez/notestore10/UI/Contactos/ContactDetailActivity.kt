package com.davidlopez.notestore10.UI.Contactos


import android.Manifest
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

import com.davidlopez.notestore10.databinding.ActivityContactDetailBinding

// CONVERTIR EN UN FRAGMENT PARA COMUNICARSE CON EL OTRO FRAGMENT

class ContactDetailActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityContactDetailBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->

        val phone=mBinding.tvTelefono.text.toString()

        if (isGranted){
        call(phone) }else Toast.makeText(this,"necesitas habilitar el permiso",Toast.LENGTH_SHORT).show()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        datos()

        // boton llamar
        mBinding.btLlamar.setOnClickListener {
            requestPermission()
        }

        // Boton editar
        mBinding.btEditar.setOnClickListener {
            // accion

        }
    }


    //funcion que muestra los datos recibidos del ...

    private fun datos(){
        val datos=intent.extras
        val nombre= datos?.getString("nombre")
        val phone= datos?.getInt("telefono")
        val email=datos?.getString("email")
        val id=datos?.getLong("id")

        mBinding.tvNombre.text=nombre.toString()
        mBinding.tvTelefono.text=phone.toString()
        mBinding.tvEmail.text=email.toString()
        mBinding.tvid.text=id.toString()//eliminar el texfield !!!!!
    }

    //  funcion que otorga los permisos

    private fun requestPermission(){

        val phone=mBinding.tvTelefono.text.toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){

            when{

                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED -> {
                        call(phone)
                    }
                else -> requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }


        }else{
            call(phone)
        }
    }

    private fun call(phone:String) {
        startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone")))

    }





}




