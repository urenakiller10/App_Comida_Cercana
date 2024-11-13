package com.example.crowfunding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.AdapterView;

public class MainScreenActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;  // FirebaseAuth para gestionar la sesión
    private FirebaseFirestore db; // Firestore para gestionar la base de datos
    private RecyclerView recyclerView; // RecyclerView para mostrar proyectos
    private ProyectoAdapter proyectoAdapter; // Adaptador para el RecyclerView
    private List<Proyecto> proyectoList; // Lista de proyectos

    private Spinner filterTypeSpinner, categorySpinner;  // Spinners para tipo de filtro y categorías
    private CheckBox ascendingCheckBox;  // CheckBox para orden ascendente/descendente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Inicializar FirebaseAuth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar el RecyclerView y la lista de proyectos
        recyclerView = findViewById(R.id.recyclerView);
        proyectoList = new ArrayList<>();
        proyectoAdapter = new ProyectoAdapter(proyectoList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(proyectoAdapter);

        // Inicializar Spinners y CheckBox
        filterTypeSpinner = findViewById(R.id.spinner_filter_type);
        categorySpinner = findViewById(R.id.spinner_category);
        ascendingCheckBox = findViewById(R.id.checkbox_ascending);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configurar DrawerLayout y Toggle
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configurar NavigationView
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Toast.makeText(MainScreenActivity.this, "Inicio seleccionado", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_profile) {
                    Toast.makeText(MainScreenActivity.this, "Perfil seleccionado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainScreenActivity.this, perfil.class);
                    startActivity(intent);

                } else if (id == R.id.nav_MisProyectos) {
                    Toast.makeText(MainScreenActivity.this, "Mis proyectos seleccionado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainScreenActivity.this, misProyectos.class);
                    startActivity(intent);

                } else if (id == R.id.nav_cartera) {
                    Intent intent = new Intent(MainScreenActivity.this, cartera.class);
                    startActivity(intent);

                } else if (id == R.id.nav_donaciones) {
                    Intent intent = new Intent(MainScreenActivity.this, DonacionesUser.class);
                    startActivity(intent);

                }else if (id == R.id.nav_foroGeneral) {  // Nueva opción
                    Toast.makeText(MainScreenActivity.this, "Foro General", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainScreenActivity.this, foroGeneral.class);
                    startActivity(intent);

                } else if (id == R.id.nav_logout) {
                    mAuth.signOut();
                    Intent intent = new Intent(MainScreenActivity.this, iniciar_sesion.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }


                drawerLayout.closeDrawer(GravityCompat.START); // Cierra el menú después de seleccionar una opción
                return true;
            }
        });

        // Configurar el Spinner de tipo de filtro
        filterTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filterType = (String) parent.getItemAtPosition(position);
                if (filterType.equals("Categoría")) {
                    categorySpinner.setVisibility(View.VISIBLE);
                    ascendingCheckBox.setVisibility(View.GONE);
                } else if (filterType.equals("Monto") || filterType.equals("Fecha límite")) {
                    categorySpinner.setVisibility(View.GONE);
                    ascendingCheckBox.setVisibility(View.VISIBLE);
                } else {
                    categorySpinner.setVisibility(View.GONE);
                    ascendingCheckBox.setVisibility(View.GONE);
                }
                // Aplicar el filtro y el orden después de cambiar el tipo
                loadProyectos();
                applyFilterAndSort(filterType, ascendingCheckBox.isChecked());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Configurar el CheckBox para ordenar de forma ascendente/descendente
        ascendingCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String filterType = (String) filterTypeSpinner.getSelectedItem();
            applyFilterAndSort(filterType, isChecked);
        });

        // Configurar el Spinner de categoría para filtrar automáticamente
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                loadProyectos();
                // Verificar si la categoría seleccionada es válida y aplicar el filtro
                Log.d("FILTER_DEBUG", "Categoría seleccionada: " + selectedCategory);
                if (selectedCategory != null && !selectedCategory.trim().isEmpty()) {
                    applyFilterAndSort("Categoría", ascendingCheckBox.isChecked());
                } else {
                    // Si no se selecciona una categoría o está vacía, mostrar todos los proyectos
                    applyFilterAndSort("Categoría", ascendingCheckBox.isChecked());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Buscar el botón en el layout para crear un proyecto
        Button createProjectButton = findViewById(R.id.create_project_button);
        createProjectButton.setOnClickListener(view -> {
            // Crear un Intent para abrir la actividad ViewCrearProyecto
            Intent intent = new Intent(MainScreenActivity.this, crearProyecto.class);
            startActivity(intent);
        });

        // Cargar los proyectos desde Firestore
        loadProyectos();
    }

    private void loadProyectos() {
        db.collection("proyectos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        proyectoList.clear(); // Limpiar la lista actual
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Obtener los datos del proyecto
                            Proyecto proyecto = document.toObject(Proyecto.class);
                            proyectoList.add(proyecto);  // Añadir a la lista
                        }
                        // Aplicar filtro y ordenación solo después de cargar los proyectos
                        applyFilterAndSort((String) filterTypeSpinner.getSelectedItem(), ascendingCheckBox.isChecked());
                    } else {
                        Toast.makeText(MainScreenActivity.this, "Error al cargar proyectos", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainScreenActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void applyFilterAndSort(String filterType, boolean isAscending) {
        // Crear una copia de la lista original para trabajar sobre ella
        List<Proyecto> filteredList = new ArrayList<>(proyectoList);

        Log.d("FILTER_DEBUG", "Filter type: " + filterType);
        Log.d("FILTER_DEBUG", "Is ascending: " + isAscending);

        // Filtrar y ordenar por "Monto"
        if (filterType.equals("Monto")) {
            Log.d("FILTER_DEBUG", "Filtrando por Monto");
            if (isAscending) {
                // Ordenar en orden ascendente por el monto (convertido a double)
                filteredList.sort((p1, p2) -> {
                    double monto1 = 0;
                    double monto2 = 0;

                    // Convertir los valores de monto a double (si es válido)
                    try {
                        monto1 = Double.parseDouble(p1.getObjetivoFinanciacion());
                        monto2 = Double.parseDouble(p2.getObjetivoFinanciacion());
                    } catch (NumberFormatException e) {
                        // Manejo de excepción si el monto no es un número válido
                        e.printStackTrace();
                    }

                    Log.d("FILTER_DEBUG", "Monto1: " + monto1 + ", Monto2: " + monto2);

                    return Double.compare(monto1, monto2); // Comparar como números
                });
            } else {
                // Ordenar en orden descendente por el monto (convertido a double)
                filteredList.sort((p1, p2) -> {
                    double monto1 = 0;
                    double monto2 = 0;

                    // Convertir los valores de monto a double (si es válido)
                    try {
                        monto1 = Double.parseDouble(p1.getObjetivoFinanciacion());
                        monto2 = Double.parseDouble(p2.getObjetivoFinanciacion());
                    } catch (NumberFormatException e) {
                        // Manejo de excepción si el monto no es un número válido
                        e.printStackTrace();
                    }

                    Log.d("FILTER_DEBUG", "Monto1: " + monto1 + ", Monto2: " + monto2);

                    return Double.compare(monto2, monto1); // Comparar como números en orden descendente
                });
            }
        }
        // Filtrar y ordenar por "Fecha límite"
        else if (filterType.equals("Fecha límite")) {
            Log.d("FILTER_DEBUG", "Filtrando por Fecha límite");
            if (isAscending) {
                // Ordenar en orden ascendente por la fecha límite
                filteredList.sort(Comparator.comparing(Proyecto::getFechaLimite));
            } else {
                // Ordenar en orden descendente por la fecha límite
                filteredList.sort(Comparator.comparing(Proyecto::getFechaLimite).reversed());
            }
        }
        // Filtrar por "Categoría"
        else if (filterType.equals("Categoría")) {
            // Obtener la categoría seleccionada del Spinner
            String selectedCategory = (String) categorySpinner.getSelectedItem();

            // Depuración: Asegúrate de que el valor seleccionado no sea vacío
            Log.d("FILTER_DEBUG", "Categoría seleccionada: " + selectedCategory);

            if (selectedCategory == null || selectedCategory.trim().isEmpty()) {
                Log.d("FILTER_DEBUG", "No se seleccionó categoría, mostrando todos los proyectos");
                // Si la categoría está vacía, mostrar todos los proyectos
                filteredList = new ArrayList<>(proyectoList); // Mostrar todos los proyectos
            } else {
                // Filtrar la lista de proyectos para que solo contenga los de la categoría seleccionada
                filteredList = proyectoList.stream()
                        .filter(proyecto -> proyecto.getCategoria() != null && proyecto.getCategoria().equalsIgnoreCase(selectedCategory))
                        .collect(Collectors.toList());

                Log.d("FILTER_DEBUG", "Número de proyectos filtrados por categoría: " + filteredList.size());
            }
        }

        // Actualizar la lista del adaptador con los nuevos datos filtrados y ordenados
        Log.d("FILTER_DEBUG", "Actualizando datos en el adaptador, número de proyectos: " + filteredList.size());
        proyectoAdapter.updateData(filteredList);
    }

}