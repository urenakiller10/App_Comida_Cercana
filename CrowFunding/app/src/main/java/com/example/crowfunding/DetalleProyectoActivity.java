package com.example.crowfunding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class DetalleProyectoActivity extends AppCompatActivity {
    private TextView textNombre, textDescripcion, textFechaLimite, textObjetivo;
    private EditText etDineroDonacion; // Campo de entrada para la donación
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
        textFechaLimite = findViewById(R.id.textFechaLimite);
        textObjetivo = findViewById(R.id.textObjetivo);
        etDineroDonacion = findViewById(R.id.etDineroDonacion); // Asegúrate de tener este campo en tu XML

        // Inicializa Firestore
        db = FirebaseFirestore.getInstance();

        // Carga los datos del proyecto desde Firestore
        cargarDatosProyecto();

        // Configura el botón de donación
        Button btnDonar = findViewById(R.id.btnDonar); // Asegúrate de tener este botón en tu XML
        btnDonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarDonacion();
            }
        });
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

    private void realizarDonacion() {
        String donacionStr = etDineroDonacion.getText().toString();
        if (donacionStr.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un monto de donación", Toast.LENGTH_SHORT).show();
            return;
        }

        float donacion = Float.parseFloat(donacionStr); // Convertir a float

        // Obtén la referencia del proyecto
        DocumentReference proyectoRef = db.collection("proyectos").document(proyectoId);

        // Actualiza el dinero del proyecto
        proyectoRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    Proyecto proyecto = snapshot.toObject(Proyecto.class);
                    if (proyecto != null) {
                        float dineroActual = proyecto.getDineroActual(); // Asumiendo que hay un método getDineroActual() que devuelve float
                        float nuevoTotal = dineroActual + donacion;

                        // Actualiza el documento en Firestore
                        proyectoRef.update("dineroActual", nuevoTotal)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(DetalleProyectoActivity.this, "Donación realizada con éxito", Toast.LENGTH_SHORT).show();
                                    etDineroDonacion.setText(""); // Limpia el campo de entrada
                                    // Aquí podrías actualizar la UI con el nuevo total si lo deseas
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(DetalleProyectoActivity.this, "Error al realizar la donación", Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            } else {
                Toast.makeText(DetalleProyectoActivity.this, "Error en la conexión con la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}