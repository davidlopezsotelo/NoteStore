package com.davidlopez.notestore10.DataBase.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "NotasEntity")
data class NotasEntity (@PrimaryKey(autoGenerate = true)
                        var id:Long=0,
                        var name:String,
                        var texto:String="",
                        var isFaborite:Boolean=false)
{

    // metodos que fuerzan la carga de datos al actualizar la vista para que aparezcan los resultados actualizados
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotasEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
