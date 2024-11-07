package com.example.crowfunding;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DonacionesUser extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DonacionAdapterUser donacionAdapter;
    private List<DonacionData> donacionesList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflar el layout correspondiente
        setContentView(R.layout.view_donaciones);

        // Inicializar Firestore y RecyclerView
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewDonaciones); // Asegúrate de que el ID en el layout sea correcto
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar la lista de donaciones
        donacionesList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        String idUser = auth.getCurrentUser().getUid();
        // Cargar las donaciones
        loadDonaciones(idUser);
    }



    private void loadDonaciones(String idUser) {
        // Realizar una consulta filtrando las donaciones por el userId
        db.collection("donaciones")
                .whereEqualTo("idUser", idUser)  // Filtrar por el campo idUser
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        donacionesList.clear(); // Limpiar lista antes de agregar los nuevos datos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Donacion donacion = document.toObject(Donacion.class);

                            // Obtener idProyecto de la donación
                            String idProyecto = donacion.getIdProyecto();

                            // Obtener nombre del usuario desde la colección "users"
                            db.collection("users").document(idUser)
                                    .get()
                                    .addOnCompleteListener(userTask -> {
                                        if (userTask.isSuccessful() && userTask.getResult() != null) {
                                            String userName = userTask.getResult().getString("name"); // Suponiendo que el nombre está en "name"

                                            // Obtener nombre del proyecto desde la colección "proyectos"
                                            db.collection("proyectos").document(idProyecto)
                                                    .get()
                                                    .addOnCompleteListener(projectTask -> {
                                                        if (projectTask.isSuccessful() && projectTask.getResult() != null) {
                                                            String projectName = projectTask.getResult().getString("nombre"); // Nombre del proyecto
                                                            if (projectName == null) {
                                                                projectName = "Proyecto desconocido"; // Asignar nombre por defecto si el nombre del proyecto es nulo
                                                            }

                                                            // Crear el objeto DonacionData con los datos obtenidos
                                                            DonacionData donacionData = new DonacionData(
                                                                    donacion.getFecha(),
                                                                    donacion.getMonto(),
                                                                    userName,    // Nombre del donante
                                                                    projectName  // Nombre del proyecto
                                                            );

                                                            // Añadir la instancia de DonacionData a la lista
                                                            donacionesList.add(donacionData);

                                                            // Notificar al adaptador después de cargar todos los datos
                                                            if (donacionesList.size() == task.getResult().size()) {
                                                                donacionAdapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }
                        // Configurar el adaptador con los datos de donaciones
                        donacionAdapter = new DonacionAdapterUser(donacionesList, DonacionesUser.this);
                        recyclerView.setAdapter(donacionAdapter);
                    } else {
                        Toast.makeText(DonacionesUser.this, "Error al cargar las donaciones", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DonacionesUser.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
