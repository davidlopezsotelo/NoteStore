package com.davidlopez.notestore10.DataBase.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.davidlopez.notestore10.DataBase.Entities.ContactosEntity


@Dao
 interface ContactosDao {

  @Query ("SELECT * FROM contactos")
  fun getAllContactos():MutableList<ContactosEntity>

 @Query("SELECT * FROM contactos where id= :id")
 fun getContactoById(id: Long):ContactosEntity

 @Insert
 fun addNota(contactos: ContactosEntity):Long // contactosEntity :Long

 @Update
 fun updateNota(ncontactos: ContactosEntity)

 @Delete
 fun deleteAll(contactos: ContactosEntity)




}