package com.davidlopez.notestore10.UI.PerfilUser

import com.davidlopez.notestore10.DataBase.Entities.PerfilEntity

interface OnClickListenerPerfil {
    fun onClick(perfilEntity: PerfilEntity)
    fun onDeleteUser(perfilEntity: PerfilEntity)
    fun onUpdateUser(usuarioId: Long)
}