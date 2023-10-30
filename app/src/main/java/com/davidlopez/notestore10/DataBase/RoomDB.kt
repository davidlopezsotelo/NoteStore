package com.davidlopez.notestore10.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davidlopez.notestore10.DataBase.DAOs.ContactosDao
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity


@Database( entities = arrayOf(ContactosEntity::class), version =1)
abstract class RoomDB : RoomDatabase(){

    abstract fun ContactosDao(): ContactosDao


}