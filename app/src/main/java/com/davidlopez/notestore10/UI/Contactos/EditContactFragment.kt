package com.davidlopez.notestore10.UI.Contactos

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.davidlopez.notestore10.App.ContactosApp
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.databinding.FragmentEditContactBinding
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.LinkedBlockingQueue

@Suppress("DEPRECATION")
class EditContactFragment : Fragment() {

    private var mActivity: ContactosActivity?=null
    private lateinit var mBinding: FragmentEditContactBinding

    // actualizar contacto--------------------------------------------------------------------------
    private var isEditMode:Boolean=false
    private var mContactosEntity:ContactosEntity?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
       mBinding=FragmentEditContactBinding.inflate(inflater,container,false)
        return mBinding.root
    }


    //creamos el menu-------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //actualizar contacto -----------------------------------
        val id=arguments?.getLong(getString(R.string.arg_id),0)
        if (id !=null && id != 0L){
            isEditMode=true
            getContacto(id)

        } else{
            isEditMode=false
            mContactosEntity =  ContactosEntity(name="", phone="", email = "")
        }


        /*
        * para que aparezca el action bar
        * ir a la carpeta res/values/themes/themes.xml
        * y borrar donde pone NoActionBar, en la linea 3
        * */

        mActivity=activity as? ContactosActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //configuramos el titulo
        mActivity?.supportActionBar?.title=getString(R.string.edit_title_add_contactos)//creamos el recurso

        //mostrar menu
        setHasOptionsMenu(true)
    }

    // actualizar contacto ------------------------------------------------------------------------
    private fun getContacto(id: Long) {
        val queue =LinkedBlockingQueue<ContactosEntity?>()
        Thread{

            mContactosEntity=ContactosApp.db.ContactosDao().getContactoById(id)
            queue.add(mContactosEntity)
        }.start()
        queue.take()?.let { setUiContacto(it) }
    }

    //le pasamos los datos seleccionados del contacto
    private fun setUiContacto(contactosEntity: ContactosEntity) {
        with(mBinding){
            etName.setText(contactosEntity.name)
           // etPhone.setText(contactosEntity.phone)
            etPhone.text=contactosEntity.phone.editable()
            etEmail.setText(contactosEntity.email).toString()
        }
    }
    private fun String.editable():Editable=Editable.Factory.getInstance().newEditable(this)


    //sobreescribimos los metodos para el menu:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save_contactos,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                hideKeyboard()//oculta el teclado al volver a la vista
                mActivity?.onBackPressedDispatcher?.onBackPressed() //ir hacia atras con el boton??revisar
                true
            }

// guardar en la base de datos--------------------------------------------------------------------------
            R.id.action_save -> {

                if (mContactosEntity !=null){
                    with(mContactosEntity!!){
                        name = mBinding.etName.text.toString().trim()
                        phone = mBinding.etPhone.text.toString().trim()
                        email = mBinding.etEmail.text.toString().trim()
                    }

                    val queue = LinkedBlockingQueue<ContactosEntity>()
                    Thread{

                        if (isEditMode){
                            ContactosApp.db.ContactosDao().updateContacto(mContactosEntity!!)
                            Snackbar.make(mBinding.root,R.string.edit_message_update_sucess,Snackbar.LENGTH_SHORT).show()
                        } else mContactosEntity!!.id=ContactosApp.db.ContactosDao().addContacto(mContactosEntity!!)
                        queue.add(mContactosEntity)
                    }.start()

                    with(queue.take()){
                        hideKeyboard()//para ocultar el teclado

                        if (isEditMode){
                            mActivity?.updateContact(this)
                            Snackbar.make(mBinding.root,R.string.edit_message_update_sucess,Snackbar.LENGTH_SHORT).show()
                        }else{
                            mActivity?.addContact(this)
                            Toast.makeText(mActivity,R.string.edit_message_save_sucess,Toast.LENGTH_LONG).show()
                            //mActivity?.onBackPressedDispatcher?.onBackPressed()
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        //return super.onOptionsItemSelected(item) , se lo pasamos al else
    }

//ocultar el teclado------------------------------------------------------------------------
    private fun hideKeyboard(){
        val imm=mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE)as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken,0)
    }

//ciclo de vida del fragment-----------------------------------------------------------------
    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title=getString(R.string.app_name)
        mActivity?.hideFab(true)
        setHasOptionsMenu(false)
        super.onDestroy()
    }
}