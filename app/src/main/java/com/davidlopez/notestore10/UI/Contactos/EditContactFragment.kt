package com.davidlopez.notestore10.UI.Contactos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.davidlopez.notestore10.NoteStoreApp
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity
import com.davidlopez.notestore10.ImageController
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.databinding.FragmentEditContactBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.util.concurrent.LinkedBlockingQueue

//@Suppress("DEPRECATION")
class EditContactFragment : Fragment() {

    private val RC_GALLERY=23
    private  var photoSelectUri: Uri?=null
    private var mActivity: ContactosActivity?=null
    private lateinit var mBinding: FragmentEditContactBinding
    private lateinit var mContex: Context

    // actualizar contacto--------------------------------------------------------------------------
    private var isEditMode:Boolean=false
    private var mContactosEntity:ContactosEntity?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        // Mostrar el ActionBar
        (activity as AppCompatActivity).supportActionBar?.show()

       mBinding=FragmentEditContactBinding.inflate(inflater,container,false)
        return mBinding.root



    }

//creamos el menu-------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//TODO REVISAR BOTON ATRAS EN FRAGMENT


    // Esto es necesario para que el fragmento intercepte el evento de pulsación del botón de atrás antes que la actividad.
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Aquí puedes manejar el evento de pulsación del botón de atrás como quieras.
            // Por ejemplo, si quieres mostrar un mensaje, puedes hacerlo así:
            Toast.makeText(context, "Botón de atrás pulsado", Toast.LENGTH_SHORT).show()

            // Si quieres que el botón de atrás tenga el comportamiento por defecto (es decir, que quite el fragmento de la pila de retroceso),
            // puedes llamar a isEnabled = false antes de llamar a requireActivity().onBackPressedDispatcher.onBackPressed()
            isEnabled = false
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    })



//boton seleccionar imagen----------------------------------------------------------------------------

        mBinding.btnSelectImage.setOnClickListener { selectImage() }

//actualizar contacto --------------------------------------------------------------------------------
        val id=arguments?.getLong(getString(R.string.arg_id),0)
        if (id !=null && id != 0L){
            isEditMode=true
            getContacto(id)
        } else{
            isEditMode=false
            mContactosEntity =  ContactosEntity(name ="", phone ="", email = "")
        }
                        /*
                        * para que aparezca el action bar
                        * ir a la carpeta res/values/themes/themes.xml
                        * y borrar donde pone NoActionBar, en la linea 3
                        * */
        setupActionBar()
        setupTextfields()// validar texfields en tiempo real
    }



    @Suppress("DEPRECATION")
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
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
    }

    private fun setupTextfields() {
        with(mBinding) {
            etName.addTextChangedListener { validateFields(tilName) }
            etPhone.addTextChangedListener { validateFields(tilPhone) }
        }
    }

// actualizar contacto --------------------------------------------------------------------------------------
    private fun getContacto(id: Long) {
        val queue =LinkedBlockingQueue<ContactosEntity?>()

        Thread{

            mContactosEntity= NoteStoreApp.db.ContactosDao().getContactoById(id)
            queue.add(mContactosEntity)
        }.start()
        queue.take()?.let { setUiContacto(it) }
    }

    //le pasamos los datos seleccionados del contacto
    private fun setUiContacto(contactosEntity: ContactosEntity) {
        mContex= this.requireContext()
        val idFoto=contactosEntity.id

        with(mBinding){
            etName.setText(contactosEntity.name)
            etPhone.text=contactosEntity.phone.editable()
            etEmail.setText(contactosEntity.email).toString()
            // cargar la imagen !!!!!!!!!
            imageViewPhoto.setImageURI(ImageController.getImageUri(mContex,idFoto))
        }
    }

    private fun String.editable():Editable=Editable.Factory.getInstance().newEditable(this)

    //sobreescribimos los metodos para el menu:
    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save_contactos,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                hideKeyboard()//oculta el teclado al volver a la vista
                mActivity?.onBackPressedDispatcher?.onBackPressed() //ir hacia atras con el boton??revisar
                true
            }

// guardar en la base de datos--------------------------------------------------------------------------
            R.id.action_save -> {


                if (mContactosEntity !=null &&
                    validateFields(mBinding.tilPhone,mBinding.tilName)){
                    
                    //le pasamos los campos como parametro para veificar
                    with(mContactosEntity!!){
                        name = mBinding.etName.text.toString().trim()
                        phone = mBinding.etPhone.text.toString().trim()

                        if (isValidEmail(email))
                        email = mBinding.etEmail.text.toString().trim()
                        else Toast.makeText(mActivity,"Formato de email, incorrecto.",Toast.LENGTH_LONG).show()


                    }
                    val queue = LinkedBlockingQueue<ContactosEntity>()
                    Thread{

                        if (isEditMode){
                            NoteStoreApp.db.ContactosDao().updateContacto(mContactosEntity!!)
                            Snackbar.make(mBinding.root,R.string.edit_message_update_sucess,Snackbar.LENGTH_SHORT).show()

                        } else mContactosEntity!!.id= NoteStoreApp.db.ContactosDao().addContacto(mContactosEntity!!)

                        // guardar imagen------------------------------
                        guardarImagen(id = mContactosEntity!!.id)
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

                            requireActivity().onBackPressedDispatcher.onBackPressed()//retroceder al pulsar el boton guardar
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        //return super.onOptionsItemSelected(item) , se lo pasamos al else
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^A-Za-z([@]{1})(.{1,})(\\.)(.{1,})".toRegex()
        return emailRegex.matches(email)
    }


//funcion que guarda la foto segun el id--------------------------------------------------------------

    private fun guardarImagen(id: Long) {
        photoSelectUri?.let {
            this.context?.let { it1 -> ImageController.saveImage(it1,id,it) } //???????

        }

        Snackbar.make(mBinding.root,"el id de la foto es:$id",Snackbar.LENGTH_LONG).show()
    }


//Validar los campos del edit text--------------------------------------------------------------------

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


//ocultar el teclado--------------------------------------------------------------------------------------------
    private fun hideKeyboard(){
        val imm=mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE)as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken,0)
    }

    override fun onDestroyView() {
        hideKeyboard()

        // Ocultar el ActionBar
        (activity as AppCompatActivity).supportActionBar?.hide()

        super.onDestroyView()
    }//-----------------------------------------------------------------------------------------------------------------


//ciclo de vida del fragment----------------------------------------------------------------------------------
    @Suppress("DEPRECATION")
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
        ImageController.selectPhotoFromGallery(this,RC_GALLERY)
    }

}