package com.davidlopez.notestore10.DataBase.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class PerfilEntity(@PrimaryKey (autoGenerate = true)
                      var id:Long=0,
                        var email:String,
                        var name:String="",
                        var surname:String="",
                        var phone:String="",
                        var adress:String="",
                        var work:String="")
{

    // metodos que fuerzan la carga de datos al actualizar la vista para que aparezcan los resultados actualizados
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PerfilEntity

        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }
}



