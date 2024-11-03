package com.example.crowfunding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class misProyectos extends AppCompatActivity {
    private RecyclerView rvProjects;
    private ProyectoAdapter proyectoAdapter;
    private List<Proyecto> proyectosList;
    private DatabaseReference proyectosRef;

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
        proyectosRef = FirebaseDatabase.getInstance().getReference("proyectos");

        // Carga los proyectos desde Firebase
        cargarProyectos();
    }

    private void cargarProyectos() {
        proyectosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                proyectosList.clear();
                for (DataSnapshot projectSnapshot : snapshot.getChildren()) {
                    Proyecto proyecto = projectSnapshot.getValue(Proyecto.class);
                    if (proyecto != null) {
                        // Puedes almacenar el ID único en el objeto Proyecto (si lo necesitas)
                        proyecto.setIdProyecto(projectSnapshot.getKey());
                        proyectosList.add(proyecto);
                    }
                }
                proyectoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error en caso de que no se puedan cargar los proyectos
            }
        });
    }
}
