package com.example.scanpiggy

import android.widget.EditText

data class Gasto(
    val imagenCategoria: Int,
    val nombreCategoria: String,
    val cantidad: EditText,
    val comentario: EditText
)
