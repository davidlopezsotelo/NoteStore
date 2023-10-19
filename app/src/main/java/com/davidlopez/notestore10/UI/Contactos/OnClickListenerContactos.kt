package com.davidlopez.notestore10.UI.Contactos

import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity


interface OnClickListenerContactos {
    fun onClick(contactosEntity: ContactosEntity)
    fun onDeleteContacto(contactosEntity: ContactosEntity)
    fun onUpdateContacto(contactosEntity: ContactosEntity)

}