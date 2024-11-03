package com.example.crowfunding;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class DetalleProyectoActivity extends AppCompatActivity {
    private TextView textNombre, textDescripcion, textFechaLimite, textObjetivo, textCreacion;
    private FirebaseFirestore db;
    private String proyectoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_proyecto);

        // Obtén el ID del proyecto desde el Intent
        proyectoId = getIntent().getStringExtra("proyectoId");

        // Verifica que el ID del proyecto no sea nulo
        if (proyectoId == null) {
            Toast.makeText(this, "Error: Proyecto no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Enlaza los elementos de la interfaz de usuario
        textNombre = findViewById(R.id.textNombre);
        textDescripcion = findViewById(R.id.textDescripcion);
        // textFechaCreacion = findViewById(R.id.textFechaCreacion); // Comentado temporalmente
        textFechaLimite = findViewById(R.id.textFechaLimite);
        textObjetivo = findViewById(R.id.textObjetivo);
        textCreacion = findViewById(R.id.textFechaCreacion);

        // Inicializa Firestore
        db = FirebaseFirestore.getInstance();

        // Carga los datos del proyecto desde Firestore
        cargarDatosProyecto();
    }

    private void cargarDatosProyecto() {
        DocumentReference proyectoRef = db.collection("proyectos").document(proyectoId);
        proyectoRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    Proyecto proyecto = snapshot.toObject(Proyecto.class);
                    if (proyecto != null) {
                        textNombre.setText(proyecto.getNombre());
                        textDescripcion.setText(proyecto.getDescripcion());
                        textFechaLimite.setText(proyecto.getFechaLimite());
                        textObjetivo.setText(proyecto.getObjetivoFinanciacion());
                        textCreacion.setText(proyecto.getFechaCreacion());

                        // Establece la fecha de creación si está disponible en la base de datos
                        // String fechaCreacion = snapshot.getString("fechaCreacion");
                        // if (fechaCreacion != null) {
                        //     textFechaCreacion.setText(fechaCreacion);
                        // } else {
                        //     textFechaCreacion.setText("Fecha no disponible");
                        // }
                    } else {
                        Toast.makeText(DetalleProyectoActivity.this, "Error al cargar el proyecto", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetalleProyectoActivity.this, "Proyecto no encontrado", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DetalleProyectoActivity.this, "Error en la conexión con la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
