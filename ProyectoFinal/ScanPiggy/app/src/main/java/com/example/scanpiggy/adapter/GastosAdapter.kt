package com.example.scanpiggy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpiggy.Gasto
import com.example.scanpiggy.R

class GastosAdapter : ListAdapter<Gasto, GastosAdapter.GastoViewHolder>(GastoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_anadir_categorias, parent, false)
        return GastoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GastoViewHolder, position: Int) {
        val gasto = getItem(position)
        holder.bind(gasto)
    }

    class GastoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageCategoria)
        private val nombreTextView: TextView = itemView.findViewById(R.id.textNombreCategoria)
        private val cantidadTextView: TextView = itemView.findViewById(R.id.textCantidad)
        private val comentarioTextView: TextView = itemView.findViewById(R.id.textComentario)

        fun bind(gasto: Gasto) {
            imageView.setImageResource(gasto.imagenCategoria)
            nombreTextView.text = gasto.nombreCategoria
            cantidadTextView.text = gasto.cantidad
            comentarioTextView.text = gasto.comentario
        }
    }

    class GastoDiffCallback : DiffUtil.ItemCallback<Gasto>() {
        override fun areItemsTheSame(oldItem: Gasto, newItem: Gasto): Boolean {
            return oldItem.nombreCategoria == newItem.nombreCategoria
        }

        override fun areContentsTheSame(oldItem: Gasto, newItem: Gasto): Boolean {
            return oldItem == newItem
        }
    }
}



