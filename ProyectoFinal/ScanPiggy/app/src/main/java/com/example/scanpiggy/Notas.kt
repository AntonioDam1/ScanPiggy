package com.example.scanpiggy

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.scanpiggy.adapter.DiaViewPagerAdapter
import com.example.scanpiggy.adapter.GastosAdapter
import com.example.scanpiggy.adapter.IngresosAdapter
import com.example.scanpiggy.adapter.ViewPagerAdapter
import com.example.scanpiggy.anadir.AnadirCategoria
import com.example.scanpiggy.anadir.AnadirCategoriaIngresos
import com.example.scanpiggy.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Notas : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    private var currentUserEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_notas)
        setSupportActionBar(binding.toolbar)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        currentUserEmail = auth.currentUser?.email

        setupDrawer()


        val bottomNavView: BottomNavigationView = findViewById(R.id.nav_view)
        bottomNavView.selectedItemId = R.id.navigation_lista
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
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



        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        val viewPager: ViewPager = findViewById(R.id.view_pager)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DiaFragment(), "Día")
        adapter.addFragment(SemanaFragment(), "Semana")
        adapter.addFragment(MesFragment(), "Mes")
        adapter.addFragment(AnioFragment(), "Año")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)


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
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_notas)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_main -> {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_notas)
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
            // Otros casos de ítems del menú
        }
        // Cerrar el drawer después de manejar el clic del usuario
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    class DiaFragment : Fragment() {
        private lateinit var viewPagerDia: ViewPager
        private lateinit var tabLayoutDia: TabLayout

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_dia, container, false)
            viewPagerDia = rootView.findViewById(R.id.viewPagerDia)
            tabLayoutDia = rootView.findViewById(R.id.tab_layout_gastosIngresos_dia)
            setupViewPager()
            return rootView
        }

        private fun setupViewPager() {
            val adapter = DiaViewPagerAdapter(childFragmentManager)
            adapter.addFragment(GastosFragment(), "Gastos")
            adapter.addFragment(IngresosFragment(), "Ingresos")
            viewPagerDia.adapter = adapter
            tabLayoutDia.setupWithViewPager(viewPagerDia)
        }

        class GastosFragment : Fragment() {

            private lateinit var recyclerViewGastos: RecyclerView
            private lateinit var textoSinCategorias: TextView
            private val gastosAdapter = GastosAdapter()
            private val REQUEST_CODE_ADD_CATEGORY = 1

            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                val rootView = inflater.inflate(R.layout.fragment_gastos, container, false)

                recyclerViewGastos = rootView.findViewById(R.id.recyclerViewGastos)
                textoSinCategorias = rootView.findViewById(R.id.textoSinCategorias)
                val floatingButton: FloatingActionButton = rootView.findViewById(R.id.floatingAnadirGastos)

                floatingButton.setOnClickListener {
                    val intent = Intent(requireContext(), AnadirCategoria::class.java)
                    startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY)
                }

                return rootView
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                recyclerViewGastos.layoutManager = LinearLayoutManager(requireContext())
                recyclerViewGastos.adapter = gastosAdapter
                updateUI()
            }

            private fun updateUI() {
                val gastos = GastosManager.getGastos()
                if (gastos.isEmpty()) {
                    textoSinCategorias.visibility = View.VISIBLE
                    recyclerViewGastos.visibility = View.GONE
                } else {
                    textoSinCategorias.visibility = View.GONE
                    recyclerViewGastos.visibility = View.VISIBLE
                    gastosAdapter.submitList(gastos)
                }
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (requestCode == REQUEST_CODE_ADD_CATEGORY && resultCode == Activity.RESULT_OK) {
                    val imagenId = data?.getIntExtra("imagenId", R.drawable.addphotoalternate)
                    val nombreCategoria = data?.getStringExtra("nombreCategoria")
                    val cantidad = data?.getStringExtra("cantidad")
                    val comentario = data?.getStringExtra("comentario")

                    // Crea un nuevo Gasto y agrega a la lista
                    if (imagenId != null && nombreCategoria != null && cantidad != null && comentario != null) {
                        val nuevoGasto = Gasto(imagenId, nombreCategoria, cantidad, comentario)
                        GastosManager.addGasto(nuevoGasto)
                        updateUI()
                    }
                }
            }

        }

        class IngresosFragment : Fragment() {
            private lateinit var recyclerViewIngresos :  RecyclerView
            private lateinit var textoSinCategorias: TextView
            private val ingresosAdapter = IngresosAdapter()
            private val REQUEST_CODE_ADD_CATEGORY = 1
            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                val rootView = inflater.inflate(R.layout.fragment_ingresos, container, false)

                recyclerViewIngresos = rootView.findViewById(R.id.recyclerViewIngresos)
                textoSinCategorias = rootView.findViewById(R.id.textoSinCategoriasIngresos)
                val floatingButton: FloatingActionButton = rootView.findViewById(R.id.floatingAnadirIngresos)

                floatingButton.setOnClickListener {
                    val intent = Intent(requireContext(), AnadirCategoriaIngresos::class.java)
                    startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY)
                }

                return rootView
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                recyclerViewIngresos.layoutManager = LinearLayoutManager(requireContext())
                recyclerViewIngresos.adapter = ingresosAdapter
                updateUI()
            }

            private fun updateUI() {
                val ingresos = IngresosManager.getIngresos()
                if (ingresos.isEmpty()) {
                    textoSinCategorias.visibility = View.VISIBLE
                    recyclerViewIngresos.visibility = View.GONE
                } else {
                    textoSinCategorias.visibility = View.GONE
                    recyclerViewIngresos.visibility = View.VISIBLE
                    ingresosAdapter.submitList(ingresos)
                }
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (requestCode == REQUEST_CODE_ADD_CATEGORY && resultCode == Activity.RESULT_OK) {
                    val imagenId = data?.getIntExtra("imagenId", R.drawable.addphotoalternate)
                    val nombreCategoria = data?.getStringExtra("nombreCategoria")
                    val cantidad = data?.getStringExtra("cantidad")
                    val comentario = data?.getStringExtra("comentario")

                    // Crea un nuevo Gasto y agrega a la lista
                    if (imagenId != null && nombreCategoria != null && cantidad != null && comentario != null) {
                        val nuevoIngresos = Ingresos(imagenId, nombreCategoria, cantidad, comentario)
                        IngresosManager.addIngresos(nuevoIngresos)
                        updateUI()
                    }
                }
            }

        }

    }


    class SemanaFragment : Fragment() {
        private lateinit var viewPagerSemana: ViewPager
        private lateinit var tabLayoutSemana: TabLayout
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_semana, container, false)
            viewPagerSemana = rootView.findViewById(R.id.viewPagerSemana)
            tabLayoutSemana = rootView.findViewById(R.id.tab_layout_gastosIngresos_Semana)
            setupViewPager()
            return rootView
        }

        private fun setupViewPager() {
            val adapter = DiaViewPagerAdapter(childFragmentManager)
            adapter.addFragment(GastosFragment(), "Gastos")
            adapter.addFragment(IngresosFragment(), "Ingresos")
            viewPagerSemana.adapter = adapter
            tabLayoutSemana.setupWithViewPager(viewPagerSemana)
        }

        class GastosFragment : Fragment() {

            private lateinit var recyclerViewGastos: RecyclerView
            private lateinit var textoSinCategorias: TextView
            private val gastosAdapter = GastosAdapter()
            private val REQUEST_CODE_ADD_CATEGORY = 1

            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                val rootView = inflater.inflate(R.layout.fragment_gastos, container, false)

                recyclerViewGastos = rootView.findViewById(R.id.recyclerViewGastos)
                textoSinCategorias = rootView.findViewById(R.id.textoSinCategorias)
                val floatingButton: FloatingActionButton = rootView.findViewById(R.id.floatingAnadirGastos)

                floatingButton.setOnClickListener {
                    val intent = Intent(requireContext(), AnadirCategoria::class.java)
                    startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY)
                }

                return rootView
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                recyclerViewGastos.layoutManager = LinearLayoutManager(requireContext())
                recyclerViewGastos.adapter = gastosAdapter
                updateUI()
            }

            private fun updateUI() {
                val gastos = GastosManager.getGastos()
                if (gastos.isEmpty()) {
                    textoSinCategorias.visibility = View.VISIBLE
                    recyclerViewGastos.visibility = View.GONE
                } else {
                    textoSinCategorias.visibility = View.GONE
                    recyclerViewGastos.visibility = View.VISIBLE
                    gastosAdapter.submitList(gastos)
                }
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (requestCode == REQUEST_CODE_ADD_CATEGORY && resultCode == Activity.RESULT_OK) {
                    val imagenId = data?.getIntExtra("imagenId", R.drawable.addphotoalternate)
                    val nombreCategoria = data?.getStringExtra("nombreCategoria")
                    val cantidad = data?.getStringExtra("cantidad")
                    val comentario = data?.getStringExtra("comentario")

                    // Crea un nuevo Gasto y agrega a la lista
                    if (imagenId != null && nombreCategoria != null && cantidad != null && comentario != null) {
                        val nuevoGasto = Gasto(imagenId, nombreCategoria, cantidad, comentario)
                        GastosManager.addGasto(nuevoGasto)
                        updateUI()
                    }
                }
            }

        }

        class IngresosFragment : Fragment() {
            private lateinit var recyclerViewIngresos :  RecyclerView
            private lateinit var textoSinCategorias: TextView
            private val ingresosAdapter = IngresosAdapter()
            private val REQUEST_CODE_ADD_CATEGORY = 1
            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                val rootView = inflater.inflate(R.layout.fragment_ingresos, container, false)

                recyclerViewIngresos = rootView.findViewById(R.id.recyclerViewIngresos)
                textoSinCategorias = rootView.findViewById(R.id.textoSinCategoriasIngresos)
                val floatingButton: FloatingActionButton = rootView.findViewById(R.id.floatingAnadirIngresos)

                floatingButton.setOnClickListener {
                    val intent = Intent(requireContext(), AnadirCategoriaIngresos::class.java)
                    startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY)
                }

                return rootView
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                recyclerViewIngresos.layoutManager = LinearLayoutManager(requireContext())
                recyclerViewIngresos.adapter = ingresosAdapter
                updateUI()
            }

            private fun updateUI() {
                val ingresos = IngresosManager.getIngresos()
                if (ingresos.isEmpty()) {
                    textoSinCategorias.visibility = View.VISIBLE
                    recyclerViewIngresos.visibility = View.GONE
                } else {
                    textoSinCategorias.visibility = View.GONE
                    recyclerViewIngresos.visibility = View.VISIBLE
                    ingresosAdapter.submitList(ingresos)
                }
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (requestCode == REQUEST_CODE_ADD_CATEGORY && resultCode == Activity.RESULT_OK) {
                    val imagenId = data?.getIntExtra("imagenId", R.drawable.addphotoalternate)
                    val nombreCategoria = data?.getStringExtra("nombreCategoria")
                    val cantidad = data?.getStringExtra("cantidad")
                    val comentario = data?.getStringExtra("comentario")

                    // Crea un nuevo Gasto y agrega a la lista
                    if (imagenId != null && nombreCategoria != null && cantidad != null && comentario != null) {
                        val nuevoIngresos = Ingresos(imagenId, nombreCategoria, cantidad, comentario)
                        IngresosManager.addIngresos(nuevoIngresos)
                        updateUI()
                    }
                }
            }

        }
    }

    class MesFragment : Fragment() {
        private lateinit var viewPagerMes: ViewPager
        private lateinit var tabLayoutMes: TabLayout
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_mes, container, false)
            viewPagerMes = rootView.findViewById(R.id.viewPagerMes)
            tabLayoutMes = rootView.findViewById(R.id.tab_layout_gastosIngresos_mes)
            setupViewPager()
            return rootView
        }

        private fun setupViewPager() {
            val adapter = DiaViewPagerAdapter(childFragmentManager)
            adapter.addFragment(GastosFragment(), "Gastos")
            adapter.addFragment(IngresosFragment(), "Ingresos")
            viewPagerMes.adapter = adapter
            tabLayoutMes.setupWithViewPager(viewPagerMes)
        }

        class GastosFragment : Fragment() {

            private lateinit var recyclerViewGastos: RecyclerView
            private lateinit var textoSinCategorias: TextView
            private val gastosAdapter = GastosAdapter()
            private val REQUEST_CODE_ADD_CATEGORY = 1

            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                val rootView = inflater.inflate(R.layout.fragment_gastos, container, false)

                recyclerViewGastos = rootView.findViewById(R.id.recyclerViewGastos)
                textoSinCategorias = rootView.findViewById(R.id.textoSinCategorias)
                val floatingButton: FloatingActionButton = rootView.findViewById(R.id.floatingAnadirGastos)

                floatingButton.setOnClickListener {
                    val intent = Intent(requireContext(), AnadirCategoria::class.java)
                    startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY)
                }

                return rootView
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                recyclerViewGastos.layoutManager = LinearLayoutManager(requireContext())
                recyclerViewGastos.adapter = gastosAdapter
                updateUI()
            }

            private fun updateUI() {
                val gastos = GastosManager.getGastos()
                if (gastos.isEmpty()) {
                    textoSinCategorias.visibility = View.VISIBLE
                    recyclerViewGastos.visibility = View.GONE
                } else {
                    textoSinCategorias.visibility = View.GONE
                    recyclerViewGastos.visibility = View.VISIBLE
                    gastosAdapter.submitList(gastos)
                }
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (requestCode == REQUEST_CODE_ADD_CATEGORY && resultCode == Activity.RESULT_OK) {
                    val imagenId = data?.getIntExtra("imagenId", R.drawable.addphotoalternate)
                    val nombreCategoria = data?.getStringExtra("nombreCategoria")
                    val cantidad = data?.getStringExtra("cantidad")
                    val comentario = data?.getStringExtra("comentario")

                    // Crea un nuevo Gasto y agrega a la lista
                    if (imagenId != null && nombreCategoria != null && cantidad != null && comentario != null) {
                        val nuevoGasto = Gasto(imagenId, nombreCategoria, cantidad, comentario)
                        GastosManager.addGasto(nuevoGasto)
                        updateUI()
                    }
                }
            }

        }

        class IngresosFragment : Fragment() {
            private lateinit var recyclerViewIngresos :  RecyclerView
            private lateinit var textoSinCategorias: TextView
            private val ingresosAdapter = IngresosAdapter()
            private val REQUEST_CODE_ADD_CATEGORY = 1
            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                val rootView = inflater.inflate(R.layout.fragment_ingresos, container, false)

                recyclerViewIngresos = rootView.findViewById(R.id.recyclerViewIngresos)
                textoSinCategorias = rootView.findViewById(R.id.textoSinCategoriasIngresos)
                val floatingButton: FloatingActionButton = rootView.findViewById(R.id.floatingAnadirIngresos)

                floatingButton.setOnClickListener {
                    val intent = Intent(requireContext(), AnadirCategoriaIngresos::class.java)
                    startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY)
                }

                return rootView
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                recyclerViewIngresos.layoutManager = LinearLayoutManager(requireContext())
                recyclerViewIngresos.adapter = ingresosAdapter
                updateUI()
            }

            private fun updateUI() {
                val ingresos = IngresosManager.getIngresos()
                if (ingresos.isEmpty()) {
                    textoSinCategorias.visibility = View.VISIBLE
                    recyclerViewIngresos.visibility = View.GONE
                } else {
                    textoSinCategorias.visibility = View.GONE
                    recyclerViewIngresos.visibility = View.VISIBLE
                    ingresosAdapter.submitList(ingresos)
                }
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (requestCode == REQUEST_CODE_ADD_CATEGORY && resultCode == Activity.RESULT_OK) {
                    val imagenId = data?.getIntExtra("imagenId", R.drawable.addphotoalternate)
                    val nombreCategoria = data?.getStringExtra("nombreCategoria")
                    val cantidad = data?.getStringExtra("cantidad")
                    val comentario = data?.getStringExtra("comentario")

                    // Crea un nuevo Gasto y agrega a la lista
                    if (imagenId != null && nombreCategoria != null && cantidad != null && comentario != null) {
                        val nuevoIngresos = Ingresos(imagenId, nombreCategoria, cantidad, comentario)
                        IngresosManager.addIngresos(nuevoIngresos)
                        updateUI()
                    }
                }
            }

        }
    }

    class AnioFragment : Fragment() {
        private lateinit var viewPagerAnio: ViewPager
        private lateinit var tabLayoutAnio: TabLayout
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_anio, container, false)
            viewPagerAnio = rootView.findViewById(R.id.viewPagerAnio)
            tabLayoutAnio = rootView.findViewById(R.id.tab_layout_gastosIngresos_anio)
            setupViewPager()
            return rootView
        }

        private fun setupViewPager() {
            val adapter = DiaViewPagerAdapter(childFragmentManager)
            adapter.addFragment(GastosFragment(), "Gastos")
            adapter.addFragment(IngresosFragment(), "Ingresos")
            viewPagerAnio.adapter = adapter
            tabLayoutAnio.setupWithViewPager(viewPagerAnio)
        }

        class GastosFragment : Fragment() {

            private lateinit var recyclerViewGastos: RecyclerView
            private lateinit var textoSinCategorias: TextView
            private val gastosAdapter = GastosAdapter()
            private val REQUEST_CODE_ADD_CATEGORY = 1

            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                val rootView = inflater.inflate(R.layout.fragment_gastos, container, false)

                recyclerViewGastos = rootView.findViewById(R.id.recyclerViewGastos)
                textoSinCategorias = rootView.findViewById(R.id.textoSinCategorias)
                val floatingButton: FloatingActionButton = rootView.findViewById(R.id.floatingAnadirGastos)

                floatingButton.setOnClickListener {
                    val intent = Intent(requireContext(), AnadirCategoria::class.java)
                    startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY)
                }

                return rootView
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                recyclerViewGastos.layoutManager = LinearLayoutManager(requireContext())
                recyclerViewGastos.adapter = gastosAdapter
                updateUI()
            }

            private fun updateUI() {
                val gastos = GastosManager.getGastos()
                if (gastos.isEmpty()) {
                    textoSinCategorias.visibility = View.VISIBLE
                    recyclerViewGastos.visibility = View.GONE
                } else {
                    textoSinCategorias.visibility = View.GONE
                    recyclerViewGastos.visibility = View.VISIBLE
                    gastosAdapter.submitList(gastos)
                }
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (requestCode == REQUEST_CODE_ADD_CATEGORY && resultCode == Activity.RESULT_OK) {
                    val imagenId = data?.getIntExtra("imagenId", R.drawable.addphotoalternate)
                    val nombreCategoria = data?.getStringExtra("nombreCategoria")
                    val cantidad = data?.getStringExtra("cantidad")
                    val comentario = data?.getStringExtra("comentario")

                    // Crea un nuevo Gasto y agrega a la lista
                    if (imagenId != null && nombreCategoria != null && cantidad != null && comentario != null) {
                        val nuevoGasto = Gasto(imagenId, nombreCategoria, cantidad, comentario)
                        GastosManager.addGasto(nuevoGasto)
                        updateUI()
                    }
                }
            }
        }

        class IngresosFragment : Fragment() {
            private lateinit var recyclerViewIngresos :  RecyclerView
            private lateinit var textoSinCategorias: TextView
            private val ingresosAdapter = IngresosAdapter()
            private val REQUEST_CODE_ADD_CATEGORY = 1
            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                val rootView = inflater.inflate(R.layout.fragment_ingresos, container, false)

                recyclerViewIngresos = rootView.findViewById(R.id.recyclerViewIngresos)
                textoSinCategorias = rootView.findViewById(R.id.textoSinCategoriasIngresos)
                val floatingButton: FloatingActionButton = rootView.findViewById(R.id.floatingAnadirIngresos)

                floatingButton.setOnClickListener {
                    val intent = Intent(requireContext(), AnadirCategoriaIngresos::class.java)
                    startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY)
                }

                return rootView
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                recyclerViewIngresos.layoutManager = LinearLayoutManager(requireContext())
                recyclerViewIngresos.adapter = ingresosAdapter
                updateUI()
            }

            private fun updateUI() {
                val ingresos = IngresosManager.getIngresos()
                if (ingresos.isEmpty()) {
                    textoSinCategorias.visibility = View.VISIBLE
                    recyclerViewIngresos.visibility = View.GONE
                } else {
                    textoSinCategorias.visibility = View.GONE
                    recyclerViewIngresos.visibility = View.VISIBLE
                    ingresosAdapter.submitList(ingresos)
                }
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (requestCode == REQUEST_CODE_ADD_CATEGORY && resultCode == Activity.RESULT_OK) {
                    val imagenId = data?.getIntExtra("imagenId", R.drawable.addphotoalternate)
                    val nombreCategoria = data?.getStringExtra("nombreCategoria")
                    val cantidad = data?.getStringExtra("cantidad")
                    val comentario = data?.getStringExtra("comentario")

                    // Crea un nuevo Gasto y agrega a la lista
                    if (imagenId != null && nombreCategoria != null && cantidad != null && comentario != null) {
                        val nuevoIngresos = Ingresos(imagenId, nombreCategoria, cantidad, comentario)
                        IngresosManager.addIngresos(nuevoIngresos)
                        updateUI()
                    }
                }
            }

        }
    }
}
