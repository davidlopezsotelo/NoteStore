package com.davidlopez.notestore10.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davidlopez.notestore10.DataBase.DAOs.ContactosDao
import com.davidlopez.notestore10.DataBase.DAOs.NotasDao
import com.davidlopez.notestore10.DataBase.DAOs.UserDao
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity
import com.davidlopez.notestore10.DataBase.Entities.NotasEntity
import com.davidlopez.notestore10.DataBase.Entities.UserEntity

//

@Database( entities = arrayOf(ContactosEntity::class,NotasEntity::class,UserEntity::class),
                      version =1)

abstract class RoomDB : RoomDatabase(){

    abstract fun ContactosDao(): ContactosDao
    abstract fun notasDao(): NotasDao
    abstract fun userDao():UserDao


}