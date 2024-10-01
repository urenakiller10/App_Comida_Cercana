package com.example.crowfunding;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class iniciar_sesion extends AppCompatActivity {

    // Instancia de FirebaseAuth
    private FirebaseAuth mAuth;

    // Elementos del layout
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_iniciar_sesion);  // Asegúrate de que este sea el layout correcto

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Referenciar los elementos del layout
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);  // Asegúrate de que este id sea correcto
        registerLink = findViewById(R.id.register_link);

        // Configurar el listener para el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí agregamos la prueba básica con un Toast
                Toast.makeText(iniciar_sesion.this, "Botón de iniciar sesión presionado", Toast.LENGTH_SHORT).show();

                // Llamar al método para iniciar sesión
                loginUser();
            }
        });

        // Listener para el enlace de registro
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la pantalla de registro
                Intent intent = new Intent(iniciar_sesion.this, registrarse.class);
                startActivity(intent);
            }
        });
    }

    // Método para iniciar sesión
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Verificar que los campos no estén vacíos
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Intentar iniciar sesión con Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso
                            Toast.makeText(iniciar_sesion.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            // Redirigir a la pantalla principal
                            Intent intent = new Intent(iniciar_sesion.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Finaliza la actividad actual para evitar volver con el botón atrás
                        } else {
                            // Si ocurre un error, mostrar un mensaje
                            Toast.makeText(iniciar_sesion.this, "Error de inicio de sesión: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
