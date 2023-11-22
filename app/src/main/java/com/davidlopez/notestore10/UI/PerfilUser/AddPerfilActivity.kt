package com.davidlopez.notestore10.UI.PerfilUser

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.davidlopez.notestore10.App.NoteStoreApp
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity
import com.davidlopez.notestore10.DataBase.Entities.UserEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.Contactos.ContactosActivity
import com.davidlopez.notestore10.UI.Contactos.ImageController
import com.davidlopez.notestore10.databinding.ActivityAddPerfilBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.LinkedBlockingQueue


class AddPerfilActivity : AppCompatActivity() {

    lateinit var mBinding:ActivityAddPerfilBinding
   // private var mActivity: PerfilActivity?=null

    private var mUserEntity: UserEntity?=null
    lateinit var  mContex: Context
    private  var photoSelectUri: Uri?=null
    private val RC_GALLERY=23

    // actualizar usuario--------------------------------------------------------------------------
    private var isEditMode:Boolean=false


    private lateinit var auth: FirebaseAuth
    var emailUser = ""//insertar email de usuario registrado aqui

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityAddPerfilBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        //lamar firebase
        auth = Firebase.auth

        //insertamos el valor del campo email obtenido desde el usuario autenticado enfirebase

        val user=FirebaseAuth.getInstance().currentUser
        emailUser= user?.email.toString()


        mBinding.btnSelectImage.setOnClickListener {
            selectImage()
        }


        // actualizar el contacto-------------------------------



        //cargar usuario de la base de datos


        if(mUserEntity?.email !=null) {
            isEditMode=true
            getUser(emailUser)

        }
        // editar usuario
        else {

            isEditMode=false
            mUserEntity= UserEntity(
                name = "",
                surname = "", email = emailUser,
                phone = "",
                adress = "",
                work = "")
            //mBinding.etEmail.text=emailUser.editable()
        }




    }

    private fun getUser(email:String){
        val queue =LinkedBlockingQueue<UserEntity?>()

        Thread{

            mUserEntity=NoteStoreApp.db.userDao().getUserByMail(email)
            queue.add(mUserEntity)


        }.start()
        queue.take()?.let { cargarUser(it) }

    }

    private fun cargarUser(usuarioEntity: UserEntity) {

       mContex=this
     //   val id=usuarioEntity.id
                with(mBinding){
                    etName.setText(usuarioEntity.name)
                    etSurname.setText(usuarioEntity.surname)
                    etPhone.setText(usuarioEntity.phone)
                    etWork.setText(usuarioEntity.work)
                    etAdress.setText(usuarioEntity.adress)
                    etEmail.setText(emailUser)

                //cargamos la imagen con el id para ponerla en el item:
                    //imageViewPhoto.setImageURI(ImageController.getImageUri(mContex,id))
                }
    }

    //cargar la imagen al refrescar la vista
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            if (requestCode == RC_GALLERY) {
                photoSelectUri = data?.data
                mBinding.imageViewPhoto.setImageURI(photoSelectUri)

            }
        }
    }


    //Creamos el menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //inflamos la vista del menu:
        menuInflater.inflate(R.menu.menu_add_user,menu)
        //colocar la flecha de ir hacia atras
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Datos de usuario"

        return super.onCreateOptionsMenu(menu)
    }


    // opciones del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed() //ir hacia atras con el boton??revisar
                true
            }

            // guardar en base de datos
             R.id.action_save ->{
                 onBackPressedDispatcher.onBackPressed()

                 if(mUserEntity !=null){
                     with(mUserEntity!!){
                         name = mBinding.etName.text.toString()
                         surname = mBinding.etSurname.text.toString()
                         phone = mBinding.etPhone.text.toString()
                         email = emailUser
                         adress = mBinding.etAdress.text.toString()
                         work = mBinding.etWork.text.toString()
                     }

                     val queue=LinkedBlockingQueue<UserEntity>()
                     Thread{

                         if (isEditMode){
                             NoteStoreApp.db.userDao().updateUser(mUserEntity!!)
                         }else {
                             //mUserEntity!!.id=NoteStoreApp.db.userDao().addUser(mUserEntity!!)
                         }

                        // guardarImagen(id= mUserEntity!!.id)
                         queue.add(mUserEntity)

                     }.start()

                 }

                 //Todo modo editar usuario existente
                /* mUserEntity=UserEntity(
                     name = mBinding.etName.text.toString(),
                     surname = mBinding.etSurname.text.toString(),
                     phone = mBinding.etPhone.text.toString(),
                     email = emailUser,
                     adress = mBinding.etAdress.text.toString(),
                     work = mBinding.etWork.text.toString())*/


                     Snackbar.make(mBinding.root,
                         "Usuario añadido",
                         Snackbar.LENGTH_SHORT).show()
                 true
             }


            else -> super.onOptionsItemSelected(item)
        }
    }

    // insertar imagenes

    //TODO arreglar foto y despues continuar con carga de datos
    private fun guardarImagen(id: Long) {
        mContex=this
        photoSelectUri?.let {
            this.mContex.let { it1 -> ImageController.saveImageUser(it1,id,it) } //???????

            Snackbar.make(mBinding.root,"el id de la foto es:$id",Snackbar.LENGTH_LONG).show()

        }
    }

    //seleccionar imagen---------------------------------------------
    private fun selectImage() {
        ImageController.selectPhotoFromGallery(this,RC_GALLERY)
    }

    private fun String.editable(): Editable = Editable.Factory.getInstance().newEditable(this)

}