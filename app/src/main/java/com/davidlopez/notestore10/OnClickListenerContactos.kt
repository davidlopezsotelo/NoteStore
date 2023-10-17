package com.davidlopez.notestore10

import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity


interface OnClickListenerContactos {
    fun onClick(contactosEntity: ContactosEntity)
    fun onDeleteContacto(contactosEntity: ContactosEntity)

}