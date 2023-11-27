package com.davidlopez.notestore10.UI.PerfilUser


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import com.davidlopez.notestore10.NoteStoreApp
import com.davidlopez.notestore10.DataBase.Entities.UserEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.MenuPrincipalActivity
import com.davidlopez.notestore10.databinding.ActivityPerfilBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.LinkedBlockingQueue

class PerfilActivity : AppCompatActivity(),PerfilAux,OnClickListenerPerfil{

    private lateinit var mBinding: ActivityPerfilBinding
    private lateinit var mAdapter: PerfilAdapter
    private lateinit var mGridLayout: GridLayoutManager

    //cargar usuario identificado desde firebase
    private lateinit var auth: FirebaseAuth
    //obtener email de usuario identificado
    lateinit var emailUser:String

        //????¿?¿?¿??borrar
    lateinit var usuario:UserEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //lamar firebase---pasar al fragment??
                     /*
                         auth = Firebase.auth
                            //insertamos el valor del campo email obtenido desde el usuario autenticado enfirebase

                             val user=FirebaseAuth.getInstance().currentUser

                            emailUser= user?.email.toString()// enviar al fragment????????????????????????????????
                        */


        // pulsar el boton atras-------------------------------
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Aquí va el código que quires ejecutar cuando se presiona el botón de atrás
                val intent=Intent(this@PerfilActivity,MenuPrincipalActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        //boton editar contacto
        // lanza el fragment-------------------------
        mBinding.btEditarContacto.setOnClickListener {launchEditFragment() }//creamos esta funcion en esta misma actividad.
        setupRecyclerView()
    }

// FRAGMENT---------------------------------------------------------------------------

    private fun launchEditFragment(args:Bundle?=null) {
        // creamos una instancia al fragment
        val fragment= PerfilFragment()

        if (args!=null) fragment.arguments=args//verificar el id

        val fragmentManager =supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.container_perfil,fragment)
        fragmentTransaction.commit()

        //retroceder al pulsar el boton atras
        fragmentTransaction.addToBackStack(null)

    }// End ------------------------------------------

    private fun setupRecyclerView() {
        mAdapter= PerfilAdapter(mutableListOf(),this)
        mGridLayout= GridLayoutManager(this,1)//numero de elementos por columna

        getUsuarios()
        mBinding.reciclerViewPerfil.apply {
            setHasFixedSize(true)//le indicamos que no va a cambiar de tamaño, asi optimizará recursos.
            layoutManager=mGridLayout
            adapter=mAdapter
        }
    }//END-----

//funcion para llamar a la base de datos y consultar los contactos
    private fun getUsuarios() {
    //configuramos una cola "queue"para aceptar los tipos de datos

        val queue=LinkedBlockingQueue<MutableList<UserEntity>>()

    //abrimos un segundo hilo para que la app no de fallos.

    Thread{
        val usuarios= NoteStoreApp.db.userDao().getAllUser() // recibe una lista con todos los contactos, cambiar a recibir solo el contacto por email?????
        //añadimos las consultas a la cola
        queue.add(usuarios)
    }.start()

    //mostramos los resultados
    mAdapter.setUsers(queue.take())
    }

    override fun addUser(userEntity: UserEntity) {
        mAdapter.add(userEntity)
    }

    override fun updateUser(cuserEntity: UserEntity) {
        mAdapter.update(cuserEntity)
    }

    override fun onClick(userEntity: UserEntity) {
        TODO("Not yet implemented")
    }

    override fun onDeleteUser(userEntity: UserEntity) {

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_contact)
            .setPositiveButton(R.string.dialog_delete_confirm) { dialogInterface, i ->

                val queue = LinkedBlockingQueue<UserEntity>()
                Thread {
                    NoteStoreApp.db.userDao().deleteAllUser(userEntity)
                    queue.add(userEntity)
                }.start()
                mAdapter.delete(queue.take())
            }
            .setNegativeButton(R.string.dialog_delete_cancel,null)
            .show()
    }

    override fun onUpdateUser(usuarioId: Long) {
        // pasamos los datos al fragment
        val args=Bundle()
        args.putLong(getString(R.string.arg_id),usuarioId)

        //llamamos al metodo que lanza el fragment
        launchEditFragment(args)
    }

}



