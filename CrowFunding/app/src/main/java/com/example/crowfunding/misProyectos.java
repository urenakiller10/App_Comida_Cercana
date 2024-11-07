package com.example.crowfunding;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class misProyectos extends AppCompatActivity {
    private RecyclerView rvProjects;
    private ProyectoAdapter proyectoAdapter;
    private List<Proyecto> proyectosList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_misproyectos); // Asegúrate de que el XML está bien referenciado

        // Inicializa el RecyclerView
        rvProjects = findViewById(R.id.rvProjects);
        rvProjects.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa la lista de proyectos
        proyectosList = new ArrayList<>();

        // Configura el adaptador
        proyectoAdapter = new ProyectoAdapter(proyectosList, this);
        rvProjects.setAdapter(proyectoAdapter);

        // Referencia a la base de datos de Firebase
        db = FirebaseFirestore.getInstance();

        // Carga los proyectos desde Firebase
        //cargarProyectos();
        loadProyectos();
    }

    private void loadProyectos() {
        // Obtener el id del usuario autenticado
        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Realizar la consulta en Firestore filtrando por el campo "idUser"
        db.collection("proyectos")
                .whereEqualTo("idUser", idUser)  // Filtrar por el idUser
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        proyectosList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Obtener los datos del proyecto
                            Proyecto proyecto = document.toObject(Proyecto.class);
                            proyectosList.add(proyecto);
                        }
                        proyectoAdapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                    } else {
                        // Aquí puedes manejar cualquier error si la consulta no fue exitosa
                        // Toast.makeText(MainScreenActivity.this, "Error al cargar proyectos", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejo de error en caso de que la consulta falle
                    // Toast.makeText(MainScreenActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



}
