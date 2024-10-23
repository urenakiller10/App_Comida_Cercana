package com.example.crowfunding;
import com.example.crowfunding.R;

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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainScreenActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;  // FirebaseAuth para gestionar la sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

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
                } else if (id == R.id.nav_MisProyectos) {
                    Toast.makeText(MainScreenActivity.this, "Mis proyectos seleccionado", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_cartera) {
                    Intent intent = new Intent(MainScreenActivity.this, cartera.class);
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
