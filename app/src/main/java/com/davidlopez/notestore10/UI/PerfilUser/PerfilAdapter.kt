package com.davidlopez.notestore10.UI.PerfilUser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davidlopez.notestore10.DataBase.Entities.UserEntity
import com.davidlopez.notestore10.R
import kotlinx.coroutines.CoroutineScope

class PerfilAdapter(
    val userList: MutableList<UserEntity>,
    val listener: CoroutineScope
)
    :RecyclerView.Adapter<PerfilAdapter.ViewHolder>(){
    inner class ViewHolder (ItemView:View) :RecyclerView.ViewHolder(ItemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista=LayoutInflater.from(parent.context).inflate(R.layout.item_rv_user,parent,false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuatio=userList[position]
    }


}
