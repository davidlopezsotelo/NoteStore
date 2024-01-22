package com.davidlopez.notestore10.DataBase.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.davidlopez.notestore10.DataBase.Entities.PerfilEntity

@Dao
interface PerfilDao {


   @Query("SELECT * FROM user ")
    fun getAllUser(): MutableList<PerfilEntity>

 @Query("SELECT * FROM user where id= :id")
 fun getUserById(id: Long): PerfilEntity

    @Query("SELECT * FROM user WHERE email= :email")
    fun getUserByMail(email:String):PerfilEntity?

    @Insert
    fun addUser(user: PerfilEntity):Long

    @Update
    fun updateUser(user: PerfilEntity)

    @Delete
    fun deleteAllUser(user: PerfilEntity)




}