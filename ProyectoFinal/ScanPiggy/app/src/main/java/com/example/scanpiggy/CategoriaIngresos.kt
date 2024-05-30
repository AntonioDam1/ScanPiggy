package com.example.scanpiggy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.GridView
import androidx.appcompat.widget.Toolbar
import com.example.scanpiggy.adapter.CategoriasAdapter
import com.example.scanpiggy.adapter.CategoriasIngresosAdapter

class CategoriaIngresos : AppCompatActivity() {
    private val imagenesCategoriasIngresos = intArrayOf(
        R.drawable.work,
        R.drawable.ventas,
        R.drawable.invertir,
        R.drawable.redes_sociales,
        R.drawable.subvenciones,
        R.drawable.otros
    )

    private val nombresCategoriasIngresos = arrayOf(
        "Trabajo",
        "Ventas",
        "Inversiones",
        "Redes Sociales",
        "Subvenciones",
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
        setContentView(R.layout.activity_categoria_ingresos)


        // Configurar la Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbarBack)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Configurar el GridView
        val gridView: GridView = findViewById(R.id.GridViewGategoriasIngresos)
        val categoriasAdapter = CategoriasIngresosAdapter(this, imagenesCategoriasIngresos, nombresCategoriasIngresos, coloresCategorias)
        gridView.setAdapter(categoriasAdapter)

        gridView.setOnItemClickListener { parent, view, position, id ->
            val categoriaSeleccionada = nombresCategoriasIngresos[position]
            val imagenCategoriaSeleccionada = imagenesCategoriasIngresos[position]

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