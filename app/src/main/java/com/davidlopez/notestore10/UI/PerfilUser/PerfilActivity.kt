package com.davidlopez.notestore10.UI.PerfilUser


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import com.davidlopez.notestore10.App.NoteStoreApp
import com.davidlopez.notestore10.DataBase.DAOs.UserDao
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity
import com.davidlopez.notestore10.DataBase.Entities.NotasEntity
import com.davidlopez.notestore10.DataBase.Entities.UserEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.Contactos.EditContactFragment
import com.davidlopez.notestore10.UI.Contactos.ImageController
import com.davidlopez.notestore10.UI.MenuPrincipalActivity
import com.davidlopez.notestore10.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.LinkedBlockingQueue

class PerfilActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityPerfilBinding
    //cargar usuario desde firebase
    private lateinit var auth: FirebaseAuth

    //obtener email de usuario identificado

    var emailUser = ""//insertar email de usuario registrado aqui


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //lamar firebase

        auth = Firebase.auth

        //insertamos el valor del campo email obtenido desde el usuario autenticado enfirebase

        val user=FirebaseAuth.getInstance().currentUser
        emailUser= user?.email.toString()


        //boton editar contacto
        mBinding.btEditarContacto.setOnClickListener {
            startActivity(Intent(this, AddPerfilActivity::class.java))
        }



        cagardatos()


    }

    private fun cagardatos() {



        Thread{


            val usuario=NoteStoreApp.db.userDao().getUserByMail(emailUser)
            val idPhoto= usuario?.id


            //si el campo email esta vacio significa que no hay datos en la base de datos
            if ( usuario?.email.isNullOrBlank() ){
                Log.d("PERFIL DE USUARIO.","No hay datos de usuario")
            }else {
                //de lo contrario carga los datos
                mBinding.tvNombre.text = usuario?.name
                mBinding.apellidos.text = usuario?.surname
                mBinding.tvTelefono.text = usuario?.phone
                mBinding.tvEmail.text = emailUser
                mBinding.tvTrabajo.text = usuario?.work
                mBinding.tvAdress.text = usuario?.adress

                //cargamos la imagen con el id para ponerla en el item:

                //TODO CARGAR LA IMAGEN
                val uriphoto = idPhoto?.let { ImageController.getImageUri(this, it) }
                mBinding.imageView.setImageURI(uriphoto)

                }

            }.start()

        }






        //configuramos boton atras---------------------------------------
        override fun onBackPressed() {
            super.onBackPressed()
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }

}



