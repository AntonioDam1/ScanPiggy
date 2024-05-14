package com.example.scanpiggy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.scanpiggy.anadir.AddBilletera

class Billetera : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billetera)

        val btn_add_card = findViewById<Button>(R.id.btn_add_card)
        btn_add_card.setOnClickListener {
            // Crear un intent para abrir la actividad AddCardActivity
            val intent = Intent(this, AddBilletera::class.java)
            // Iniciar la actividad
            startActivity(intent)
        }
    }
}