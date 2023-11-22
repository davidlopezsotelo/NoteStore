package com.davidlopez.notestore10.UI.PerfilUser


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.davidlopez.notestore10.App.NoteStoreApp
import com.davidlopez.notestore10.DataBase.Entities.UserEntity
import com.davidlopez.notestore10.DataBase.RoomDB
import com.davidlopez.notestore10.UI.MenuPrincipalActivity
import com.davidlopez.notestore10.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

//Main.....

class PerfilActivity : AppCompatActivity(){

    lateinit var mBinding: ActivityPerfilBinding

    //cargar usuario identificado desde firebase
    private lateinit var auth: FirebaseAuth

    //obtener email de usuario identificado
    lateinit var emailUser:String

    //Lista de usuarios
    private var userList:MutableList<UserEntity> = mutableListOf()

    // inicializamos el adaptador
    lateinit var mAdapter: PerfilAdapter

    lateinit var room:RoomDB
    lateinit var usuario:UserEntity

    //Todo seguir en : https://www.youtube.com/watch?v=gztLyRShg2I&t=585s





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

        // instanciamos la base de datos
        room=Room.databaseBuilder(this,RoomDB::class.java,"NoteStoreDataBase").build()



        cargarUsuarios(room)

        cagardatos()


    }

    private fun cargarUsuarios(room: RoomDB) {
        lifecycleScope.launch {
            userList=room.userDao().getAllUser()
            mAdapter=PerfilAdapter(userList,this)

        }

    }

    private fun cagardatos() {



        Thread{


            val usuario=NoteStoreApp.db.userDao().getUserByMail(emailUser)



            //si el campo email esta vacio significa que no hay datos en la base de datos
            if ( usuario?.email.isNullOrBlank() ){
                Log.d("PERFIL DE USUARIO.","No hay datos de usuario")

                // habilitamos el modo de creacion de usuario

            }else {
                //de lo contrario habilitamos el modo de actualizacion de usuario


                // carga los datos
                mBinding.tvNombre.text = usuario?.name
                mBinding.apellidos.text = usuario?.surname
                mBinding.tvTelefono.text = usuario?.phone
                mBinding.tvEmail.text = emailUser
                mBinding.tvTrabajo.text = usuario?.work
                mBinding.tvAdress.text = usuario?.adress

                //cargamos la imagen con el id para ponerla en el item:

                //TODO CARGAR LA IMAGEN
                //val uriphoto = idPhoto?.let { ImageController.getImageUri(this, it) }
               // mBinding.imageView.setImageURI(uriphoto)

                }

            }.start()

        }


        //configuramos boton atras---------------------------------------
        override fun onBackPressed() {
            super.onBackPressed()
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }



}



