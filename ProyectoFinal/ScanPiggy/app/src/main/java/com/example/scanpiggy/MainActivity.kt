package com.example.scanpiggy

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpiggy.CustomAdapter
import com.example.scanpiggy.R
import com.example.scanpiggy.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieData

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupPieChart()
        setupRecyclerView()
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
}
