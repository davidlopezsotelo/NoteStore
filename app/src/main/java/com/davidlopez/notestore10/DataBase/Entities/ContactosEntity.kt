package com.davidlopez.notestore10.DataBase.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Contactos")
data class ContactosEntity(@PrimaryKey(autoGenerate = true)
                            var id:Long=0,
                           var name:String,
                           var surname:String,
                           var email:String,
                           var phone:Int)

