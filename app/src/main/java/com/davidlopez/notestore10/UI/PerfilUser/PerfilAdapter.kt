package com.davidlopez.notestore10.UI.PerfilUser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.davidlopez.notestore10.DataBase.Entities.UserEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.ImageController
import com.davidlopez.notestore10.databinding.ItemRvUserBinding

class PerfilAdapter(
    private var users: MutableList<UserEntity>,
    private var listener: OnClickListenerPerfil)
    :RecyclerView.Adapter<PerfilAdapter.ViewHolder>(){

    private lateinit var mContex: Context
    inner class ViewHolder (view:View) :RecyclerView.ViewHolder(view) {
        val binding=ItemRvUserBinding.bind(view)

        fun setListener(userEntity: UserEntity){

            //boton popup
            fun showPopUpMenu(view: View?) {
                val popupMenu = view?.let { PopupMenu(mContex, it) }
                if (popupMenu != null) {
                    popupMenu.inflate(R.menu.pop_up_menu_user)
                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.item_1 -> {
                                // editar
                                listener.onUpdateUser(userEntity.id)
                                true
                            }
                            R.id.item_2 -> {
                                // borrar
                                listener.onDeleteUser(userEntity)
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
                setOnClickListener { listener.onClick(userEntity) }
            }

        }
    }

// Metodos sobreescritos del viewholder ----------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        mContex=parent.context

        val vista=LayoutInflater.from(parent.context).inflate(R.layout.item_rv_user,parent,false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int= users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario=users.get(position)

        var idPhoto=usuario.id
        val uriphoto = ImageController.getImageUri(mContex,idPhoto)
        with(holder){
            setListener(usuario)

            binding.tvNombre.text=usuario.name
            binding.apellidos.text=usuario.surname
            binding.tvTelefono.text=usuario.phone
            binding.tvEmail.text=usuario.email
            binding.tvAdress.text=usuario.adress
            binding.tvTrabajo.text=usuario.work
            binding.imageView.setImageURI(uriphoto)//cargamos la imagen con el id para ponerla en el item:
        }
    }

    fun add (user: UserEntity){
        if (!users.contains(user)){
            users.add(user)
            notifyItemInserted(users.size-1)
        }
    }

    fun setUsers(user: MutableList<UserEntity>) {
        this.users=user
        notifyDataSetChanged()
    }

    fun update(userEntity: UserEntity){
        val index=users.indexOf(userEntity)
        if(index!=-1){
            users.set(index,userEntity)
            notifyItemChanged(index)
        }
    }

    fun delete(userEntity: UserEntity){
        val index=users.indexOf(userEntity)
        if (index !=-1){
            users.removeAt(index)
            notifyItemRemoved(index)
        }
    }


}
