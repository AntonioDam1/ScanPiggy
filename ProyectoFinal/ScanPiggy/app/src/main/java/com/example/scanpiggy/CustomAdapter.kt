package com.example.scanpiggy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CustomAdapter:RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    val arrayPresupuesto = arrayOf("Presupuesto de hoy: ",
        "Presupuesto de la semana: ",
        "Presupuesto del mes: ",
        "Presupuesto del año: ")

    val arrayFechas = arrayOf("Día de hoy: XX/XX/XXXX - XX/XX/XXXX",
        "Semana: XX/XX/XXXX - XX/XX/XXXX ",
        "Mes: XXXX ",
        "Año: XXXX ")


    val images = intArrayOf(R.drawable.euro,
        R.drawable.euro,
        R.drawable.euro,
        R.drawable.euro)
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.cards_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemImage.setImageResource(images[i])
        viewHolder.itemPresupuesto.text = arrayPresupuesto[i]
        viewHolder.itemFecha.text = arrayFechas[i]
        viewHolder.itemNumeroPresupuesto.editableText
    }

    override fun getItemCount(): Int {
        return arrayPresupuesto.size
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemPresupuesto: TextView
        var itemFecha: TextView
        var itemNumeroPresupuesto:  EditText

        init {
            itemImage = itemView.findViewById(R.id.image_divisa)
            itemPresupuesto = itemView.findViewById(R.id.text_presupuesto)
            itemFecha = itemView.findViewById(R.id.text_fecha)
            itemNumeroPresupuesto = itemView.findViewById(R.id.text_numeroPresupuesto)

        }
    }

}