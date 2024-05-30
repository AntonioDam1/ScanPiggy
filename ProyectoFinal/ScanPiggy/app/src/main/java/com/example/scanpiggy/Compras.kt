package com.example.scanpiggy

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.scanpiggy.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Compras : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_compras)
        setSupportActionBar(binding.toolbar)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        setupDrawer()


        val bottomNavView: BottomNavigationView = findViewById(R.id.nav_view_compras)
        bottomNavView.selectedItemId = R.id.navigation_compras
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_lista -> {
                    val intent = Intent(this, Notas::class.java)
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
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_compras)
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
            // Otros casos de ítems del menú
        }
        // Cerrar el drawer después de manejar el clic del usuario
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}