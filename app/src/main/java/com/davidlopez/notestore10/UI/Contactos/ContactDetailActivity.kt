package com.davidlopez.notestore10.UI.Contactos


import android.Manifest
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
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
            requestPermissionTel()
        }

        // Boton sms
        mBinding.btSMS.setOnClickListener {
            // accion
            requestPermissionSms()

        }
        // Boton mail
        mBinding.btEmail.setOnClickListener {
          sendEmail()
        }
    }



    //funcion que muestra los datos recibidos del ...

    private fun datos(){
        val datos=intent.extras
        val nombre= datos?.getString("nombre")
        val phone= datos?.getString("telefono")
        val email=datos?.getString("email")
        val id=datos?.getLong("id")

        mBinding.tvNombre.text=nombre.toString()
        mBinding.tvTelefono.text=phone.toString()
        mBinding.tvEmail.text=email.toString()

    }

    //  funcion que otorga los permisos

    private fun requestPermissionTel(){

        val phone=mBinding.tvTelefono.text.toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) -> {
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


    private fun requestPermissionSms() {

        val phone=mBinding.tvTelefono.text.toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS) -> {
                    SendSms(phone)
                }
                else -> requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)
            }
        }else{
            SendSms(phone)
        }
    }

    private fun SendSms(phone: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel:$phone")))

    }

    private fun sendEmail() {

        val email=mBinding.tvEmail.text.toString()

        val emailIntent = Intent(Intent.ACTION_SENDTO,
            Uri.fromParts("mailto","$email",null))
            startActivity(Intent.createChooser(emailIntent,"Enviar Correo..."))

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,ContactosActivity::class.java))
    }

}




