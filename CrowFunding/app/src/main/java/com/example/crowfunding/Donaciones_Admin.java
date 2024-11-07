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
    private List<DonacionData> donacionesList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_donaciones_admin);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewDonaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        donacionesList = new ArrayList<>();
        donacionAdapter = new DonacionAdapter(donacionesList, this);
        recyclerView.setAdapter(donacionAdapter);

        loadDonaciones();
    }

    private void loadDonaciones() {
        db.collection("donaciones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        donacionesList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Donacion donacion = document.toObject(Donacion.class);
                            String idProyecto = donacion.getIdProyecto();

                            // Obtener el nombre del proyecto
                            db.collection("proyectos").document(idProyecto).get().addOnCompleteListener(projectTask -> {
                                if (projectTask.isSuccessful() && projectTask.getResult() != null) {
                                    String nombreProyecto = projectTask.getResult().getString("nombre");

                                    // Crear el objeto DonacionData con los datos obtenidos (sin nombre de usuario)
                                    DonacionData donacionData = new DonacionData(
                                            donacion.getFecha(),
                                            donacion.getMonto(),
                                            "aaaaa", // Sin nombre de usuario
                                            nombreProyecto
                                    );

                                    donacionesList.add(donacionData);
                                    donacionAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(Donaciones_Admin.this, "Error al obtener nombre del proyecto", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(Donaciones_Admin.this, "Error al cargar las donaciones", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Donaciones_Admin.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
