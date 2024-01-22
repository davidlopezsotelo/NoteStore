package com.davidlopez.notestore10.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davidlopez.notestore10.DataBase.DAOs.ContactosDao
import com.davidlopez.notestore10.DataBase.DAOs.NotasDao
import com.davidlopez.notestore10.DataBase.DAOs.PerfilDao
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity
import com.davidlopez.notestore10.DataBase.Entities.NotasEntity
import com.davidlopez.notestore10.DataBase.Entities.PerfilEntity

//

@Database( entities = [ContactosEntity::class, NotasEntity::class, PerfilEntity::class],
                      version =1)

abstract class RoomDB : RoomDatabase(){

    abstract fun ContactosDao(): ContactosDao
    abstract fun notasDao(): NotasDao
    abstract fun userDao():PerfilDao


}