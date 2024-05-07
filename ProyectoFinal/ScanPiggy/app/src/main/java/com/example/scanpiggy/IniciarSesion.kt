package com.example.scanpiggy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide

class IniciarSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)

        val btnLogin = findViewById<Button>(R.id.btnLogin)

        val gifImageView = findViewById<ImageView>(R.id.gifImageView)
        Glide.with(this).load(R.drawable.huchaunscreen).into(gifImageView)

        val imageGmail = findViewById<ImageView>(R.id.imageGmail)
        imageGmail.setOnClickListener {
            val intent = packageManager.getLaunchIntentForPackage("com.google.android.gm")
            startActivity(intent)
        }

        val imageFacebook = findViewById<ImageView>(R.id.imageFacebook)
        imageFacebook.setOnClickListener {
            val intent = packageManager.getLaunchIntentForPackage("com.facebook.katana")
            startActivity(intent)
        }

        val imageTwitter = findViewById<ImageView>(R.id.imageTwitter)
        imageTwitter.setOnClickListener {
            val intent = packageManager.getLaunchIntentForPackage("com.twitter.android")
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}