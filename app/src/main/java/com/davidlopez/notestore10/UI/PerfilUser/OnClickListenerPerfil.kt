package com.davidlopez.notestore10.UI.PerfilUser

import com.davidlopez.notestore10.DataBase.Entities.UserEntity

interface OnClickListenerPerfil {
    fun onClick(userEntity: UserEntity)
    fun onDeleteUser(userEntity: UserEntity)
    fun onUpdateUser(usuarioId: Long)
}