package com.example.scanpiggy.anadir

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.scanpiggy.R
import com.google.android.material.imageview.ShapeableImageView

class AnadirCategoriaOtro : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageCategoriaOtro: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_categoria_otro)

        // Referenciar la imagen en el layout
        imageCategoriaOtro = findViewById(R.id.imageCategoriaOtro)

        // Configurar el OnClickListener para la imagen
        imageCategoriaOtro.setOnClickListener {
            openGallery()
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
            imageCategoriaOtro.setImageURI(selectedImage)
        }
    }
}