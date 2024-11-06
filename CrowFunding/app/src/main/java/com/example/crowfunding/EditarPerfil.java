package com.example.crowfunding;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarPerfil extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Referencias a los campos de edición
    private EditText etNombre, etAreaTrabajo, etDineroInicial;
    private Button btnGuardarCambios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_editar_perfil);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referenciar los campos del layout
        etNombre = findViewById(R.id.etNombreEditar);
        etAreaTrabajo = findViewById(R.id.etAreaTrabajoEditarr);
        etDineroInicial = findViewById(R.id.etDineroInicialEditarr);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambioss);

        // Configurar el botón para guardar cambios
        btnGuardarCambios.setOnClickListener(v -> saveChangesAndRedirect());
    }

    private void saveChangesAndRedirect() {
        // Obtener los datos ingresados
        String nombre = etNombre.getText().toString().trim();
        String areaTrabajo = etAreaTrabajo.getText().toString().trim();
        String dineroInicialStr = etDineroInicial.getText().toString().trim();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Preparar los datos a actualizar
            Map<String, Object> userData = new HashMap<>();
            if (!TextUtils.isEmpty(nombre)) userData.put("name", nombre);
            if (!TextUtils.isEmpty(areaTrabajo)) userData.put("AreaDeTrabajo", areaTrabajo);
            if (!TextUtils.isEmpty(dineroInicialStr)) {
                try {
                    double dineroInicial = Double.parseDouble(dineroInicialStr);
                    userData.put("initialMoney", dineroInicial);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Ingrese una cantidad válida de dinero inicial", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Actualizar los datos en Firestore
            db.collection("users").document(userId)
                    .update(userData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditarPerfil.this, "Cambios guardados", Toast.LENGTH_SHORT).show();

                            // Redirigir a la actividad de perfil y cerrar esta actividad
                            Intent intent = new Intent(EditarPerfil.this, perfil.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(EditarPerfil.this, "Error al guardar cambios: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}
