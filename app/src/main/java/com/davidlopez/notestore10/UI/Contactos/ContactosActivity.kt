package com.davidlopez.notestore10.UI.Contactos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.davidlopez.notestore10.App.ContactosApp
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.MenuPrincipalActivity
import com.davidlopez.notestore10.databinding.ActivityContactosBinding
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs
import java.util.concurrent.LinkedBlockingQueue

//main-----

 class ContactosActivity : AppCompatActivity() , ContactosAux, OnClickListenerContactos {

    private lateinit var mBinding: ActivityContactosBinding
    private lateinit var mAdapter: ContactosAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityContactosBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


// FRAGMENT---------------------------------------------------------------------------

        mBinding.fab.setOnClickListener { launchEditFragment() }//creamos esta funcion en esta misma actividad.
        setupRecyclerView()
    }

    private fun launchEditFragment(args: Bundle?=null){
        // creamos una instancia al fragment
        val fragment= EditContactFragment()

        if (args!=null) fragment.arguments=args//verificar el id

        val fragmentManager =supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.containerContactos,fragment)
        fragmentTransaction.commit()


        //retroceder al pulsar el boton atras
        fragmentTransaction.addToBackStack(null)

        // ocultamos el boton despues de pulsarlo
         mBinding.fab.hide()
        hideFab()//este metodo lo oculta y lo vuelve a mostrar al pulsar atras
    }

    private fun setupRecyclerView() {
        mAdapter= ContactosAdapter(mutableListOf(),this)
        mGridLayout= GridLayoutManager(this,1)//numero de elementos por columna

        getContactos()
        mBinding.reciclerViewContactos.apply {
            setHasFixedSize(true)//le indicamos que no va a cambiar de tamaño, asi optimizará recursos.
            layoutManager=mGridLayout
            adapter=mAdapter
        }

    }

//funcion para llamar a la base de datos y consultar los contactos
    private fun getContactos(){

    //configuramos una cola "queue"para aceptar los tipos de datos

        val queue=LinkedBlockingQueue<MutableList<ContactosEntity>>()

    //abrimos un segundo hilo para que la app no de fallos.

    Thread{
        val contactos=ContactosApp.db.ContactosDao().getAllContactos()
        //añadimos las consultas a la cola

        queue.add(contactos)
    }.start()

    //mostramos los resultados

    mAdapter.setContactos(queue.take())

    }



//configuramos boton atras---------------------------------------
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MenuPrincipalActivity::class.java))
    }

    override fun hideFab(isVisible: Boolean) {

        if (isVisible)mBinding.fab.show() else mBinding.fab.hide()
    }

    override fun addContact(contactosEntity: ContactosEntity) {
        mAdapter.add(contactosEntity)
    }

     override fun updateContact(contactosEntity: ContactosEntity) {

         mAdapter.update(contactosEntity)


     }

     override fun onClick(contactosEntity: ContactosEntity) {

         //pasar a ContactDetailActivity
         val intent=Intent(this,ContactDetailActivity::class.java)
         intent.putExtra("nombre",contactosEntity.name)
         intent.putExtra("telefono",contactosEntity.phone)
         intent.putExtra("email",contactosEntity.email)
         intent.putExtra("id",contactosEntity.id)
         startActivity(intent)
         finish()

     }

     override fun onDeleteContacto(contactosEntity: ContactosEntity) {
         val queue=LinkedBlockingQueue<ContactosEntity>()
         Thread{

             ContactosApp.db.ContactosDao().deleteAllContacto(contactosEntity)
             queue.add(contactosEntity)
         }.start()
         mAdapter.delete(queue.take())
     }

     override fun onUpdateContacto(contactosId: Long) {

         // pasamos los datos al fragment
         val args=Bundle()
         args.putLong(getString(R.string.arg_id),contactosId)

         //llamamos al metodo que lanza el fragment
         launchEditFragment(args)
     }


 }