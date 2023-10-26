package com.davidlopez.notestore10.DataBase.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Contactos")
data class ContactosEntity(@PrimaryKey(autoGenerate = true)
                            var id:Long=0,
                           var name:String,
                           var email:String="",//inicializamos el mail a vacio por si no se quiere ingresar
                           var phone:String,
                            var imagen:String=""){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactosEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

