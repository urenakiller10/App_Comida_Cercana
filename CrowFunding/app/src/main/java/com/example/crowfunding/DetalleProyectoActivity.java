package com.example.crowfunding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DetalleProyectoActivity extends AppCompatActivity {
    private static final String TAG = "DetalleProyectoActivity";

    // Campos de donación

    private EditText etDineroDonacion;
    private TextView textNombre, textDescripcion, textFechaLimite, textObjetivo, textFechaCreacion;
    private String proyectoId;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // Campos de comentarios y calificación (estrellas y campo para comentar)

    private RatingBar ratingBar;
    private EditText editComentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_proyecto);

        // Inicializar Firestore y FirebaseAuth

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Obtén el ID del proyecto desde el Intent

        proyectoId = getIntent().getStringExtra("proyectoId");
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
        etDineroDonacion = findViewById(R.id.etDineroDonacion); // Para donación
        textFechaCreacion = findViewById(R.id.textFechaCreacion);

        // Configurar el botón de donación

        Button btnDonar = findViewById(R.id.btnDonar);
        btnDonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarDonacion();
            }
        });

        // Inicializar campos para comentarios y calificación (5 estrellas y el campo para escribir)

        ratingBar = findViewById(R.id.ratingBar);
        editComentario = findViewById(R.id.editComentario);
        Button btnCalificar = findViewById(R.id.btnCalificar);
        btnCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int calificacion = (int) ratingBar.getRating();
                String comentario = editComentario.getText().toString();
                agregarComentarioYCalificacion(comentario, calificacion);
            }
        });

        // Cargar datos del proyecto desde Firestore
        cargarDatosProyecto();
    }

    // Método para cargar los datos del proyecto en la ventana de detalles, según el proyecto que se tocó

    private void cargarDatosProyecto() {
        DocumentReference proyectoRef = db.collection("proyectos").document(proyectoId);
        proyectoRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    Proyecto proyecto = snapshot.toObject(Proyecto.class);
                    if (proyecto != null) {
                        textNombre.setText(proyecto.getNombre());
                        textFechaCreacion.setText(proyecto.getFechaCreacion());
                        textDescripcion.setText(proyecto.getDescripcion());
                        textFechaLimite.setText(proyecto.getFechaLimite());
                        textObjetivo.setText(proyecto.getObjetivoFinanciacion());
                    } else {
                        Toast.makeText(this, "Error al cargar el proyecto", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Proyecto no encontrado", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error en la conexión con la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }



    //---------------------- Sección de código para realizar donación-------------------------//

    private void realizarDonacion() {
        String donacionStr = etDineroDonacion.getText().toString();
        if (donacionStr.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un monto de donación", Toast.LENGTH_SHORT).show();
            return;
        }

        float donacion = Float.parseFloat(donacionStr);
        String idUser = auth.getCurrentUser().getUid();
        generarDonacion(idUser, donacion);
    }

    private void generarDonacion(String idUser, float donacion) {
        DocumentReference userRef = db.collection("users").document(idUser);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    float initialMoney = snapshot.getDouble("initialMoney").floatValue();
                    if (initialMoney >= donacion) {
                        float nuevoSaldo = initialMoney - donacion;
                        userRef.update("initialMoney", nuevoSaldo)
                                .addOnSuccessListener(aVoid -> {
                                    DocumentReference donacionRef = db.collection("donaciones").document();
                                    Donacion nuevaDonacion = new Donacion(idUser, proyectoId, donacion, Calendar.getInstance().getTime());
                                    donacionRef.set(nuevaDonacion)
                                            .addOnSuccessListener(aVoid1 -> {
                                                donarProyecto(donacion);
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(this, "Error al registrar la donación", Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error al actualizar el saldo del usuario", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Saldo insuficiente para realizar la donación", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "Error en la conexión con la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void donarProyecto(float donacion) {
        DocumentReference proyectoRef = db.collection("proyectos").document(proyectoId);
        proyectoRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                float dineroDonado = snapshot.getDouble("dineroActual").floatValue();
                float nuevoTotal = dineroDonado + donacion;
                proyectoRef.update("dineroActual", nuevoTotal)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Donación realizada con éxito", Toast.LENGTH_SHORT).show();
                            etDineroDonacion.setText("");
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Error al realizar la donación", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Error en la conexión con la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }





    // ----------------------------Sección de código para agregar comentario y calificación-----------------------------//

    private void agregarComentarioYCalificacion(String comentario, int calificacion) {
        Map<String, Object> comentarioData = new HashMap<>();
        comentarioData.put("comentario", comentario);
        comentarioData.put("calificacion", calificacion);
        comentarioData.put("idUser", auth.getCurrentUser().getUid());

        db.collection("proyectos").document(proyectoId)
                .collection("Comentarios")
                .add(comentarioData)
                .addOnSuccessListener(documentReference -> {
                    actualizarCalificacionPromedio(calificacion);
                    Toast.makeText(this, "Comentario y calificación agregados exitosamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al agregar comentario", Toast.LENGTH_SHORT).show();
                });
    }

    private void actualizarCalificacionPromedio(int calificacion) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("calificacionPromedio", calificacion);

        db.collection("proyectos").document(proyectoId)
                .update(updates)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Calificación promedio actualizada con éxito"))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al actualizar la calificación promedio", Toast.LENGTH_SHORT).show();
                });
    }
}
