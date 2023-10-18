package com.davidlopez.notestore10.UI.Fragments

import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity



interface ContactosAux {

    fun hideFab(isVisible:Boolean=false)
    // actualizar la vista desde el fragment
    fun addContact(contactosEntity: ContactosEntity)
    fun updateContact(contactosEntity: ContactosEntity)


}