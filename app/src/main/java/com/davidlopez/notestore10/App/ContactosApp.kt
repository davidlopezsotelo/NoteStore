package com.davidlopez.notestore10.App

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.davidlopez.notestore10.DataBase.RoomDB

class ContactosApp:Application() {

// aplicacion del patron singleton , para poder acceder a la base de datos desde cualquier punto de la aplicacion.
    companion object{
        lateinit var db:RoomDB
    }

    override fun onCreate() {
        super.onCreate()

        //MIGRACION DE LA BASE DE DATOS

        val migration_1_2 = object :Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ContactosEntity ADD COLUMN imagen TEXT NOT NULL DEFAULT 0")
            }
        }


        db=Room.databaseBuilder(this,RoomDB::class.java,"NoteStoreDataBase")
            .addMigrations(migration_1_2)
            .build()
    }
}