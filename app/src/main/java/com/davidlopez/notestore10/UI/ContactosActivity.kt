package com.davidlopez.notestore10.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.GridLayoutManager
import com.davidlopez.notestore10.ContactosAdapter
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity
import com.davidlopez.notestore10.UI.Fragments.ContactosAux
import com.davidlopez.notestore10.databinding.ActivityContactosBinding

//main-----

class ContactosActivity : AppCompatActivity() ,ContactosAux,OnClickListener {

    private lateinit var mBinding: ActivityContactosBinding
    private lateinit var mAdapter: ContactosAdapter
    private lateinit var mGridLayout: GridLayoutManager //??????

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityContactosBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // FRAGMENT---------------------------------------------------------------------------

        //Binding.fab.setOnClickListener { launchEditFragment() }//creamos esta funcion en ...
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        mAdapter= ContactosAdapter(mutableListOf(),this)
        mGridLayout= GridLayoutManager(this,1)//numero de elementos por columna

        mBinding.reciclerViewContactos.apply {
            setHasFixedSize(true)
            layoutManager=mGridLayout
            adapter=mAdapter
        }

    }



//configuramos boton atras---------------------------------------
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MenuPrincipalActivity::class.java))
    }

    override fun hideFab(isVisible: Boolean) {

    }

    override fun addContact(contactosEntity: ContactosEntity) {
       // mAdapter.add(notasEntity))
    }

    override fun updateContact(contactosEntity: ContactosEntity) {

    }



    fun onClick(p0: ContactosEntity) {

    }

    override fun onClick(p0: View?) {

    }
}