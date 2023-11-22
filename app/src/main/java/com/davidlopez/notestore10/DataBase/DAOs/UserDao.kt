package com.davidlopez.notestore10.DataBase.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.davidlopez.notestore10.DataBase.Entities.UserEntity

@Dao
interface UserDao {


   @Query("SELECT * FROM user ")
    fun getAllUser(): MutableList<UserEntity>

    @Query("SELECT * FROM user WHERE email= :email")
    fun getUserByMail(email:String):UserEntity?

    @Insert
    fun addUser(user: UserEntity)

    @Update
    fun updateUser(user: UserEntity)

    @Delete
    fun deleteAllUser(user: UserEntity)




}