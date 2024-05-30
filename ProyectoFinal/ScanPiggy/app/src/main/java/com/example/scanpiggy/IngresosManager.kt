package com.example.scanpiggy

object IngresosManager {
    private val ingresos = mutableListOf<Ingresos>()

    fun addIngresos(ingreso: Ingresos) {
        ingresos.add(ingreso)
    }

    fun getIngresos(): List<Ingresos> {
        return ingresos
    }
}