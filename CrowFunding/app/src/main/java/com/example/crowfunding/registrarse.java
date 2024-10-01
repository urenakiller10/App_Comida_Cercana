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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registrarse extends AppCompatActivity {

    // Instancia de FirebaseAuth para manejar la autenticación
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Elementos del layout para ingresar datos
    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private EditText nameEditText, cedulaEditText, phoneEditText, initialMoneyEditText;
    private Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);


        setContentView(R.layout.view_registrarse);

        // Inicializar la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referenciar los elementos del layout (los campos de texto y el botón)
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register_button);
        nameEditText = findViewById(R.id.name);
        cedulaEditText = findViewById(R.id.cedula);
        phoneEditText = findViewById(R.id.phone);
        initialMoneyEditText = findViewById(R.id.initial_money);

        // Listener para el botón de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    // Función para registrar al usuario en Firebase
    private void registerUser() {
        // Obtener los valores de los campos
        String name = nameEditText.getText().toString().trim();
        String cedula = cedulaEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String initialMoney = initialMoneyEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Verificar que todos los campos estén llenos
        if (name.isEmpty() || cedula.isEmpty() || phone.isEmpty() || email.isEmpty() || initialMoney.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(registrarse.this, "Por favor, complete todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        // Verificar que el email termine en @estudiantec.cr
        if (!email.endsWith("@estudiantec.cr")) {
            Toast.makeText(registrarse.this, "El correo debe terminar en @estudiantec.cr", Toast.LENGTH_LONG).show();
            return;
        }

        // Verificar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si todo es válido, proceder con el registro en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            String userId = mAuth.getCurrentUser().getUid();

                            // Guardar la información adicional en Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("cedula", cedula);
                            userData.put("phone", phone);
                            userData.put("email", email);
                            userData.put("initialMoney", initialMoney);

                            db.collection("users").document(userId)
                                    .set(userData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Mostrar mensaje de registro exitoso
                                                Toast.makeText(registrarse.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                                                // Redirigir a la pantalla de inicio de sesión
                                                Intent intent = new Intent(registrarse.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();  // Finaliza la actividad actual para evitar volver con el botón "atrás"
                                            } else {
                                                // Manejo de error si falla al guardar los datos
                                                Toast.makeText(registrarse.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Si ocurre un error en el registro
                            Toast.makeText(registrarse.this, "Error de registro: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



}
