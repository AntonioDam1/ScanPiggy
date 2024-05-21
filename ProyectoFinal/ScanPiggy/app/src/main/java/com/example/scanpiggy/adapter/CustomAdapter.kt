package com.example.scanpiggy.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpiggy.R
import java.text.SimpleDateFormat
import java.util.*

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    val arrayPresupuesto = arrayOf(
        "Presupuesto de hoy: ",
        "Presupuesto de la semana: ",
        "Presupuesto del mes: ",
        "Presupuesto del año: "
    )

    val images = intArrayOf(
        R.drawable.euro,
        R.drawable.euro,
        R.drawable.euro,
        R.drawable.euro
    )

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.cards_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemImage.setImageResource(images[i])
        viewHolder.itemPresupuesto.text = arrayPresupuesto[i]
        viewHolder.itemFecha.text = getDateRange(i)
        viewHolder.itemNumeroPresupuesto.editableText
    }

    override fun getItemCount(): Int {
        return arrayPresupuesto.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView = itemView.findViewById(R.id.image_divisa)
        var itemPresupuesto: TextView = itemView.findViewById(R.id.text_presupuesto)
        var itemFecha: TextView = itemView.findViewById(R.id.text_fecha)
        var itemNumeroPresupuesto: EditText = itemView.findViewById(R.id.text_numeroPresupuesto)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateRange(type: Int): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")

        return when (type) {
            0 -> {
                // Día de hoy
                val today = calendar.time
                "Día: ${dateFormat.format(today)}"
            }
            1 -> {
                // Semana: del día de hoy a 7 días después
                val startOfWeek = calendar.time
                calendar.add(Calendar.DAY_OF_YEAR, 7)
                val endOfWeek = calendar.time
                "Semana: ${dateFormat.format(startOfWeek)} - ${dateFormat.format(endOfWeek)}"
            }
            2 -> {
                // Mes: el mes actual
                val currentMonth = SimpleDateFormat("MMMM yyyy").format(calendar.time)
                "Mes: $currentMonth"
            }
            3 -> {
                // Año: el año actual
                val currentYear = SimpleDateFormat("yyyy").format(calendar.time)
                "Año: $currentYear"
            }
            else -> "Fecha desconocida"
        }
    }
}
