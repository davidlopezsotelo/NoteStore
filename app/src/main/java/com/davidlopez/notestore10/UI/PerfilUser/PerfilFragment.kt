package com.davidlopez.notestore10.UI.PerfilUser

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.Contactos.ContactosActivity
import com.davidlopez.notestore10.UI.Contactos.ImageController
import com.davidlopez.notestore10.databinding.FragmentPerfilBinding

class PerfilFragment : Fragment() {

    private val RC_GALLERY=27
    private lateinit var mBinding: FragmentPerfilBinding
    private var mActivity:PerfilActivity?=null
    private lateinit var mContex: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
       mBinding= FragmentPerfilBinding.inflate(inflater,container,false)
        return mBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //boton seleccionar imagen----------------------------------------------------------------------------
        mBinding.btnSelectImage.setOnClickListener { selectImage() }


        //menu
        setupActionBar()
        setupTextfields()
    }

    private fun setupTextfields() {

        val nombre=mBinding.etName.text.toString()
        val apellido= mBinding.etSurname.text.toString()
        val telefono=mBinding.etPhone.text.toString()
        val mail=mBinding.etEmail.text.toString()
        val adress=mBinding.etAdress.text.toString()
        val work=mBinding.etWork.text.toString()


    }

    private fun setupActionBar() {
        mActivity=activity as? PerfilActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title=getString(R.string.edit_title_editar_perfil)
        setHasOptionsMenu(true)
    }


    private fun selectImage() {
        ImageController.selectPhotoFromGallery(this,RC_GALLERY)
    }


}

