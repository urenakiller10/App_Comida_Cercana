package com.example.crowfunding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class crearProyecto extends AppCompatActivity {

    private EditText nombreProyecto;
    private EditText descripcionProyecto;
    private EditText fechaLimite;
    private EditText objetivoFinanciacion;
    private Spinner spinnerCategoria;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_crearproyecto);

        // Inicializar los componentes
        nombreProyecto = findViewById(R.id.nombre_proyecto);
        descripcionProyecto = findViewById(R.id.descripcion_proyecto);
        fechaLimite = findViewById(R.id.fecha_limite);
        objetivoFinanciacion = findViewById(R.id.objetivo_financiacion);
        spinnerCategoria = findViewById(R.id.spinner_categoria);

        // Configurar el Spinner para categorías
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Configurar el botón para crear el proyecto
        Button crearProyectoButton = findViewById(R.id.btn_crear_proyecto);
        crearProyectoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los valores de los campos
                FirebaseAuth auth = FirebaseAuth.getInstance();

                String idUser = auth.getCurrentUser().getUid();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String fechaCreacion = sdf.format(Calendar.getInstance().getTime());
                String nombre = nombreProyecto.getText().toString().trim();
                String descripcion = descripcionProyecto.getText().toString().trim();
                String fecha = fechaLimite.getText().toString().trim();
                String objetivo = objetivoFinanciacion.getText().toString().trim();
                String categoria = spinnerCategoria.getSelectedItem().toString();

                Log.d("CrearProyecto", "Botón presionado");

                if (!nombre.isEmpty() && !descripcion.isEmpty() && !fecha.isEmpty() && !objetivo.isEmpty() && !categoria.isEmpty()) {
                    // Crear un nuevo objeto Proyecto
                    Proyecto proyecto = new Proyecto(idUser, nombre, descripcion, fechaCreacion, fecha, objetivo, categoria, 0);

                    Log.d("CrearProyecto", "Datos de proyecto obtenidos correctamente");

                    // Guardar el proyecto en Firestore
                    db.collection("proyectos").add(proyecto)
                            .addOnSuccessListener(documentReference -> {
                                String proyectoId = documentReference.getId(); // Obtener el ID del documento
                                proyecto.setIdProyecto(proyectoId); // Establecer el ID del proyecto

                                // Guardar el proyecto de nuevo con el ID actualizado
                                db.collection("proyectos").document(proyectoId).set(proyecto)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("CrearProyecto", "Proyecto creado con ID: " + proyectoId);
                                            Toast.makeText(crearProyecto.this, "Proyecto creado exitosamente", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(crearProyecto.this, MainScreenActivity.class);
                                            startActivity(intent);

                                            // Obtener el email guardado
                                            String email = SessionManager.getEmail();



                                            EmailSender emailSender = new EmailSender("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");
                                            emailSender.enviarCorreo(email, "Proyecto creado exitosamente", "Su proyecto ha sido creado de manera exitosa en la plataforma de crowdfunding.");







                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("CrearProyecto", "Error al guardar el ID del proyecto", e);
                                            Toast.makeText(crearProyecto.this, "Error al guardar el proyecto", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("CrearProyecto", "Error al crear el proyecto", e);
                                Toast.makeText(crearProyecto.this, "Error al crear el proyecto", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(crearProyecto.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
