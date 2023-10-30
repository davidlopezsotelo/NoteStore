package com.davidlopez.notestore10.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davidlopez.notestore10.DataBase.DAOs.ContactosDao
import com.davidlopez.notestore10.DataBase.DAOs.NotasDao
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity
import com.davidlopez.notestore10.DataBase.Entities.NotasEntity

//

@Database( entities = arrayOf(ContactosEntity::class,NotasEntity::class),
                      version =1, exportSchema = false)

abstract class RoomDB : RoomDatabase(){

    abstract fun ContactosDao(): ContactosDao
    abstract fun notasDao(): NotasDao


}