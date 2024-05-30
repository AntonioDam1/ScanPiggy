package com.example.scanpiggy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.GridView
import com.example.scanpiggy.adapter.CategoriasAdapter

class CategoriaGastoActivity : AppCompatActivity() {
    private val imagenesCategorias = intArrayOf(
        R.drawable.house,
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

    private val coloresCategorias = intArrayOf(
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
        val toolbar = findViewById<Toolbar>(R.id.toolbarBack)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Configurar el GridView
        val gridView: GridView = findViewById(R.id.GridViewGategoriasGastos)
        val categoriasAdapter = CategoriasAdapter(this, imagenesCategorias, nombresCategorias, coloresCategorias)
        gridView.setAdapter(categoriasAdapter)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
