package com.example.scanpiggy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.scanpiggy.adapter.DiaViewPagerAdapter
import com.example.scanpiggy.adapter.GastosAdapter
import com.example.scanpiggy.adapter.ViewPagerAdapter
import com.example.scanpiggy.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class Notas : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_notas)
        setSupportActionBar(binding.toolbar)


        val bottomNavView: BottomNavigationView = findViewById(R.id.nav_view)
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

        val navViewLateral: NavigationView = findViewById(R.id.nav_view_lateral_notas)
        navViewLateral.setNavigationItemSelectedListener(this)



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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_billetera -> {
                val intent = Intent(this, Billetera::class.java)
                startActivity(intent)
            }
            // Otros casos de ítems del menú
        }
        // Cerrar el drawer después de manejar el clic del usuario
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_notas)
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

        class IngresosFragment : Fragment() {
            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                // Inflar el diseño del fragmento
                return inflater.inflate(R.layout.fragment_ingresos, container, false)
            }
        }

        class GastosFragment : Fragment() {

            // Declarar la variable para el RecyclerView
            private lateinit var recyclerViewGastos: RecyclerView
            private lateinit var textoSinCategorias: TextView

            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                // Inflar el diseño del fragmento
                val rootView = inflater.inflate(R.layout.fragment_gastos, container, false)

                // Acceder al RecyclerView
                recyclerViewGastos = rootView.findViewById(R.id.recyclerViewGastos)
                textoSinCategorias = rootView.findViewById(R.id.textoSinCategorias)

                // Configurar el RecyclerView (aquí puedes configurar el LinearLayoutManager, el Adapter, etc.)

                // Acceder al FloatingActionButton
                val floatingButton: FloatingActionButton = rootView.findViewById(R.id.floatingAnadirGastos)

                // Establecer el OnClickListener
                floatingButton.setOnClickListener {
                    // Aquí colocas el código para navegar a la actividad de categoría de gastos
                    // Establecer el OnClickListener del FloatingActionButton
                    floatingButton.setOnClickListener {
                        val intent = Intent(requireContext(), CategoriaGastoActivity::class.java)
                        val REQUEST_CODE_ADD_CATEGORY = 1
                        startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY)
                    }


                    // Después de agregar la categoría, mostrar el RecyclerView y ocultar el texto
                    textoSinCategorias.visibility = View.GONE
                    recyclerViewGastos.visibility = View.VISIBLE

                    // Configurar el RecyclerView
                    val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerViewGastos)
                    if (recyclerView != null) {
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }

                    // Configurar el adaptador
                    val adapter = GastosAdapter()
                    if (recyclerView != null) {
                        recyclerView.adapter = adapter
                    }
                }

                return rootView
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
            adapter.addFragment(GastosFragmentSemana(), "Gastos")
            adapter.addFragment(IngresosFragmentSemana(), "Ingresos")
            viewPagerSemana.adapter = adapter
            tabLayoutSemana.setupWithViewPager(viewPagerSemana)
        }

        class IngresosFragmentSemana : Fragment() {
            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                // Inflar el diseño del fragmento
                return inflater.inflate(R.layout.fragment_ingresos, container, false)
            }
        }

        class GastosFragmentSemana : Fragment() {
            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                // Inflar el diseño del fragmento
                return inflater.inflate(R.layout.fragment_gastos, container, false)
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
            adapter.addFragment(GastosFragmentMes(), "Gastos")
            adapter.addFragment(IngresosFragmentMes(), "Ingresos")
            viewPagerMes.adapter = adapter
            tabLayoutMes.setupWithViewPager(viewPagerMes)
        }

        class IngresosFragmentMes : Fragment() {
            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                // Inflar el diseño del fragmento
                return inflater.inflate(R.layout.fragment_ingresos, container, false)
            }
        }

        class GastosFragmentMes : Fragment() {
            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                // Inflar el diseño del fragmento
                return inflater.inflate(R.layout.fragment_gastos, container, false)
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
            adapter.addFragment(GastosFragmentAnio(), "Gastos")
            adapter.addFragment(IngresosFragmentAnio(), "Ingresos")
            viewPagerAnio.adapter = adapter
            tabLayoutAnio.setupWithViewPager(viewPagerAnio)
        }

        class IngresosFragmentAnio : Fragment() {
            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                // Inflar el diseño del fragmento
                return inflater.inflate(R.layout.fragment_ingresos, container, false)
            }
        }

        class GastosFragmentAnio : Fragment() {
            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                // Inflar el diseño del fragmento
                return inflater.inflate(R.layout.fragment_gastos, container, false)
            }
        }
    }
}
