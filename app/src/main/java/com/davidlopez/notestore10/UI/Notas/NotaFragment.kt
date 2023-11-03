package com.davidlopez.notestore10.UI.Notas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.davidlopez.notestore10.App.ContactosApp
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

    private lateinit var notas:MutableList<NotasEntity>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View
    {
        mBinding=FragmentNotaBinding.inflate(inflater,container,false)
        return mBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity=activity as? NotasActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title=getString(R.string.nota_fragment_title)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_nota,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home -> {
                mActivity?.onBackPressedDispatcher?.onBackPressed()

                true
            }

            R.id.save_nota ->{
                // guardar nota

                  val nota =NotasEntity(
                      name = mBinding.etTituloNota.text.toString().trim(),
                      texto = mBinding.etTextoNota.text.toString().trim())

                // tambien guardar al ir hacia atrs ??????????????????????

                val queue =LinkedBlockingQueue<Long?>()
                Thread{
                    val id=ContactosApp.db.notasDao().addNota(nota)
                    queue.add(id)
                }.start()

                 queue.take()?.let {
                    Snackbar.make(mBinding.root,"Nota Guardara",Snackbar.LENGTH_SHORT).show()
                }
                true

            }

            //TODO ELIMINAR DESDE AQUI EDICION, HACERLO  DESDE NOTAS ACTIVITY
            /*R.id.editar_nota ->{
                Snackbar.make(mBinding.root,"Nota Modificada",Snackbar.LENGTH_SHORT).show()
                true
            }*/

            R.id.borrar_nota ->{

                //TODO incluir verificacion
                Snackbar.make(mBinding.root,"Nota Borrada",Snackbar.LENGTH_SHORT).show()
                 true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title=getString(R.string.app_name)
        mActivity?.hideFabN(true)
        setHasOptionsMenu(false)
        super.onDestroy()
    }




}