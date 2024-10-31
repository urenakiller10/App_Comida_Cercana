package com.example.crowfunding;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import java.util.List;

public class MainScreenActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;  // FirebaseAuth para gestionar la sesión
    private FirebaseFirestore db; // Firestore para gestionar la base de datos
    private RecyclerView recyclerView; // RecyclerView para mostrar proyectos
    private ProyectoAdapter proyectoAdapter; // Adaptador para el RecyclerView
    private List<Proyecto> proyectoList; // Lista de proyectos

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
                    Intent intent = new Intent(MainScreenActivity.this, donaciones.class);
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

        // Buscar el botón en el layout
        Button createProjectButton = findViewById(R.id.create_project_button);

        // Configurar el evento onClick para el botón
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
                        proyectoList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Obtener los datos del proyecto
                            Proyecto proyecto = document.toObject(Proyecto.class);
                            proyectoList.add(proyecto);
                        }
                        proyectoAdapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                    } else {
                        Toast.makeText(MainScreenActivity.this, "Error al cargar proyectos", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainScreenActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START); // Cierra el menú si está abierto
        } else {
            super.onBackPressed();
        }
    }
}
