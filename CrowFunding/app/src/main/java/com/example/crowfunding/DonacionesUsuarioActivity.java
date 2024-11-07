package com.example.crowfunding;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crowfunding.Donacion;
import com.example.crowfunding.DonacionAdapter;
import com.example.crowfunding.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class DonacionesUsuarioActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private DonacionAdapter donacionAdapter;
    private List<DonacionData> donacionesList;
    private String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donaciones_usuario);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerViewDonaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        donacionesList = new ArrayList<>();
        donacionAdapter = new DonacionAdapter(donacionesList, this);
        recyclerView.setAdapter(donacionAdapter);

        String userId = auth.getCurrentUser().getUid();

        // Consulta para obtener el nombre del usuario
        db.collection("users")
                .whereEqualTo("idUser", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        nombreUsuario = task.getResult().getDocuments().get(0).getString("name");
                        // Cargar las donaciones después de obtener el nombre del usuario
                        loadDonaciones(userId);
                    } else {
                        Toast.makeText(this, "Error al obtener el nombre del usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadDonaciones(String userId) {
        db.collection("donaciones")
                .whereEqualTo("idUser", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        donacionesList.clear(); // Limpiar lista antes de agregar nuevos datos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Donacion donacion = document.toObject(Donacion.class);
                            String idProyecto = donacion.getIdProyecto();

                            // Obtener el nombre del proyecto
                            db.collection("proyectos").document(idProyecto).get().addOnCompleteListener(projectTask -> {
                                if (projectTask.isSuccessful() && projectTask.getResult() != null) {
                                    String nombreProyecto = projectTask.getResult().getString("nombre");

                                    // Crear el objeto DonacionData con los datos obtenidos
                                    DonacionData donacionData = new DonacionData(
                                            donacion.getFecha(),
                                            donacion.getMonto(),
                                            nombreUsuario,    // Usa el nombre del usuario obtenido en onCreate
                                            nombreProyecto
                                    );

                                    donacionesList.add(donacionData);
                                    donacionAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(DonacionesUsuarioActivity.this, "Error al obtener nombre del proyecto", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(DonacionesUsuarioActivity.this, "Error al cargar las donaciones", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DonacionesUsuarioActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
