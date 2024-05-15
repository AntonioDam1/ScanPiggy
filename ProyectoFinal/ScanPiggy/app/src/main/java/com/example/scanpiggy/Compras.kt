package com.example.scanpiggy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.scanpiggy.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class Compras : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_compras)
        setSupportActionBar(binding.toolbar)


        val bottomNavView: BottomNavigationView = findViewById(R.id.nav_view_compras)
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }
}