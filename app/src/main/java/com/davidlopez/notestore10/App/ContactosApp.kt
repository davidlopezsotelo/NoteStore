package com.davidlopez.notestore10.App

import android.app.Application
import androidx.room.Room
import com.davidlopez.notestore10.DataBase.RoomDB

class ContactosApp:Application() {

    companion object{
        lateinit var db:RoomDB
    }

    override fun onCreate() {
        super.onCreate()
        db=Room.databaseBuilder(this,RoomDB::class.java,"NoteStoreDataBase").build()
    }
}