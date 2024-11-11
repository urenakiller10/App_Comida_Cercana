package com.example.crowfunding;

import android.os.Bundle;
import android.util.Log;
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
    private DonacionAdapterAdmin donacionAdapter;
    private List<DonacionData> donacionesList;
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

                            // Obtener idUser e idProyecto de la donación
                            String idUser = donacion.getIdUser(); // ID del usuario que hizo la donación
                            String idProyecto = donacion.getIdProyecto(); // ID del proyecto

                            // Obtener nombre del usuario desde la colección "usuarios"
                            if (idUser != null) {
                                db.collection("users").document(idUser)
                                        .get()
                                        .addOnCompleteListener(userTask -> {
                                            if (userTask.isSuccessful() && userTask.getResult() != null) {
                                                String userName = userTask.getResult().getString("name"); // Suponiendo que el nombre del usuario está en "nombre"
                                                if (userName == null) {
                                                    userName = "Mi novia"; // Asignar "Mi novia" si el nombre es nulo
                                                }

                                                // Obtener nombre del proyecto desde la colección "proyectos"
                                                String finalUserName = userName;
                                                db.collection("proyectos").document(idProyecto)
                                                        .get()
                                                        .addOnCompleteListener(projectTask -> {
                                                            if (projectTask.isSuccessful() && projectTask.getResult() != null) {
                                                                String projectName = projectTask.getResult().getString("nombre"); // Suponiendo que el nombre del proyecto está en "nombre"
                                                                if (projectName == null) {
                                                                    projectName = "Proyecto desconocido"; // Asignar nombre por defecto si el nombre del proyecto es nulo
                                                                }

                                                                // Crear el objeto DonacionData con los datos obtenidos
                                                                DonacionData donacionData = new DonacionData(
                                                                        donacion.getFecha(),
                                                                        donacion.getMonto(),
                                                                        finalUserName,    // Asignar el nombre del donante
                                                                        projectName  // Asignar el nombre del proyecto
                                                                );

                                                                // Añadir la instancia de DonacionData a la lista
                                                                donacionesList.add(donacionData);

                                                                // Notificar al adaptador después de cargar todos los datos
                                                                if (donacionesList.size() == task.getResult().size()) {
                                                                    donacionAdapter.notifyDataSetChanged();
                                                                }
                                                            }
                                                        });
                                            } else {
                                                // Si no se puede obtener el nombre del usuario, asignamos "Mi novia"
                                                String userName = "Mi novia";

                                                // Obtener nombre del proyecto desde la colección "proyectos"
                                                db.collection("proyectos").document(idProyecto)
                                                        .get()
                                                        .addOnCompleteListener(projectTask -> {
                                                            if (projectTask.isSuccessful() && projectTask.getResult() != null) {
                                                                String projectName = projectTask.getResult().getString("nombre");
                                                                if (projectName == null) {
                                                                    projectName = "Proyecto desconocido";
                                                                }

                                                                // Crear el objeto DonacionData
                                                                DonacionData donacionData = new DonacionData(
                                                                        donacion.getFecha(),
                                                                        donacion.getMonto(),
                                                                        userName,
                                                                        projectName
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
                            } else {
                                // Si no hay idUser, asignamos "Mi novia"
                                String userName = "Mi novia";

                                // Obtener nombre del proyecto desde la colección "proyectos"
                                db.collection("proyectos").document(idProyecto)
                                        .get()
                                        .addOnCompleteListener(projectTask -> {
                                            if (projectTask.isSuccessful() && projectTask.getResult() != null) {
                                                String projectName = projectTask.getResult().getString("nombre");
                                                if (projectName == null) {
                                                    projectName = "Proyecto desconocido";
                                                }

                                                // Crear el objeto DonacionData
                                                DonacionData donacionData = new DonacionData(
                                                        donacion.getFecha(),
                                                        donacion.getMonto(),
                                                        userName,
                                                        projectName
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
                        }
                        // Configurar el adaptador con los datos de donaciones
                        donacionAdapter = new DonacionAdapterAdmin(donacionesList, Donaciones_Admin.this);
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
