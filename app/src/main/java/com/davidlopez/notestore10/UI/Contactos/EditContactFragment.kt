package com.davidlopez.notestore10.UI.Contactos

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.davidlopez.notestore10.App.ContactosApp
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.databinding.FragmentEditContactBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.util.concurrent.LinkedBlockingQueue

@Suppress("DEPRECATION")
class EditContactFragment : Fragment() {

    private val RC_GALLERY=23
    private  var photoSelectUri: Uri?=null

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

//boton seleccionar imagen----------------------------------------------------------------------------
        mBinding.btnSelectImage.setOnClickListener { selectImage() }

        //actualizar contacto -----------------------------------
        val id=arguments?.getLong(getString(R.string.arg_id),0)
        if (id !=null && id != 0L){
            isEditMode=true
            getContacto(id)
        } else{
            isEditMode=false
            mContactosEntity =  ContactosEntity(name="", phone="", email = "", imagen = 0)
        }

        /*
        * para que aparezca el action bar
        * ir a la carpeta res/values/themes/themes.xml
        * y borrar donde pone NoActionBar, en la linea 3
        * */

        setupActionBar()
        setupTextfields()// validar texfields en tiempo real
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK){
            if (requestCode == RC_GALLERY) {
                photoSelectUri = data?.data
                mBinding.imageViewPhoto.setImageURI(photoSelectUri)
            }
        }
    }

    private fun setupActionBar() {
        mActivity=activity as? ContactosActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //configuramos el titulo
        mActivity?.supportActionBar?.title= if (isEditMode) getString(R.string.edit_title_editar_contactos)//creamos el recurso
        else getString(R.string.edit_title_add_contactos)
        //mostrar menu
        setHasOptionsMenu(true)
    }

    private fun setupTextfields() {
        with(mBinding) {
            etName.addTextChangedListener { validateFields(tilName) }
            etPhone.addTextChangedListener { validateFields(tilPhone) }
        }
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
            etPhone.text=contactosEntity.phone.editable()
            etEmail.setText(contactosEntity.email).toString()
            // cargar la imagen !!!!!!!!!

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

                mBinding.btnSelectImage.setOnClickListener {

                }

                if (mContactosEntity !=null && validateFields(mBinding.tilPhone,mBinding.tilName)){//le pasamos los campos como parametro para veificar
                    with(mContactosEntity!!){
                        name = mBinding.etName.text.toString().trim()
                        phone = mBinding.etPhone.text.toString().trim()
                        email = mBinding.etEmail.text.toString().trim()
                        imagen=photoSelectUri.toString().toInt()// funciona??

                        // guardar la imagen!!!!!!!!



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
//Validar los campos del edit text--------------------------------------------------------------------

    // sin parametros, hay que repetir mucho codigo
    private fun validateFields(): Boolean {

        var isValid = true

        if (mBinding.etPhone.text.toString().trim().isEmpty()){
            mBinding.tilPhone.error=getString(R.string.helper_required)
            mBinding.tilPhone.requestFocus()

            // cargar la imagen !!!!!!!

            isValid=false
        }

        if (mBinding.etName.text.toString().trim().isEmpty()){
            mBinding.tilName.error=getString(R.string.helper_required)
            mBinding.tilName.requestFocus()

            //cargar imagen !!!!
            isValid=false
        }
        return isValid
    }

    //con parametros, codigo mas limpio y ampliable a mas texfields
    private fun validateFields(vararg textFields: TextInputLayout): Boolean{
        var isValid = true

        for (textField in textFields){
            if (textField.editText?.text.toString().trim().isEmpty()){
                textField.error = getString(R.string.helper_required)
                isValid = false
            } else textField.error = null
        }

         if (!isValid) Snackbar.make(mBinding.root,
            R.string.edit_store_message_valid,
            Snackbar.LENGTH_SHORT).show()

        return isValid
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

// insertar imagenes

    //seleccionar imagen---------------------------------------------
    private fun selectImage() {

        val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,RC_GALLERY)
    }






}