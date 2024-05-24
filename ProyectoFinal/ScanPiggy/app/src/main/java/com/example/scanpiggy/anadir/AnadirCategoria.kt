package com.example.scanpiggy.anadir

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.scanpiggy.CategoriaGastoActivity
import com.example.scanpiggy.Gasto
import com.example.scanpiggy.GastosManager
import com.example.scanpiggy.R

class AnadirCategoria : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_categoria)

        val toolbar = findViewById<Toolbar>(R.id.toolbarBack)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val imagenCategoria = intent.getIntExtra("imagenCategoria", R.drawable.addphotoalternate)
        val nombreCategoria = intent.getStringExtra("nombreCategoria")

        val btnAnadirCategoria: Button = findViewById(R.id.btnAnadirCategoria)
        btnAnadirCategoria.setOnClickListener {
            val cantidadEditText = findViewById<EditText>(R.id.editCantidad)
            val comentarioEditText = findViewById<EditText>(R.id.editComentario)

            val cantidad = cantidadEditText.text.toString()
            val comentario = comentarioEditText.text.toString()

            // Crear el objeto de gasto y añadirlo a la lista de gastos
            val nuevoGasto = Gasto(imagenCategoria, nombreCategoria ?: "", cantidad, comentario)
            GastosManager.addGasto(nuevoGasto)

            // Devolver el resultado a GastosFragment
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        val addPhotoAlternateIcon: ImageView = findViewById(R.id.imageCategoria)
        addPhotoAlternateIcon.setOnClickListener {
            val intent = Intent(this, CategoriaGastoActivity::class.java)
            startActivityForResult(intent, REQUEST_SELECT_CATEGORY)
        }
    }

    companion object {
        private const val REQUEST_SELECT_CATEGORY = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_CATEGORY) {
            val imagenCategoria = data?.getIntExtra("imagenCategoria", R.drawable.addphotoalternate)
            val nombreCategoria = data?.getStringExtra("nombreCategoria")

            // Actualizar la imagen y el nombre en la actividad AnadirCategoria
            val imageCategoriaView = findViewById<ImageView>(R.id.imageCategoria)
            val nombreCategoriaView = findViewById<TextView>(R.id.textNombreCategoria)

            imagenCategoria?.let { imageCategoriaView.setImageResource(it) }
            nombreCategoria?.let { nombreCategoriaView.text = it }
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
