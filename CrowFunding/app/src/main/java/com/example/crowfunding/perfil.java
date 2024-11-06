package com.example.crowfunding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class perfil extends AppCompatActivity {

    // Referencias de Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Elementos de la interfaz para mostrar los datos del perfil
    private TextView tvNombre, tvCorreo, tvAreaTrabajo, tvDineroInicial;
    private Button btnEditarPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_perfil); // Asociar el archivo XML

        // Inicializar FirebaseAuth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referenciar los elementos del layout
        tvNombre = findViewById(R.id.tvNombre);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvAreaTrabajo = findViewById(R.id.tvAreaTrabajo);
        tvDineroInicial = findViewById(R.id.tvDineroInicial);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);

        // Configurar listener para el botón de editar perfil
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de edición de perfil
                Intent intent = new Intent(perfil.this, EditarPerfil.class);
                startActivity(intent);
            }
        });

        // Cargar datos del usuario desde Firebase
        loadUserProfile();
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Consultar el documento del usuario en Firestore
            db.collection("users").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Asignar los datos recuperados a los TextViews
                                tvNombre.setText(document.getString("name"));
                                tvCorreo.setText(document.getString("email"));
                                tvAreaTrabajo.setText(document.getString("AreaDeTrabajo"));
                                tvDineroInicial.setText(String.valueOf(document.getDouble("initialMoney")));
                            } else {
                                Toast.makeText(perfil.this, "Documento no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(perfil.this, "Error al cargar datos: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(perfil.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile(); // Recargar datos del perfil cuando se vuelve a esta actividad
    }
}
