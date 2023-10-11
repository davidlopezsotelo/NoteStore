package com.davidlopez.notestore10.UI.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.UI.ContactosActivity
import com.davidlopez.notestore10.databinding.FragmentEditContactBinding

class EditContactFragment : Fragment() {

    private var mActivity:ContactosActivity?=null
    private lateinit var mBinding: FragmentEditContactBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,): View? {
       mBinding=FragmentEditContactBinding.inflate(inflater,container,false)
        return mBinding.root
    }




    //ocultar el teclado------------------------------------------------------------------------
    // no funciona correctamente, mejorar el metodo en toda la app********
    private fun hideKeyboard(){
        val imm=mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE)as InputMethodManager

        imm.hideSoftInputFromWindow(requireView().windowToken,0)
    }

    //ciclo de vida del fragment-----------------------------------------------

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title=getString(R.string.app_name)
        mActivity?.hideFab(true)

        setHasOptionsMenu(false)
        super.onDestroy()
    }


}