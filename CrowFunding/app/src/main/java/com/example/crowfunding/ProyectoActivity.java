package com.example.crowfunding;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProyectoActivity extends AppCompatActivity {

    private static final String TAG = "ProyectoActivity";
    private String idProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_proyecto);

        // Inicializa el idProyecto
        idProyecto = "Mpfn85vcIT4LXqDgqq02";

        // Referencia al botón
        Button btnCalificar = findViewById(R.id.btnCalificar);

        // Establecer el OnClickListener para el botón
        btnCalificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Aquí puedes capturar los datos de calificación y comentario
                int calificacion = 4;
                String comentario = "Buen proyecto";
                String usuarioID = "6PVMIDRLpWPh0UcbMFWjmAkqQVV2";
                // Llama al método para agregar el comentario
                agregarComentario(idProyecto, calificacion, comentario, usuarioID);
            }
        });
    }

    private void agregarComentario(String idProyecto, int calificacion, String comentario, String usuarioID) {
        DatabaseReference projectRef = FirebaseDatabase.getInstance().getReference("proyectos").child(idProyecto);

        // Crear un objeto Comentario
        Comentario nuevoComentario = new Comentario(calificacion, comentario, usuarioID);

        // Guardar el comentario y actualizar la calificación promedio
        projectRef.child("comentario").setValue(nuevoComentario.comentario)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Comentario añadido con éxito");

                        // Actualiza la calificación promedio
                        projectRef.child("calificacionPromedio").setValue(calificacion)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "Calificación promedio actualizada con éxito");
                                    } else {
                                        Log.e(TAG, "Error al actualizar calificación promedio", task1.getException());
                                    }
                                });
                    } else {
                        Log.e(TAG, "Error al añadir comentario: " + task.getException().getMessage());
                    }
                });
    }

    // Clase Comentario
    public static class Comentario {
        public int calificacion;
        public String comentario;
        public String usuarioID;

        public Comentario(int calificacion, String comentario, String usuarioID) {
            this.calificacion = calificacion;
            this.comentario = comentario;
            this.usuarioID = usuarioID;
        }
    }
}
