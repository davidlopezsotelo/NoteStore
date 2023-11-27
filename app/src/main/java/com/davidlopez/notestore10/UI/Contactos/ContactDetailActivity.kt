package com.davidlopez.notestore10.UI.Contactos


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.davidlopez.notestore10.ImageController
import com.davidlopez.notestore10.UI.MenuPrincipalActivity
import com.davidlopez.notestore10.databinding.ActivityContactDetailBinding



class ContactDetailActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityContactDetailBinding

    //permisos necesarios----------------------------------------------------------------------------

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


        // pulsar el boton atras-------------------------------
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Aquí va el código que quires ejecutar cuando se presiona el botón de atrás
                val intent=Intent(this@ContactDetailActivity, ContactosActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

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



    //funcion que muestra los datos recibidos del onclick del activity...

    private fun datos(){
        val datos=intent.extras
        val nombre= datos?.getString("nombre")
        val phone= datos?.getString("telefono")
        val email=datos?.getString("email")
        val id=datos?.getLong("id")

 //cargar la imagen

        val imageUri= id?.let { ImageController.getImageUri(this, it) }
        mBinding.imageView.setImageURI(imageUri)

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
            Uri.fromParts("mailto", email,null))
            startActivity(Intent.createChooser(emailIntent,"Enviar Correo"))
    }

}




