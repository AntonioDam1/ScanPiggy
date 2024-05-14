package com.example.scanpiggy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpiggy.Gasto
import com.example.scanpiggy.R

class GastosAdapter : RecyclerView.Adapter<GastosAdapter.GastosViewHolder>() {

    private val gastosList = mutableListOf<Gasto>()

    inner class GastosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreCategoriaTextView: TextView = itemView.findViewById(R.id.textNombreCategoria)
        val imagenCategoriaImageView: ImageView = itemView.findViewById(R.id.imageCategoria)
        val cantidadTextView: EditText = itemView.findViewById(R.id.editCantidad)
        val comentarioTextView: EditText = itemView.findViewById(R.id.editComentario)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_anadir_categorias, parent, false)
        return GastosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GastosViewHolder, position: Int) {
        // Obtener el gasto en la posición 'position'
        val gasto = gastosList[position]

        // Establecer los datos del gasto en los elementos de la vista del elemento de la lista
        holder.nombreCategoriaTextView.text = gasto.nombreCategoria
        holder.imagenCategoriaImageView.setImageResource(gasto.imagenCategoria)
        holder.cantidadTextView.text = gasto.cantidad.text
        holder.comentarioTextView.text = gasto.comentario.text
    }


    override fun getItemCount(): Int {
        return gastosList.size
    }

    // Método para actualizar la lista de gastos
    fun updateGastosList(newGastosList: List<Gasto>) {
        gastosList.clear()
        gastosList.addAll(newGastosList)
        notifyDataSetChanged()
    }
}

