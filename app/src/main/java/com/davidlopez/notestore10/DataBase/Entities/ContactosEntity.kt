package com.davidlopez.notestore10.DataBase.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Contactos")
data class ContactosEntity(@PrimaryKey(autoGenerate = true)
                            var id:Long=0,
                           var name:String,
                           var email:String="",//inicializamos el mail a vacio por si no se quiere ingresar
                           var phone:Int)

