package com.davidlopez.notestore10.UI.Notas

import com.davidlopez.notestore10.DataBase.Entities.NotasEntity

interface NotasAux {
    fun addNota(notasEntity: NotasEntity)
    fun updateNota(notasEntity: NotasEntity)
}