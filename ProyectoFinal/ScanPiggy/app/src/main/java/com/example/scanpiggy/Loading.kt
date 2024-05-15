package com.example.scanpiggy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide

class Loading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val gifImageView = findViewById<ImageView>(R.id.imageViewBackground)
        Glide.with(this).load(R.drawable.huchaunscreen).into(gifImageView)
        val btnWelcome = findViewById<Button>(R.id.btnWelcome)

        btnWelcome.setOnClickListener{
            startActivity(Intent(this, IniciarSesion::class.java))
        }
    }
}