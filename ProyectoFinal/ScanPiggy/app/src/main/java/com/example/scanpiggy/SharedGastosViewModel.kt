package com.example.scanpiggy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scanpiggy.Gasto

class SharedGastosViewModel : ViewModel() {

    private val _gastos = MutableLiveData<List<Gasto>>()
    val gastos: LiveData<List<Gasto>>
        get() = _gastos

    fun addGasto(gasto: Gasto) {
        val currentList = _gastos.value?.toMutableList() ?: mutableListOf()
        currentList.add(gasto)
        _gastos.value = currentList.toList()
    }
}
