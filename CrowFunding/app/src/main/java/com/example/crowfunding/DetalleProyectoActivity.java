package com.example.crowfunding;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;

public class DetalleProyectoActivity extends AppCompatActivity {
    private TextView textNombre, textDescripcion, textFechaLimite, textObjetivo, textFechaCreacion;
    private EditText etDineroDonacion; // Campo de entrada para la donación
    private FirebaseFirestore db;
    private FirebaseAuth auth;  // Agregar variable FirebaseAuth
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
        textFechaCreacion = findViewById(R.id.textFechaCreacion);

        // Inicializa Firestore y FirebaseAuth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();  // Inicializar FirebaseAuth

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
        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();  // Obtener el UID del usuario autenticado

        // Primero intenta realizar la donación restando el monto del saldo del usuario
        generarDonacion(idUser, donacion);
    }

    private void generarDonacion(String idUser, float donacion) {
        DocumentReference userRef = db.collection("users").document(idUser);

        // Obtener el dinero inicial del usuario
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    float initialMoney = snapshot.getDouble("initialMoney").floatValue();

                    // Verifica si el usuario tiene suficiente dinero
                    if (initialMoney >= donacion) {
                        // Actualiza el dinero del usuario restando la donación
                        float nuevoSaldo = initialMoney - donacion;
                        userRef.update("initialMoney", nuevoSaldo)
                                .addOnSuccessListener(aVoid -> {
                                    // Guarda los detalles de la donación en la colección "donaciones"
                                    DocumentReference donacionRef = db.collection("donaciones").document();
                                    Donacion nuevaDonacion = new Donacion(idUser, proyectoId, donacion, Calendar.getInstance().getTime());
                                    donacionRef.set(nuevaDonacion)
                                            .addOnSuccessListener(aVoid1 -> {
                                                // Llama a donarProyecto para actualizar el dinero en el proyecto después de registrar la donación
                                                donarProyecto(donacion);
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(DetalleProyectoActivity.this, "Error al registrar la donación", Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(DetalleProyectoActivity.this, "Error al actualizar el saldo del usuario", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(DetalleProyectoActivity.this, "Saldo insuficiente para realizar la donación", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(DetalleProyectoActivity.this, "Error en la conexión con la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void donarProyecto(float donacion) {
        // Referencia al proyecto en Firestore
        DocumentReference proyectoRef = db.collection("proyectos").document(proyectoId);

        // Obtén los datos del proyecto para actualizar
        proyectoRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                float dineroDonado = snapshot.getDouble("dineroActual").floatValue();
                // Aquí se obtiene el dinero donado actual
                float nuevoTotal = dineroDonado + donacion; // Sumar la nueva donación
                //Log.d("Proyecto", "Dinero actual: " + dineroDonado + ", Nueva donación: " + donacion + ", Nuevo total: " + nuevoTotal);

                // Actualiza el dinero en el proyecto
                proyectoRef.update("dineroActual", nuevoTotal) // Asegúrate de actualizar "dineroDonado"
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(DetalleProyectoActivity.this, "Donación realizada con éxito", Toast.LENGTH_SHORT).show();
                            etDineroDonacion.setText(""); // Limpia el campo de entrada
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(DetalleProyectoActivity.this, "Error al realizar la donación", Toast.LENGTH_SHORT).show();
                        });

            } else {
                Toast.makeText(DetalleProyectoActivity.this, "Error en la conexión con la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }






}
