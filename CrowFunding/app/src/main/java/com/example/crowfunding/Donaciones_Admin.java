package com.example.crowfunding;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Donaciones_Admin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DonacionAdapter donacionAdapter;
    private List<Donacion> donacionesList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflar el layout correspondiente
        setContentView(R.layout.view_donaciones_admin);

        // Inicializar Firestore y RecyclerView
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewDonaciones); // Asegúrate de que el ID en el layout sea correcto
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar la lista de donaciones
        donacionesList = new ArrayList<>();

        // Cargar las donaciones
        loadDonaciones();
    }

    private void loadDonaciones() {
        db.collection("donaciones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        donacionesList.clear(); // Limpiar lista antes de agregar los nuevos datos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Donacion donacion = document.toObject(Donacion.class);
                            donacionesList.add(donacion);
                        }
                        // Configurar el adaptador con los datos de donaciones
                        donacionAdapter = new DonacionAdapter(donacionesList, Donaciones_Admin.this);
                        recyclerView.setAdapter(donacionAdapter);
                    } else {
                        Toast.makeText(Donaciones_Admin.this, "Error al cargar las donaciones", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Donaciones_Admin.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
