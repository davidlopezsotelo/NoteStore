package com.davidlopez.notestore10.UI.Contactos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.databinding.ItemContactosBinding

//https://www.youtube.com/watch?v=k3zoVAMuW5w&t=4s

// RECYCLERVIEW==================================================================================

class ContactosAdapter(
    private var cotactos:MutableList<ContactosEntity>, //lista de contactos
    private var listener: OnClickListenerContactos):
    RecyclerView.Adapter<ContactosAdapter.ViewHolder>(){

    private lateinit var mContex: Context

    inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
        val binding= ItemContactosBinding.bind(view)

        fun setListener(contactosEntity: ContactosEntity){
            //boton popup
            fun showPopUpMenu(view: View?) {
                val popupMenu = view?.let { PopupMenu(mContex, it) }
                if (popupMenu != null) {
                    popupMenu.inflate(R.menu.popup_menu_contact)
                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.item_1 -> {
                                // editar
                                listener.onUpdateContacto(contactosEntity.id)
                                true
                            }
                            R.id.item_2 -> {
                                // borrar
                                listener.onDeleteContacto(contactosEntity)
                                true
                            }  else -> false
                        }
                    }
                    popupMenu.show()
                }
            }

            with(binding.root) {
                binding.btnPopupMenu.setOnClickListener {
                    view -> showPopUpMenu(view)
                }
                setOnClickListener { listener.onClick(contactosEntity) }
            }
        }
    }


// Metodos sobreescritos del viewholder ----------------------------------------------------------------------------------------------

    // metodo que devuelve al viewholder cada elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        mContex=parent.context

        val view=LayoutInflater.from(mContex).inflate(R.layout.item_contactos,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =cotactos.size  // metodo que devuelve el tamaño del listado

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contactos=cotactos.get(position)

        var idPhoto=contactos.id
        val uriphoto = ImageController.getImageUri(mContex,idPhoto)
        with(holder){
            setListener(contactos)
            binding.tvName.text=contactos.name
            binding.imageViewPhoto.setImageURI(uriphoto)//cargamos la imagen con el id para ponerla en el item:
        }
    }

// End Methods-----------------------------------------------------------------------------------------------------------------


    fun add (contacto:ContactosEntity){
        if (!cotactos.contains(contacto)){
            cotactos.add(contacto)
            notifyItemInserted(cotactos.size-1)
        }
    }

    fun setContactos(contactos: MutableList<ContactosEntity>) {

        this.cotactos=contactos
        notifyDataSetChanged()
    }
    fun update(contactosEntity: ContactosEntity){
        val index=cotactos.indexOf(contactosEntity)
        if (index !=-1){
            cotactos.set(index,contactosEntity)
            notifyItemChanged(index)
        }
    }
    fun delete(contactosEntity: ContactosEntity){
        val index=cotactos.indexOf(contactosEntity)
        if (index !=-1){
            cotactos.removeAt(index)
            notifyItemRemoved(index)
        }
    }

}