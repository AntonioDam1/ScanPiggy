package com.example.scanpiggy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.GridView
import com.example.scanpiggy.adapter.CategoriasAdapter

class CategoriaGastoActivity : AppCompatActivity() {
    private val imagenesCategorias = intArrayOf(
        R.drawable.home,
        R.drawable.entertainment,
        R.drawable.viajes,
        R.drawable.salud,
        R.drawable.educacion,
        R.drawable.otros
    )

    private val nombresCategorias = arrayOf(
        "Hogar",
        "Entretenimiento",
        "Viajes",
        "Salud",
        "Educacion",
        "Otros"
    )

    val coloresCategorias = intArrayOf(
        android.R.color.holo_red_light,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light,
        android.R.color.holo_red_light,
        android.R.color.holo_red_light
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria_gasto)

        // Configurar la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configurar el GridView
        val gridView: GridView = findViewById(R.id.GridViewGategoriasGastos)
        val categoriasAdapter = CategoriasAdapter(this, imagenesCategorias, nombresCategorias,coloresCategorias)
        gridView.adapter = categoriasAdapter

        gridView.setOnItemClickListener { parent, view, position, id ->
            val categoriaSeleccionada = nombresCategorias[position]
            val imagenCategoriaSeleccionada = imagenesCategorias[position]

            val intent = Intent()
            intent.putExtra("nombreCategoria", categoriaSeleccionada)
            intent.putExtra("imagenCategoria", imagenCategoriaSeleccionada)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }
}
