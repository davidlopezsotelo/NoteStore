package com.davidlopez.notestore10.UI.Notas

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.davidlopez.notestore10.DataBase.Entities.NotasEntity
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.databinding.ItemNotaBinding

class NotasAdapter(private var notas:MutableList<NotasEntity>,
                   private var listener: OnClickListenerNotas):
    RecyclerView.Adapter<NotasAdapter.ViewHolder>() {

    private lateinit var mContex: Context

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding=ItemNotaBinding.bind(view)
        fun setListener(notasEntity: NotasEntity){

            //boton popup
            fun showPopUpMenu(view: View?) {
                val popupMenu = view?.let { PopupMenu(mContex, it) }
                if (popupMenu != null) {
                    popupMenu.inflate(R.menu.popup_menu_notas)
                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.item_1 -> {

                                val posicion = adapterPosition
                                        if (posicion != RecyclerView.NO_POSITION) {
                                            val itemActual = notas[posicion]
                                            val texto=binding.tvNotaResum.text.toString()
                                            enviarTextoACalendario(
                                                itemView.context,
                                                texto
                                            )
                                        }
                                true
                            }
                            R.id.item_2 -> {
                                // borrar
                                listener.onDeleteNota(notasEntity)
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
                setOnClickListener { listener.onClick(notasEntity) }
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContex=parent.context

        val view = LayoutInflater.from(mContex).inflate(R.layout.item_nota,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = notas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notas=notas.get(position)

        with(holder){
            setListener(notas)
            binding.tvName.text=notas.name
            binding.cbNotas.isChecked=notas.isFaborite
            binding.tvNotaResum.text=notas.texto
        }
    }

    fun add(nota: NotasEntity) {

        if (!notas.contains(nota))
        {notas.add(nota)
            notifyItemInserted(notas.size-1)}
    }
    fun setNotas(notas: MutableList<NotasEntity>) {
        this.notas=notas
        notifyDataSetChanged()
    }

    // actualizar la nota --------------------------------------------------------------------------
    fun update(notasEntity: NotasEntity) {

        val index=notas.indexOf(notasEntity)
        if (index!=-1){
            notas.set(index,notasEntity)
            notifyItemChanged(index)
        }
    }
//borrar nota-----------------------------------------------------------------------------------

    fun delete(notasEntity: NotasEntity) {
        val index=notas.indexOf(notasEntity)
        if (index!=-1){
            notas.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    private fun enviarTextoACalendario(context: Context, texto: String) {
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, System.currentTimeMillis())
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, System.currentTimeMillis() + (60*60*1000))
            .putExtra(CalendarContract.Events.TITLE, "Mi Evento")
            .putExtra(CalendarContract.Events.DESCRIPTION, texto)
        context.startActivity(intent)
    }
}


