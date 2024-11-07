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

public class HomeAdmin extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ProyectoAdapter proyectoAdapter;
    private List<Proyecto> proyectoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_home_admin);

        // Inicializar FirebaseAuth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar el RecyclerView y la lista de proyectos
        recyclerView = findViewById(R.id.recyclerView);
        proyectoList = new ArrayList<>();

        // Configurar el adaptador sin OnClickListener
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

                if (id == R.id.nav_home_admin) {
                    Toast.makeText(HomeAdmin.this, "Inicio seleccionado", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_donaciones) {
                    Toast.makeText(HomeAdmin.this, "Donaciones seleccionado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeAdmin.this, Donaciones_Admin.class);
                    startActivity(intent);

                } else if (id == R.id.nav_usuarios) {
                    Toast.makeText(HomeAdmin.this, "Gestión de usuarios seleccionado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeAdmin.this, GestionUsuarios_Admin.class);
                    startActivity(intent);

                } else if (id == R.id.nav_estadisticas) {
                    Toast.makeText(HomeAdmin.this, "Estadísticas seleccionado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeAdmin.this, Estadisticas_Admin.class);
                    startActivity(intent);

                } else if (id == R.id.nav_logout) {
                    mAuth.signOut();
                    Intent intent = new Intent(HomeAdmin.this, iniciar_sesion.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else if (id == R.id.nav_mis_donaciones) {
                    Toast.makeText(HomeAdmin.this, "Mis Donaciones", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeAdmin.this, DonacionesUsuarioActivity.class);
                    startActivity(intent);
                }



                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
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
                            Proyecto proyecto = document.toObject(Proyecto.class);
                            proyectoList.add(proyecto);
                        }
                        proyectoAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(HomeAdmin.this, "Error al cargar proyectos", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeAdmin.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
