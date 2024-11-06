package com.example.crowfunding;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Estadisticas_Admin extends AppCompatActivity {

    // Declaración de los TextViews correspondientes a los campos
    private TextView txtUsuariosActivos;
    private TextView txtCantidadProyectos;
    private TextView txtCantidadDonaciones;
    private TextView txtTotalUsuarios;

    // Declaración de la instancia de Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflar el layout correspondiente
        setContentView(R.layout.view_estadisticas_admin);

        // Inicialización de los TextViews
        txtUsuariosActivos = findViewById(R.id.txtUsuariosActivos);
        txtCantidadProyectos = findViewById(R.id.txtCantidadProyectos);
        txtCantidadDonaciones = findViewById(R.id.txtCantidadDonaciones);
        txtTotalUsuarios = findViewById(R.id.txtTotalUsuarios);

        // Inicialización de Firestore
        db = FirebaseFirestore.getInstance();

        // Cargar los datos de Firebase
        cargarUsuariosActivos();
        cargarCantidadProyectos();
        cargarCantidadDonaciones();
        cargarTotalUsuarios();
    }

    // Cargar la cantidad de usuarios activos
    private void cargarUsuariosActivos() {
        db.collection("users")
                .whereEqualTo("estado", "activo") // Filtramos por el campo "estado" con el valor "activo"
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int usuariosActivos = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            usuariosActivos++; // Contamos los usuarios activos
                        }
                        txtUsuariosActivos.setText("Usuarios Activos: " + usuariosActivos);
                    } else {
                        // Manejo de error
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejo de error
                });
    }

    // Cargar la cantidad de proyectos
    private void cargarCantidadProyectos() {
        db.collection("proyectos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int cantidadProyectos = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            cantidadProyectos++; // Contamos los proyectos
                        }
                        txtCantidadProyectos.setText("Cantidad de Proyectos: " + cantidadProyectos);
                    } else {
                        // Manejo de error
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejo de error
                });
    }

    // Cargar la cantidad de donaciones
    private void cargarCantidadDonaciones() {
        db.collection("donaciones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int cantidadDonaciones = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            cantidadDonaciones++; // Contamos las donaciones
                        }
                        txtCantidadDonaciones.setText("Cantidad de Donaciones: " + cantidadDonaciones);
                    } else {
                        // Manejo de error
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejo de error
                });
    }

    // Cargar el total de usuarios
    private void cargarTotalUsuarios() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int totalUsuarios = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            totalUsuarios++; // Contamos todos los usuarios
                        }
                        txtTotalUsuarios.setText("Total Usuarios: " + totalUsuarios);
                    } else {
                        // Manejo de error
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejo de error
                });
    }
}
