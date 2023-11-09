package com.davidlopez.notestore10.UI.PerfilUser

import com.davidlopez.notestore10.DataBase.Entities.UserEntity

interface PerfilAux {

    fun addUser(userEntity: UserEntity)
    fun updateUser(cuserEntity: UserEntity)

}