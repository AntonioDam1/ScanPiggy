package com.example.scanpiggy.anadir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.scanpiggy.Gasto
import com.example.scanpiggy.GastosManager
import com.example.scanpiggy.Notas
import com.example.scanpiggy.R

class AnadirCategoria : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_categoria)

        val nombreCategoria = intent.getStringExtra("nombreCategoria")
        val imagenCategoria = intent.getIntExtra("imagenCategoria", R.drawable.error) // Puedes proporcionar una imagen predeterminada si no se encuentra la imagen

        // Aquí puedes configurar la imagen y el nombre de la categoría en tus elementos de la interfaz de usuario
        val imageView = findViewById<ImageView>(R.id.imageCategoria)
        val textView = findViewById<TextView>(R.id.textNombreCategoria)

        imageView.setImageResource(imagenCategoria)
        textView.text = nombreCategoria

        // Suponiendo que tienes un botón en tu diseño XML con el id btnAñadirNota
        val btnAnadirCategoria: Button = findViewById(R.id.btnAnadirCategoria)

        btnAnadirCategoria.setOnClickListener {
            val intent = Intent(this, Notas::class.java)
            startActivity(intent)
        }

        val cantidadEditText = findViewById<EditText>(R.id.editCantidad)
        val comentarioEditText = findViewById<EditText>(R.id.editComentario)

        val nuevoGasto = Gasto(imagenCategoria, nombreCategoria.toString(), cantidadEditText, comentarioEditText)
        GastosManager.addGasto(nuevoGasto)



    }
}