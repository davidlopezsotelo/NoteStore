package com.davidlopez.notestore10.UI.Notas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import com.davidlopez.notestore10.NoteStoreApp
import com.davidlopez.notestore10.DataBase.Entities.NotasEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.MenuPrincipalActivity
import com.davidlopez.notestore10.databinding.ActivityNotasBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.concurrent.LinkedBlockingQueue

class NotasActivity : AppCompatActivity(),OnClickListenerNotas,NotasAux {

    private lateinit var mBinding: ActivityNotasBinding
    private lateinit var mAdapter: NotasAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityNotasBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        // pulsar el boton atras-------------------------------
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Aquí va el código que quires ejecutar cuando se presiona el botón de atrás
                val intent=Intent(this@NotasActivity,MenuPrincipalActivity::class.java)
                startActivity(intent)
                finish()
            }
        })


//NOTAS--------------------------------------------------------------------------------------------
        mBinding.fabN.setOnClickListener {//boton flotante

            launchNotasfragment()
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
            val notas = NoteStoreApp.db.notasDao().getAllNotas()// consultamos a la base de datos

            // añadimos las consultas a la cola
            queue.add(notas)
        }.start()

        //mostramos los resultados
        mAdapter.setNotas(queue.take())

    }
    /*
 * OnClickListener
 * */
    override fun onClick(notasId: NotasEntity) {

        // editar nota al pulsar en ella, se abre el fragment en modo edit

        val args=Bundle()
        args.putLong(/* key = */ getString(R.string.arg_id_nota),/* value = */ notasId.id)

        //enviamos los datos al fragment para poder actualizar------
        launchNotasfragment(args)

    }


    //FRAGMENT----------------------------------------------------------------------------------------------
    private fun launchNotasfragment(args: Bundle?=null) {
        val fragment=NotaFragment()
        if (args!=null) fragment.arguments=args

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
            NoteStoreApp.db.notasDao().updateNota(notasEntity)
            queue.add(notasEntity)
        }.start()
        updateNota(queue.take())
    }

    //borrar registro

    override fun onDeleteNota(notasEntity: NotasEntity) {

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_contact_nota)
            .setPositiveButton(R.string.dialog_delete_confirm) { dialogInterface, i ->

                val queue = LinkedBlockingQueue<NotasEntity>()
                Thread {
                    NoteStoreApp.db.notasDao().updateNota(notasEntity)
                    queue.add(notasEntity)
                }.start()
                mAdapter.delete(queue.take())
            }
            .setNegativeButton(R.string.dialog_delete_cancel,null)
            .show()
    }



    override fun hideFabN(isVisible: Boolean) {
        if (isVisible)mBinding.fabN.show() else mBinding.fabN.hide()
    }
    override fun addNota(notasEntity: NotasEntity) {
        mAdapter.add(notasEntity)
    }
    override fun updateNota(notasEntity: NotasEntity) {

        mAdapter.update(notasEntity)
    }
    //configuramos boton atras---------------------------------------
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MenuPrincipalActivity::class.java))
    }
}