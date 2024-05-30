package com.example.scanpiggy.anadir

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.scanpiggy.Gasto
import com.example.scanpiggy.GastosManager
import com.example.scanpiggy.Notas
import com.example.scanpiggy.R

class AnadirCategoriaOtro : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageCategoriaOtro: ImageView
    private lateinit var nombreCategoriaEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_categoria_otro)

        val toolbar = findViewById<Toolbar>(R.id.toolbarBack)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        imageCategoriaOtro = findViewById(R.id.imageCategoriaOtro)
        nombreCategoriaEditText = findViewById(R.id.textNombreCategoriaOtro)

        imageCategoriaOtro.setOnClickListener {
            openGallery()
        }

        val btnAnadirCategoriaOtro: Button = findViewById(R.id.btnAnadirCategoriaOtro)
        btnAnadirCategoriaOtro.setOnClickListener {
            val cantidadEditText = findViewById<EditText>(R.id.textCantidadOtro)
            val comentarioEditText = findViewById<EditText>(R.id.textComentarioOtro)

            val cantidad = cantidadEditText.text.toString()
            val comentario = comentarioEditText.text.toString()
            val nombreCategoria = nombreCategoriaEditText.text.toString()

            // Obtener el URI de la imagen seleccionada, o usar una imagen por defecto si no hay selección
            val imagenUriString = imageCategoriaOtro.tag as? String
            val imagenSeleccionada = if (!imagenUriString.isNullOrEmpty()) Uri.parse(imagenUriString) else null

            // Obtener el ID de la imagen seleccionada o usar una imagen por defecto si no hay selección
            val imagenId = if (imagenSeleccionada != null) {
                // Convertir el URI de la imagen a ID de recurso drawable
                getDrawableIdFromUri(imagenSeleccionada) ?: R.drawable.addphotoalternate
            } else {
                R.drawable.addphotoalternate
            }

            val nuevoGasto = Gasto(imagenId, nombreCategoria, cantidad, comentario)
            GastosManager.addGasto(nuevoGasto)

            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)

            val intent = Intent(this, Notas::class.java)
            startActivity(intent)

            finish()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data
            if (selectedImage != null) {
                // Asignar el URI de la imagen seleccionada a ImageView
                imageCategoriaOtro.setImageURI(selectedImage)
                // Guardar el URI de la imagen seleccionada en el tag del ImageView
                imageCategoriaOtro.tag = selectedImage.toString()
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Función para obtener el ID de recurso drawable desde el URI
    private fun getDrawableIdFromUri(uri: Uri): Int? {
        val context = applicationContext
        val resolver = context.contentResolver

        try {
            resolver.openInputStream(uri)?.use { inputStream ->
                // Decodificar el tamaño de la imagen para obtener el ID de recurso drawable
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(inputStream, null, options)
                options.inSampleSize = calculateInSampleSize(options, 100, 100) // Ajusta el tamaño si es necesario
                options.inJustDecodeBounds = false
                BitmapFactory.decodeStream(resolver.openInputStream(uri), null, options)

                // Convertir el URI directamente a un ID de recurso drawable
                return context.resources.getIdentifier(uri.toString(), null, context.packageName)
            } ?: return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    // Función para calcular el tamaño de muestra para la imagen
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
