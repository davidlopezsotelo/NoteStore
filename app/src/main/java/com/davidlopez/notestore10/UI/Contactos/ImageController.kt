package com.davidlopez.notestore10.UI.Contactos

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.File

object ImageController {

    fun selectPhotoFromGallery(activity: EditContactFragment, code:Int){

        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        activity.startActivityForResult(intent,code)

    }


    fun saveImage(context: Context,id:Long,uri: Uri){

        val file= File(context.filesDir,id.toString())

        val bytes=context.contentResolver.openInputStream(uri)?.readBytes()!!

        file.writeBytes(bytes)
    }

    fun getImageUri(context: Context, id: Long):Uri {
        val file=File(context.filesDir,id.toString())
        return if (file.exists()) Uri.fromFile(file)
        else Uri.parse("android.resource://com.davidlopez.notestore10/drawable/ic_person.xml")
    }

}