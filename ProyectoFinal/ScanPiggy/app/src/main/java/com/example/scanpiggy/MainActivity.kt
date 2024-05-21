package com.example.scanpiggy

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
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
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        setupDrawer()
        setupPieChart()
        setupRecyclerView()

        val bottomNavView: BottomNavigationView = findViewById(R.id.nav_view)
        bottomNavView.selectedItemId = R.id.navigation_home
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

        // Mostrar el correo electrónico del usuario en el NavigationView
        val headerView = navViewLateral.getHeaderView(0)
        val emailTextView = headerView.findViewById<TextView>(R.id.nav_header_email)
        emailTextView.text = currentUser?.email

        val profileImageView = headerView.findViewById<ImageView>(R.id.imageViewFotoPerfil)
        profileImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startForResult.launch(intent)
        }

    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                uri?.let {
                    val headerView = findViewById<NavigationView>(R.id.nav_view_lateral).getHeaderView(0)
                    val profileImageView = headerView.findViewById<ImageView>(R.id.imageViewFotoPerfil)
                    Glide.with(this).load(uri).into(profileImageView)
                }
            }
        }

    private fun setupDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }


    private fun setupPieChart() {
        val pieChart: PieChart = findViewById(R.id.pie_chart)

        val entries = listOf(
            PieEntry(30f, "Entretenimiento"),
            PieEntry(20f, "Salud"),
            PieEntry(40f, "Hogar"),
            PieEntry(10f, "Educacion"),
        )

        val dataSet = PieDataSet(entries,"")
        dataSet.colors = listOf(Color.BLUE, Color.GREEN, Color.RED, Color.MAGENTA)

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
