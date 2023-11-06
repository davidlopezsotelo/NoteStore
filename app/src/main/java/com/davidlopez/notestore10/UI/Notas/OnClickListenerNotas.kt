package com.davidlopez.notestore10.UI.Notas

import com.davidlopez.notestore10.DataBase.Entities.NotasEntity

interface OnClickListenerNotas {

    fun onClick(notasId: NotasEntity)
    //actualizar registro
    fun onFavoriteNota(notasEntity: NotasEntity)
    //borrar registro
    fun onDeleteNota(notasDB: NotasEntity)
}