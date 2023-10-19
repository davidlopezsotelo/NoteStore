package com.davidlopez.notestore10.UI.Contactos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davidlopez.notestore10.R
import com.davidlopez.notestore10.databinding.ActivityAboutBinding
import com.davidlopez.notestore10.databinding.ActivityContactDetailBinding

class ContactDetailActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityContactDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

    }
}