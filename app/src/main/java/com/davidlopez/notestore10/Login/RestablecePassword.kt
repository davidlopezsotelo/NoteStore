package com.davidlopez.notestore10.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.davidlopez.notestore10.MainActivity

import com.davidlopez.notestore10.databinding.ActivityRestablecePasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RestablecePassword : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mBinding: ActivityRestablecePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityRestablecePasswordBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



        val txtmail=mBinding.etEmailCambio

        mBinding.buttonSave.setOnClickListener {

            sendPasswordReset(txtmail.text.toString())
        }
        firebaseAuth=Firebase.auth

        mBinding.buttonSalir.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun sendPasswordReset(email:String?){
        firebaseAuth.sendPasswordResetEmail(email!!)
            .addOnCompleteListener(){ task ->
                if (task.isSuccessful)
                {
                    Toast.makeText(baseContext,"Correo de cambio de contraseña,enviado.",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,LoginActivity::class.java))
                }else
                {
                    Toast.makeText(baseContext,"Error, no se pudo completar el proceso.",Toast.LENGTH_SHORT).show()
                }
        }

    }
}