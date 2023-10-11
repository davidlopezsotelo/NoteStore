package com.davidlopez.notestore10

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity
import com.davidlopez.notestore10.UI.ContactosActivity
import com.davidlopez.notestore10.databinding.ActivityContactosBinding

class ContactosAdapter(private var cotactos:MutableList<ContactosEntity>, private var listener: ContactosActivity):
        RecyclerView.Adapter<ContactosAdapter.ViewHolder>(){

    private lateinit var mContex: Context




    inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view){

        // val binding=ItemStoreBinding.bind(view)    original

        val binding=ActivityContactosBinding.bind(view)

        fun setListener(contactosEntity: ContactosEntity){
            binding.root.setOnClickListener {  listener.onClick(contactosEntity)}
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContex=parent.context

        val view=LayoutInflater.from(mContex).inflate(R.layout.item_contacto,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =cotactos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contactos=cotactos.get(position)

        with(holder){
            setListener(contactos)
            // revisar !!!!!!!!!!!!!!!!!!!!!!!!!!
            binding.tvContactos.text=contactos.name
        }
    }
}