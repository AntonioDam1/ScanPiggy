package com.example.scanpiggy.adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpiggy.R
import com.example.scanpiggy.model.PresupuestoItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CustomAdapter(
    private val presupuestos: List<PresupuestoItem>
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cards_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val presupuestoItem = presupuestos[position]

        holder.itemImage.setImageResource(presupuestoItem.imageResource)
        holder.itemPresupuesto.text = presupuestoItem.tipoPresupuesto
        holder.itemFecha.text = presupuestoItem.fecha
        holder.itemNumeroPresupuesto.setText(presupuestoItem.valor.toString())

        holder.itemNumeroPresupuesto.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val nuevoPresupuesto = holder.itemNumeroPresupuesto.text.toString().toDoubleOrNull() ?: 0.0
                updatePresupuesto(presupuestoItem, nuevoPresupuesto)
            }
        }
    }

    override fun getItemCount(): Int {
        return presupuestos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.image_divisa)
        var itemPresupuesto: TextView = itemView.findViewById(R.id.text_presupuesto)
        var itemFecha: TextView = itemView.findViewById(R.id.text_fecha)
        var itemNumeroPresupuesto: EditText = itemView.findViewById(R.id.text_numeroPresupuesto)

        init {
            // Agregar listener para guardar el presupuesto cuando se presiona Enter
            itemNumeroPresupuesto.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val nuevoPresupuesto = itemNumeroPresupuesto.text.toString().toDoubleOrNull() ?: 0.0
                    updatePresupuesto(presupuestos[adapterPosition], nuevoPresupuesto)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun updatePresupuesto(presupuestoItem: PresupuestoItem, nuevoValor: Double) {
        presupuestoItem.valor = nuevoValor

        val user = auth.currentUser
        user?.let { currentUser ->
            val presupuestoRef = firestore.collection("usuarios").document(currentUser.uid)
                .collection("presupuestos").document(presupuestoItem.id)

            // Actualizar en Firestore
            presupuestoRef.update("valor", nuevoValor)
                .addOnSuccessListener {
                    Log.d(TAG, "Presupuesto actualizado correctamente en Firestore")
                    notifyDataSetChanged() // Actualizar RecyclerView si es necesario
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error al actualizar presupuesto en Firestore", e)
                    // Manejar error o revertir cambios locales si es necesario
                    presupuestoItem.valor = presupuestoItem.valor // Revertir cambios locales si es necesario
                }
        }
    }


}
