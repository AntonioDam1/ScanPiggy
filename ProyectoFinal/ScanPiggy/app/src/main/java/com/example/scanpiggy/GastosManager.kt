package com.example.scanpiggy

object GastosManager {
    private val gastosList = mutableListOf<Gasto>()
    private var listener: GastosUpdateListener? = null

    fun setListener(listener: GastosUpdateListener) {
        this.listener = listener
    }

    fun addGasto(gasto: Gasto) {
        gastosList.add(gasto)
        listener?.onGastosUpdated()
    }

    fun getGastos(): List<Gasto> {
        return gastosList
    }
}

interface GastosUpdateListener {
    fun onGastosUpdated()
}
