package com.example.scanpiggy.model

import com.google.firebase.firestore.PropertyName

data class PresupuestoItem(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("tipoPresupuesto") @set:PropertyName("tipoPresupuesto") var tipoPresupuesto: String = "",
    @get:PropertyName("imageResource") @set:PropertyName("imageResource") var imageResource: Int = 0,
    @get:PropertyName("fecha") @set:PropertyName("fecha") var fecha: String = "",
    @get:PropertyName("valor") @set:PropertyName("valor") var valor: Double = 0.0
) {
    // Constructor vacío requerido por Firestore
    constructor() : this("", "", 0, "", 0.0)
}

