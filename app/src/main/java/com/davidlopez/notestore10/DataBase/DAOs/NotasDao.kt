package com.davidlopez.notestore10.DataBase.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.davidlopez.notestore10.DataBase.Entities.NotasEntity

@Dao
interface NotasDao {
    @Query("SELECT * FROM NotasEntity")
    fun getAllNotas():MutableList<NotasEntity>

    @Query("SELECT * FROM NotasEntity where id= :id")
    fun getNotaById(id: Long):NotasEntity

    @Insert
    fun addNota(notasEntity: NotasEntity):Long

    @Update
    fun updateNota(notasEntity: NotasEntity)

    @Delete
    fun deleteAll(notasEntity: NotasEntity)
}