package com.davidlopez.notestore10.UI.Notas

import com.davidlopez.notestore10.DataBase.Entities.NotasEntity

interface OnClickListenerNotas {

    fun onClick(notasEntity: NotasEntity)

    //actualizar registro
    fun onFavoriteNota(notasEntity: NotasEntity)

    //borrar registri
    fun onDeleteNota(notasDB: NotasEntity)
}