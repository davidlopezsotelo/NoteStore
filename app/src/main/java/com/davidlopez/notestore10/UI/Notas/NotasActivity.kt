package com.davidlopez.notestore10.UI.Notas

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.recyclerview.widget.GridLayoutManager
import com.davidlopez.notestore10.App.ContactosApp
import com.davidlopez.notestore10.DataBase.Entities.NotasEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.MenuPrincipalActivity
import com.davidlopez.notestore10.databinding.ActivityNotasBinding
import java.util.concurrent.LinkedBlockingQueue


//class main...
class NotasActivity : AppCompatActivity(),OnClickListenerNotas,NotasAux {

    private lateinit var mBinding: ActivityNotasBinding
    private lateinit var mAdapter: NotasAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityNotasBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


//NOTAS--------------------------------------------------------------------------------------------
        mBinding.fabN.setOnClickListener {

            //TODO abrir fragment y crear la nota desde el fragmet al guardar o salir

            launchNotasfragment()
            /*
            //creamos la nota desde el editText
            val nota= NotasEntity(name = mBinding.tvNotas.text.toString())

            //INSERTAR EN BASE DE DATOS-----------------------------------------------------------------------

            //creamos un segundo hilo para la insercion de datos en la base de datos
            Thread {

                //hacemos que la nota creada se inserte en la base de datos
                ContactosApp.db.notasDao().addNota(nota)
            }.start()
            mAdapter.add(nota)// añadimos la nota con el adaptador
            */
        }

        setupRecyclerView()

    }


//RECYCLERVIEW------------------------------------------------------------------------------------------

    private fun setupRecyclerView() {
        mAdapter= NotasAdapter(mutableListOf(),this)
        mGridLayout= GridLayoutManager(this,2)//numero de elementos por columna
        getNotas()
        mBinding.reciclerView.apply {
            setHasFixedSize(true)
            layoutManager=mGridLayout
            adapter=mAdapter
        }
    }


    //funcion para llamar a la base de datos y consultar todas las notas:

    private fun getNotas(){

        //configuramos una cola "queue"para aceptar los tipos de datos
        val queue= LinkedBlockingQueue<MutableList<NotasEntity>>()

        //abrimos un segundo hilo para que la app no pete.
        Thread {
            val notas = ContactosApp.db.notasDao().getAllNotas()// consultamos a la base de datos

            // añadimos las consultas a la cola
            queue.add(notas)
        }.start()

        //mostramos los resultados
        mAdapter.setNotas(queue.take())

    }
    /*
 * OnClickListener
 * */
    override fun onClick(notasEntity: NotasEntity) {

        //TODO  editar nota al pulsar en ella, se abre el fragment en modo edit
        //launchNotasfragment()

    }


    //FRAGMENT----------------------------------------------------------------------------------------------
    private fun launchNotasfragment() {
        val fragment=NotaFragment()

        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()

        fragmentTransaction.addToBackStack(null)//cambiar para guardar al ir atras??
        fragmentTransaction.add(R.id.containerNotas,fragment)
        fragmentTransaction.commit()

        //retroceder al pulsar el boton atras
        fragmentTransaction.addToBackStack(null)

        // ocultamos el boton despues de pulsarlo
        mBinding.fabN.hide()
        hideFabN()//este metodo lo oculta y lo vuelve a mostrar al pulsar atras

    }





    //actualizar registro
    override fun onFavoriteNota(notasEntity: NotasEntity) {
        notasEntity.isFaborite=!notasEntity.isFaborite

        val queue=LinkedBlockingQueue<NotasEntity>()

        //insertar actualizacion en base de datos
        Thread{
            ContactosApp.db.notasDao().updateNota(notasEntity)
            queue.add(notasEntity)
        }.start()
        mAdapter.update(queue.take())
    }
    //borrar registro
    override fun onDeleteNota(notasDB: NotasEntity) {

        val queue=LinkedBlockingQueue<NotasEntity>()

        Thread{
            ContactosApp.db.notasDao().deleteAll(notasDB)
            queue.add(notasDB)
        }.start()
        mAdapter.delete(queue.take())
    }

    override fun hideFabN(isVisible: Boolean) {
        if (isVisible)mBinding.fabN.show() else mBinding.fabN.hide()
    }


    override fun addNota(notasEntity: NotasEntity) {
        mAdapter.add(notasEntity)
    }

    override fun updateNota(notasEntity: NotasEntity) {

    }


    //configuramos boton atras---------------------------------------
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MenuPrincipalActivity::class.java))
    }


}













