package com.example.scanpiggy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.scanpiggy.anadir.AnadirCategoria
import com.example.scanpiggy.anadir.AnadirCategoriaOtro
import com.example.scanpiggy.R

class CategoriasAdapter(private val context: Context, private val imagenes: IntArray, private val nombres: Array<String>, private val colores: IntArray) : BaseAdapter() {

    override fun getCount(): Int {
        return imagenes.size
    }

    override fun getItem(position: Int): Any {
        return imagenes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_categorias, parent, false)
        val imageView = view.findViewById<ImageView>(R.id.imageCategoria)
        val textView = view.findViewById<TextView>(R.id.textCategoria)

        imageView.setImageResource(imagenes[position])
        textView.text = nombres[position]
        imageView.background = context.resources.getDrawable(R.drawable.circle_background)
        imageView.setBackgroundColor(colores[position % colores.size]) // Utiliza el módulo para ciclar a través de los colores

        // Establecer el listener de clic para cada elemento del GridView
        view.setOnClickListener {
            val categoriaSeleccionada = nombres[position] // Nombre de la categoría seleccionada
            val imagenCategoriaSeleccionada = imagenes[position] // ID de la imagen de la categoría seleccionada

            if (nombres[position] == "Otros") {
                val intent = Intent(context, AnadirCategoriaOtro::class.java)
                context.startActivity(intent)
            }else{
                // Crear un intent para abrir AnadirCategoriaActivity y pasar los datos de la categoría seleccionada
                val intent = Intent(context, AnadirCategoria::class.java)
                intent.putExtra("nombreCategoria", categoriaSeleccionada)
                intent.putExtra("imagenCategoria", imagenCategoriaSeleccionada)
                context.startActivity(intent)
            }

        }

        return view
    }
}
