package com.davidlopez.notestore10.DataBase.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "NotasEntity")
data class NotasEntity (@PrimaryKey(autoGenerate = true)
                        var id:Long=0,
                        var name:String,
                        var isFaborite:Boolean=false)
