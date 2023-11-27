package com.davidlopez.notestore10

import android.app.Application
import androidx.room.Room
import com.davidlopez.notestore10.DataBase.RoomDB

class NoteStoreApp:Application() {

// aplicacion del patron singleton , para poder acceder a la base de datos desde cualquier punto de la aplicacion.
    companion object{
        lateinit var db:RoomDB
    }

    override fun onCreate() {
        super.onCreate()

        db =Room.databaseBuilder(this,RoomDB::class.java,"NoteStoreDataBase").build()
    }
}