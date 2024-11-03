package com.example.crowfunding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProyectoActivity extends AppCompatActivity {

    private String idProyecto; // ID del proyecto al que se está donando
    private FirebaseFirestore db;
    private FirebaseAuth auth;  // Agregar variable FirebaseAuth
    private String proyectoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_proyecto);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        idProyecto = getIntent().getStringExtra("proyectoId");


        // Verifica que el ID del proyecto no sea nulo
        if (proyectoId == null) {
            Toast.makeText(this, "Error: Proyecto no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar componentes de la vista
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        EditText editComentario = findViewById(R.id.editComentario);
        Button btnCalificar = findViewById(R.id.btnCalificar);

        btnCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ProyectoActivity", "Botón calificar presionado");
                Toast.makeText(ProyectoActivity.this, "Botón calificar presionado", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void agregarComentarioYCalificacion(String comentario, int calificacion, String idUser) {
        Log.d("AgregarComentario", "Agregando comentario y calificación al proyecto");

        // Crear un HashMap para el comentario
        Map<String, Object> comentarioData = new HashMap<>();
        comentarioData.put("comentario", comentario);
        comentarioData.put("calificacion", calificacion);
        comentarioData.put("idUser", idUser); // Agregar idUser

        // Agregar el comentario a la subcolección "Comentarios"
        db.collection("proyectos").document(idProyecto)
                .collection("Comentarios") // Acceder a la subcolección "Comentarios"
                .add(comentarioData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("AgregarComentario", "Comentario agregado con ID: " + documentReference.getId());

                    // Actualizar la calificación promedio del proyecto
                    actualizarCalificacionPromedio(calificacion);

                    // Mostrar un mensaje de éxito
                    Toast.makeText(ProyectoActivity.this, "Comentario y calificación agregados exitosamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("AgregarComentario", "Error al agregar comentario: " + e.getMessage(), e);
                    Toast.makeText(ProyectoActivity.this, "Error al agregar comentario", Toast.LENGTH_SHORT).show();
                });
    }

    private void actualizarCalificacionPromedio(int calificacion) {
        Log.d("ActualizarCalificacion", "Actualizando la calificación promedio del proyecto");

        // Aquí debes implementar la lógica para calcular la calificación promedio real.
        // Por simplicidad, vamos a suponer que simplemente se establece la calificación actual.

        Map<String, Object> updates = new HashMap<>();
        updates.put("calificacionPromedio", calificacion); // Aquí se asigna la calificación directamente

        // Actualizar la calificación promedio del proyecto
        db.collection("proyectos").document(idProyecto)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d("ActualizarCalificacion", "Calificación promedio actualizada con éxito");
                    // Mensaje de éxito ya mostrado al agregar el comentario
                })
                .addOnFailureListener(e -> {
                    Log.e("ActualizarCalificacion", "Error al actualizar la calificación promedio: " + e.getMessage(), e);
                    Toast.makeText(ProyectoActivity.this, "Error al actualizar la calificación promedio", Toast.LENGTH_SHORT).show();
                });
    }
}
