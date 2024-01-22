package com.davidlopez.notestore10.UI.PerfilUser

import com.davidlopez.notestore10.DataBase.Entities.PerfilEntity

interface PerfilAux {

    fun addUser(perfilEntity: PerfilEntity)
    fun updateUser(cuserEntity: PerfilEntity)

}