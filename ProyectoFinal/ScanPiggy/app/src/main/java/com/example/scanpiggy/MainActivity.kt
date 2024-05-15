package com.example.scanpiggy

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpiggy.adapter.CustomAdapter
import com.example.scanpiggy.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupPieChart()
        setupRecyclerView()

        val bottomNavView: BottomNavigationView = findViewById(R.id.nav_view)
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_lista -> {
                    val intent = Intent(this, Notas::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_compras -> {
                    val intent = Intent(this, Compras::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        val navViewLateral: NavigationView = findViewById(R.id.nav_view_lateral)
        navViewLateral.setNavigationItemSelectedListener(this)

    }


    private fun setupPieChart() {
        val pieChart: PieChart = findViewById(R.id.pie_chart)

        val entries = listOf(
            PieEntry(30f, "Grupo A"),
            PieEntry(20f, "Grupo B"),
            PieEntry(50f, "Grupo C")
        )

        val dataSet = PieDataSet(entries, "Datos de ejemplo")
        dataSet.colors = listOf(Color.BLUE, Color.GREEN, Color.RED)

        val pieData = PieData(dataSet)
        pieChart.data = pieData

        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.transparentCircleRadius = 30f
        pieChart.animateY(1000)
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_main)
        val adapter = CustomAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_main -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_billetera -> {
                val intent = Intent(this, Billetera::class.java)
                startActivity(intent)
                return true
            }
            // Otros casos de ítems del menú
        }
        // Cerrar el drawer después de manejar el clic del usuario
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
