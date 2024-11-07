package com.example.crowfunding;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GestionUsuarios_Admin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<Usuario> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflar el layout correspondiente
        setContentView(R.layout.view_gestionusuarios_admin);

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UserAdapter(users);
        recyclerView.setAdapter(adapter);

        // Cargar los usuarios desde Firebase
        cargarUsuariosDesdeFirebase();
    }

    private void cargarUsuariosDesdeFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Acceder a la colección "users" en Firestore
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Limpiar la lista de usuarios antes de agregar los nuevos datos
                        users.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Extraer el nombre del usuario y cualquier otro dato necesario
                            String name = document.getString("name");
                            if (name != null) {
                                users.add(new Usuario(name));
                            }
                        }
                        // Notificar al adaptador que los datos han cambiado
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(GestionUsuarios_Admin.this, "Error al cargar usuarios", Toast.LENGTH_SHORT).show();
                        Log.e("GestionUsuarios_Admin", "Error al cargar usuarios", task.getException());
                    }
                });
    }
}