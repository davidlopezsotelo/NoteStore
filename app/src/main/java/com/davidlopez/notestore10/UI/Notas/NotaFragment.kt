package com.davidlopez.notestore10.UI.Notas

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.davidlopez.notestore10.App.NoteStoreApp
import com.davidlopez.notestore10.DataBase.Entities.NotasEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.databinding.FragmentNotaBinding
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.LinkedBlockingQueue


/*
                   * para que aparezca el action bar
                   * ir a la carpeta res/values/themes/themes.xml
                   * y borrar donde pone NoActionBar, en la linea 3
                   * luego aplicar tema en manifest
                   * */

@Suppress("DEPRECATION")
class NotaFragment : Fragment() {


    private lateinit var mBinding: FragmentNotaBinding
    private var mActivity:NotasActivity?=null

    private lateinit var mAdapter: NotasAdapter

    private var mIsEditMode: Boolean = false
    private var mNotasEntity:NotasEntity?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View
    {
        mBinding=FragmentNotaBinding.inflate(inflater,container,false)
        return mBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recibimos los datos esde el bundle de notas activity, para poder actualizar la nota
        val id=arguments?.getLong(getString(R.string.arg_id_nota),0)

        // activamos el modo de edicion
        if (id != null && id != 0L){
            mIsEditMode=true
            getNota(id)
        }else{
            // case de creacion de nota
            mIsEditMode=false
            mNotasEntity=NotasEntity(name = "", texto = "")//inicializa el objeto con los parametros en vacio.
        }
        setupActionBar()
    }

    private fun setupActionBar() {
        // inicializar y configurar barra de acciones:
        mActivity=activity as? NotasActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title=if (mIsEditMode) getString(R.string.edit_title_editar_nota)//creamos el recurso
        else getString(R.string.edit_title_add_nota)
        setHasOptionsMenu(true)

    }

    private fun getNota(id:Long) {
        val queue=LinkedBlockingQueue<NotasEntity?>()
        Thread{
            mNotasEntity=NoteStoreApp.db.notasDao().getNotaById(id)
            queue.add(mNotasEntity)
        }.start()
        queue.take().let {
            //rellenamos les editText
            setUiNota(it)
        }
    }

    private fun setUiNota(notasEntity: NotasEntity?) {

        with(mBinding){
          etTituloNota.setText(notasEntity?.name)
            etTextoNota.setText(notasEntity?.texto)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_nota,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home -> {
                hideKeyboard()
                mActivity?.onBackPressedDispatcher?.onBackPressed()
                true
            }

            R.id.save_nota ->{
                // guardar nota

                if (mNotasEntity !=null) {
                    with(mNotasEntity!!) {
                        name = mBinding.etTituloNota.text.toString().trim()
                        texto = mBinding.etTextoNota.text.toString().trim()
                    }
                    val queue = LinkedBlockingQueue<NotasEntity>()
                    Thread {
                        hideKeyboard()
                        if (mIsEditMode) {
                            NoteStoreApp.db.notasDao().updateNota(mNotasEntity!!)
                            Snackbar.make(mBinding.root, R.string.edit_message_update_sucess,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else mNotasEntity!!.id =
                            NoteStoreApp.db.notasDao().addNota(mNotasEntity!!)
                        queue.add(mNotasEntity)
                    }.start()

                    with(queue.take()) {

                        if (mIsEditMode) {
                            mActivity?.updateNota(this)
                            Snackbar.make(mBinding.root, R.string.edit_message_update_note,
                                Snackbar.LENGTH_SHORT
                            ).show()

                        } else {
                            mActivity?.addNota(this)
                            Toast.makeText(mActivity, R.string.edit_message_save_nota,
                                Toast.LENGTH_LONG
                            ).show()

                     requireActivity().
                     onBackPressedDispatcher.
                     onBackPressed()//retroceder al pulsar el boton guardar
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun hideKeyboard(){
        val imm=mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE)as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken,0)
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }
    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title=getString(R.string.app_name)
        mActivity?.hideFabN(true)
        setHasOptionsMenu(false)
        super.onDestroy()
    }
}