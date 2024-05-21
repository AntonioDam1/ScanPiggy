package com.example.scanpiggy

object GastosManager {
    private val gastos = mutableListOf<Gasto>()

    fun addGasto(gasto: Gasto) {
        gastos.add(gasto)
    }

    fun getGastos(): List<Gasto> {
        return gastos
    }
}
