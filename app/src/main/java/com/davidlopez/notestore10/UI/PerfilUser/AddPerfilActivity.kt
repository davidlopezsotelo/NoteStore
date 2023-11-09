package com.davidlopez.notestore10.UI.PerfilUser

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.davidlopez.notestore10.App.NoteStoreApp
import com.davidlopez.notestore10.DataBase.Entities.UserEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.Contactos.ContactosActivity
import com.davidlopez.notestore10.UI.Contactos.ImageController
import com.davidlopez.notestore10.databinding.ActivityAddPerfilBinding
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.LinkedBlockingQueue


class AddPerfilActivity : AppCompatActivity() {

    lateinit var mBinding:ActivityAddPerfilBinding
    private var mActivity: AddPerfilActivity?=null

    private lateinit var mContex: Context
    private  var photoSelectUri: Uri?=null
    private val RC_GALLERY=23

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityAddPerfilBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        mBinding.btnSelectImage.setOnClickListener {
            selectImage()
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
                 val usuario=UserEntity(
                     name = mBinding.etName.text.toString(),
                     surname = mBinding.etSurname.text.toString(),
                     phone = mBinding.etPhone.text.toString(),
                     email = mBinding.etEmail.text.toString(),
                     adress = mBinding.etAdress.text.toString(),
                     work = mBinding.etWork.text.toString())

                 val queue=LinkedBlockingQueue<Long?>()

                 Thread{
                     val id=NoteStoreApp.db.userDao().addUser(usuario)

                     // guardar imagen
                     guardarImagen(id= usuario.id)
                     queue.add(id)


                 }.start()

                 queue.take()?.let{
                     Snackbar.make(mBinding.root,
                         "Usuario añadido",
                         Snackbar.LENGTH_SHORT).show()
                 }

                 true
             }

            else -> super.onOptionsItemSelected(item)
        }

    }


    // insertar imagenes

    private fun guardarImagen(id: Long) {
        photoSelectUri?.let {
            this.mContex.let { it1 -> ImageController.saveImage(it1,id,it) } //???????
        }
    }

    //seleccionar imagen---------------------------------------------
    private fun selectImage() {
        ImageController.selectPhotoFromGallery(this,RC_GALLERY)
    }

}