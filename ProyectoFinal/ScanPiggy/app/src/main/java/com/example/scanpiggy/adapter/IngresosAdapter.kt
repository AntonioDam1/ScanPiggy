package com.example.scanpiggy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpiggy.Ingresos
import com.example.scanpiggy.R

class IngresosAdapter : ListAdapter<Ingresos, IngresosAdapter.IngresoViewHolder>(IngresoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngresoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_anadir_categorias, parent, false)
        return IngresoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngresoViewHolder, position: Int) {
        val ingreso = getItem(position)
        holder.bind(ingreso)
    }

    class IngresoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageCategoria)
        private val nombreTextView: TextView = itemView.findViewById(R.id.textNombreCategoria)
        private val cantidadTextView: TextView = itemView.findViewById(R.id.textCantidad)
        private val comentarioTextView: TextView = itemView.findViewById(R.id.textComentario)

        fun bind(ingreso: Ingresos) {
            imageView.setImageResource(ingreso.imagenCategoria)
            nombreTextView.text = ingreso.nombreCategoria
            cantidadTextView.text = ingreso.cantidad
            comentarioTextView.text = ingreso.comentario
        }
    }

    class IngresoDiffCallback : DiffUtil.ItemCallback<Ingresos>() {
        override fun areItemsTheSame(oldItem: Ingresos, newItem: Ingresos): Boolean {
            return oldItem.nombreCategoria == newItem.nombreCategoria
        }

        override fun areContentsTheSame(oldItem: Ingresos, newItem: Ingresos): Boolean {
            return oldItem == newItem
        }
    }
}