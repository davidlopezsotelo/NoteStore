package com.davidlopez.notestore10.UI.PerfilUser

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
import androidx.fragment.app.Fragment
import com.davidlopez.notestore10.NoteStoreApp
import com.davidlopez.notestore10.DataBase.Entities.UserEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.ImageController
import com.davidlopez.notestore10.databinding.FragmentPerfilBinding
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.LinkedBlockingQueue

@Suppress("DEPRECATION")
class PerfilFragment:Fragment() {

    private val RC_GALLERY=36
    private  var photoSelectUri: Uri?=null
    private var mActivity:PerfilActivity?=null
    private lateinit var mBinding:FragmentPerfilBinding
    private lateinit var mContext: Context

    // actualizar usuario--------------------------------------------------------------------------
    private var isEditMode:Boolean=false
    private var mUserEntity: UserEntity?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding= FragmentPerfilBinding.inflate(inflater,container,false)
        return mBinding.root
    }
    //creamos el menu-------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //boton seleccionar imagen----------------------------------------------------------------------------

        mBinding.btnSelectImage.setOnClickListener { selectImage() }

        //actualizar contacto --------------------------------------------------------------------------------
        val id = arguments?.getLong(getString(R.string.arg_id), 0)
        if (id != null && id != 0L) {
            isEditMode = true
            getUser(id)
        } else {
            isEditMode = false
            mUserEntity =
                UserEntity(name = "", surname = "", phone = "", email = "", adress = "", work = "")
        }
        /*
        * para que aparezca el action bar
        * ir a la carpeta res/values/themes/themes.xml
        * y borrar donde pone NoActionBar, en la linea 3
        * */
        setupActionBar()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            if (requestCode == RC_GALLERY) {
                photoSelectUri = data?.data
                mBinding.imageViewPhoto.setImageURI(photoSelectUri)

            }
        }
    }

    private fun setupActionBar() {
        mActivity=activity as? PerfilActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //configuramos el titulo
        mActivity?.supportActionBar?.title= if (isEditMode) "Editar Usuario"//creamos el recurso
        else "Añadir usuario"
        //mostrar menu
        setHasOptionsMenu(true)
    }

    private fun getUser(id: Long) {
        val queue = LinkedBlockingQueue<UserEntity?>()

        Thread{

            mUserEntity= NoteStoreApp.db.userDao().getUserById(id)
            queue.add(mUserEntity)
        }.start()
        queue.take()?.let { setUiUser(it) }
    }

    //le pasamos los datos seleccionados del usuario
    private fun setUiUser(userEntity: UserEntity) {
        mContext= this.requireContext()
        val idFoto=userEntity.id

        with(mBinding){
            etName.setText(userEntity.name)
            etPhone.text=userEntity.phone.editable()
            etEmail.setText(userEntity.email).toString()
            etSurname.setText(userEntity.surname)
            etAdress.setText(userEntity.adress)
            etWork.setText(userEntity.work)
            // cargar la imagen !!!!!!!!!
            imageViewPhoto.setImageURI(ImageController.getImageUri(mContext,idFoto))
        }
    }

    private fun String.editable(): Editable = Editable.Factory.getInstance().newEditable(this)

    //sobreescribimos los metodos para el menu:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_user,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //ocultar el teclado--------------------------------------------------------------------------------------------
    private fun hideKeyboard(){
        val imm=mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE)as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken,0)
    }
    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }//-----------------------------------------------------------------------------------------------------------------

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                hideKeyboard()//oculta el teclado al volver a la vista
                mActivity?.onBackPressedDispatcher?.onBackPressed() //ir hacia atras con el boton??revisar
                true
            }

// guardar en la base de datos--------------------------------------------------------------------------
            R.id.action_save -> {


                if (mActivity !=null ){//le pasamos los campos como parametro para veificar
                    with(mUserEntity!!){
                        name = mBinding.etName.text.toString().trim()
                        surname=mBinding.etSurname.text.toString().trim()
                        phone = mBinding.etPhone.text.toString().trim()
                        email = mBinding.etEmail.text.toString().trim()
                        adress=mBinding.etAdress.text.toString().trim()
                        work=mBinding.etWork.text.toString().trim()
                    }

                    val queue = LinkedBlockingQueue<UserEntity>()
                    Thread{

                        if (isEditMode){
                            NoteStoreApp.db.userDao().updateUser(mUserEntity!!)
                            Snackbar.make(mBinding.root,"Usuario actualizado",
                                Snackbar.LENGTH_SHORT).show()

                        } else mUserEntity!!.id= NoteStoreApp.db.userDao().addUser(mUserEntity!!)

                        // guardar imagen------------------------------
                        guardarImagen(id = mUserEntity!!.id)
                        queue.add(mUserEntity)

                    }.start()

                    with(queue.take()){
                        hideKeyboard()//para ocultar el teclado

                        if (isEditMode){
                            mActivity?.updateUser(this)
                            Snackbar.make(mBinding.root,R.string.edit_message_update_sucess,
                                Snackbar.LENGTH_SHORT).show()

                        }else{
                            mActivity?.addUser(this)
                            Toast.makeText(mActivity,R.string.edit_message_save_sucess, Toast.LENGTH_LONG).show()

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


//funcion que guarda la foto segun el id--------------------------------------------------------------

    private fun guardarImagen(id: Long) {
        photoSelectUri?.let {
            this.context?.let { it1 -> ImageController.saveImage(it1,id,it) } //???????

        }

        Snackbar.make(mBinding.root,"el id de la foto es:$id",Snackbar.LENGTH_LONG).show()
    }


    //ciclo de vida del fragment----------------------------------------------------------------------------------
    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title=getString(R.string.app_name)
        setHasOptionsMenu(false)
        super.onDestroy()
    }

// insertar imagenes

    //seleccionar imagen---------------------------------------------
    private fun selectImage() {
        ImageController.selectPhotoFromGallery(this,RC_GALLERY)
    }




}//END FRAGMENT