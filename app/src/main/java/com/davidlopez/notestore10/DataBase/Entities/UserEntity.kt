package com.davidlopez.notestore10.DataBase.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserEntity(@PrimaryKey(autoGenerate = true)
                      var id:Long=0,
                      var name:String="",
                      var surname:String="",
                      var email:String="",//inicializamos el mail a vacio por si no se quiere ingresar
                      var phone:String="",
                      var adress:String="",
                      var work:String="")
