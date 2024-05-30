package com.example.scanpiggy

import android.annotation.SuppressLint
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
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.scanpiggy.model.PresupuestoItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.Date
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentUserEmail: String? = null
    private var currentUser: FirebaseUser? = null

    private lateinit var adapter: CustomAdapter
    private lateinit var presupuestos: MutableList<PresupuestoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        currentUserEmail = auth.currentUser?.email

        presupuestos = mutableListOf(
            PresupuestoItem("1", "Presupuesto de hoy", R.drawable.euro, getDateRange(0), 0.0),
            PresupuestoItem("2", "Presupuesto de la semana", R.drawable.euro, getDateRange(1), 0.0),
            PresupuestoItem("3", "Presupuesto del mes", R.drawable.euro, getDateRange(2), 0.0),
            PresupuestoItem("4", "Presupuesto del año", R.drawable.euro, getDateRange(3), 0.0)
        )

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

        // Ejemplo de cómo guardar presupuestos
        saveBudgets()
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
                val currentMonth = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
                "Mes: $currentMonth"
            }
            3 -> {
                // Año: el año actual
                val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.time)
                "Año: $currentYear"
            }
            else -> "Fecha desconocida"
        }
    }



    private fun setupPieChart() {
        val pieChart: PieChart = findViewById(R.id.pie_chart)

        val entries = listOf(
            PieEntry(30f, "entertainment", "entertainment"),  // Etiqueta "entertainment" para el primer segmento
            PieEntry(20f, "education", "education"),     // Etiqueta "education" para el segundo segmento
            PieEntry(50f, "health", "health")         // Etiqueta "health" para el tercer segmento
        )

        val iconResources = mapOf(
            "entertainment" to R.drawable.entertainment,
            "education" to R.drawable.educacion,
            "health" to R.drawable.salud
        )

        val colors = mapOf(
            "entertainment" to ContextCompat.getColor(this, R.color.colorAccent),
            "education" to ContextCompat.getColor(this, R.color.colorPressed),
            "health" to ContextCompat.getColor(this, R.color.purple_200)
        )



        val icons = entries.mapNotNull { entry ->
            val category = entry.data as? String ?: return@mapNotNull null
            val resId = iconResources[category] ?: return@mapNotNull null

            try {
                val drawable = AppCompatResources.getDrawable(this, resId)
                drawable?.let {
                    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)

                    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true)
                    bitmap.recycle()
                    resizedBitmap
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        if (icons.size != entries.size) {
            throw IllegalArgumentException("Failed to load or resize one or more icons")
        }

        val dataSet = PieDataSet(entries, "")
        val colorsList = entries.map { entry ->
            val category = entry.data as? String ?: ""
            colors[category] ?: ContextCompat.getColor(this, android.R.color.darker_gray)
        }
        dataSet.colors = colorsList

        val pieData = PieData(dataSet)
        pieChart.data = pieData

        // Desactivar la descripción y la leyenda
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = true

        // Habilitar las etiquetas de los segmentos (valores)
        dataSet.setDrawValues(true)
        dataSet.setValueTextColor(Color.BLACK)
        dataSet.setValueTextSize(12f)

        // Configurar la posición de las etiquetas
        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.5f

        // Resto del código para cargar y redimensionar iconos (como ya tienes implementado)

        pieChart.renderer = CustomPieChartRenderer(pieChart, pieChart.animator, pieChart.viewPortHandler, icons)

        pieChart.invalidate() // Refresh the chart
    }



    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_main)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = CustomAdapter(presupuestos)
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

    private fun closeApp() {
        // Cerrar todas las actividades abiertas
        finishAffinity()

        // Salir del proceso de la aplicación
        System.exit(0)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_billetera -> {
                val intent = Intent(this, Billetera::class.java)
                startActivity(intent)
                return true
            }
            R.id.exit -> {
                // Cerrar la aplicación
                closeApp()
                return true
            }
            R.id.consejos -> {
                val intent = Intent(this, SplashScreen::class.java)
                startActivity(intent)
                return true
            }
        }
        // Cerrar el drawer después de manejar el clic del usuario
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun saveBudgets() {
        val userId = auth.currentUser?.uid ?: return
        val budgetsRef = db.collection("usuarios").document(userId).collection("presupuestos")

        // Ejemplo de presupuestos para guardar
        val budgets = listOf(
            BudgetEntry(0.0, "2024-05-30", "diario"),
            BudgetEntry(0.0, "2024-05-28", "semanal"),
            BudgetEntry(0.0, "", "mensual"), // Ejemplo con fecha vacía
            BudgetEntry(0.0, "", "anual")    // Ejemplo con fecha vacía
        )

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        budgets.forEachIndexed { index, budget ->
            val data = hashMapOf(
                "email" to currentUserEmail,
                "monto" to budget.amount,
                "periodo" to budget.periodo,
                "fecha" to if (budget.fecha.isNotEmpty()) budget.fecha else dateFormat.format(java.util.Date())
            )

            budgetsRef.add(data)
                .addOnSuccessListener { documentReference ->
                    // Éxito al guardar
                }
                .addOnFailureListener { e ->
                    // Error al guardar
                }
        }
    }
}

data class BudgetEntry(val amount: Double, val fecha: String, val periodo: String)

