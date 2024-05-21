package com.example.scanpiggy.anadir

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.scanpiggy.R
import com.google.android.material.imageview.ShapeableImageView

class AnadirCategoriaOtro : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageCategoriaOtro: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_categoria_otro)

        val toolbar = findViewById<Toolbar>(R.id.toolbarBack)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Referenciar la imagen en el layout
        imageCategoriaOtro = findViewById(R.id.imageCategoriaOtro)

        // Configurar el OnClickListener para la imagen
        imageCategoriaOtro.setOnClickListener {
            openGallery()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data
            imageCategoriaOtro.setImageURI(selectedImage)
        }
    }
}