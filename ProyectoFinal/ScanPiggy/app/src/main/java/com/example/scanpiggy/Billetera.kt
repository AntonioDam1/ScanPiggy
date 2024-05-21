package com.example.scanpiggy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpiggy.adapter.TarjetaAdapter
import com.example.scanpiggy.anadir.AddBilletera

class Billetera : AppCompatActivity() {
    private val tarjetas: ArrayList<String> = ArrayList()
    private val ADD_TARJETA_REQUEST_CODE = 1
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billetera)

        val toolbar = findViewById<Toolbar>(R.id.toolbarBack)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)



        recyclerView = findViewById(R.id.recyclerViewTajetasDeCredito)

        val btn_add_card = findViewById<Button>(R.id.btn_add_card)
        btn_add_card.setOnClickListener {
            val intent = Intent(this, AddBilletera::class.java)
            startActivityForResult(intent, ADD_TARJETA_REQUEST_CODE)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TarjetaAdapter(tarjetas)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TARJETA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val tarjeta = data?.getStringExtra("tarjeta")
            val fecha = data?.getStringExtra("fecha")
            tarjeta?.let {
                fecha?.let {
                    val nuevaTarjeta = "$tarjeta - $fecha"
                    tarjetas.add(nuevaTarjeta)
                    recyclerView.adapter?.notifyItemInserted(tarjetas.size - 1)
                }
            }
        }
    }
}

