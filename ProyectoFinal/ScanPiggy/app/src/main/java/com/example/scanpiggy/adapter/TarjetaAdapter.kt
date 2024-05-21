package com.example.scanpiggy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpiggy.R

class TarjetaAdapter(private val tarjetas: ArrayList<String>) :
    RecyclerView.Adapter<TarjetaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTarjeta: TextView = itemView.findViewById(R.id.textViewTarjeta)
        val textViewFecha: TextView = itemView.findViewById(R.id.textViewFechaVencimientoTarjeta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarjeta_credito, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tarjetaInfo = tarjetas[position].split(" - ")
        val cuentaTarjeta = tarjetaInfo[0]
        val fechaExpiracion = tarjetaInfo[1]

        holder.textViewTarjeta.text = cuentaTarjeta
        holder.textViewFecha.text = "$fechaExpiracion"
    }

    override fun getItemCount(): Int {
        return tarjetas.size
    }
}
