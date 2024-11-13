package com.example.crowfunding;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.semantics.Role;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GestionRolesPermisosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRoles, recyclerViewPermisos;
    private RolesAdapter rolesAdapter;
    private PermisosAdapter permisosAdapter;
    private List<Rol> roles = new ArrayList<>();
    private List<Permiso> permisos = new ArrayList<>();
    private String userId;
    private TextView tvNombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_rolespermisos);

        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        String userId = getIntent().getStringExtra("userId");

        if (userId != null) {
            // Llamamos a la función que buscará el nombre del usuario en Firestore
            obtenerNombreUsuario(userId);
        } else {
            Log.e("GestionRolesPermisos", "No se recibió el ID de usuario");
        }

        cargarRolesPermisos();

        permisos = new ArrayList<>(); // Inicializa como lista vacía
        permisosAdapter = new PermisosAdapter(permisos);

        roles = new ArrayList<>();
        rolesAdapter = new RolesAdapter(roles);
    }

    private void obtenerNombreUsuario(String userId) {
        // Referencia a la colección de usuarios en Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Consulta para obtener el documento del usuario con el ID correspondiente
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Si la consulta es exitosa, obtenemos el documento
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            String nombre = document.getString("name");

                            tvNombreUsuario.setText(nombre);
                        } else {
                            Log.e("GestionRolesPermisos", "No se encontró el usuario con ID: " + userId);
                        }
                    } else {
                        Log.e("GestionRolesPermisos", "Error al obtener el usuario", task.getException());
                    }
                });
    }

    private void cargarRolesPermisos() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        recyclerViewRoles = findViewById(R.id.rvListaRoles);
        recyclerViewPermisos = findViewById(R.id.rvListaPermisos);

        db.collection("roles")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Rol> roles = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String rolName = document.getString("Rol");
                            if (rolName != null) {
                                Rol role = new Rol(rolName);
                                roles.add(role);
                            }
                        }
                        rolesAdapter.setRoles(roles);
                        rolesAdapter.notifyDataSetChanged();
                        mostrarRoles(roles);
                    } else {
                        Log.e("GestionRolesPermisos", "Error al obtener roles", task.getException());
                    }
                });

        // Cargar permisos del usuario
        db.collection("permisos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Permiso> permisos = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String permisoName = document.getString("Permiso");
                            if (permisoName != null) {
                                Permiso permiso = new Permiso(permisoName);
                                permisos.add(permiso);
                            }
                        }
                        permisosAdapter.setPermisos(permisos);
                        permisosAdapter.notifyDataSetChanged();
                        mostrarPermisos(permisos);
                    } else {
                        Log.e("GestionRolesPermisos", "Error al obtener permisos", task.getException());
                    }
                });
    }

        private void mostrarRoles(List<Rol> roles) {
            RolesAdapter rolesAdapter = new RolesAdapter(roles);
            recyclerViewRoles.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewRoles.setAdapter(rolesAdapter);
        }

        private void mostrarPermisos(List<Permiso> permisos) {
            PermisosAdapter permisosAdapter = new PermisosAdapter(permisos);
            recyclerViewPermisos.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewPermisos.setAdapter(permisosAdapter);
        }
}