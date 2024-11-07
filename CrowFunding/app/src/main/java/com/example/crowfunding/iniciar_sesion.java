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
        setContentView(R.layout.view_iniciar_sesion);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Referenciar los elementos del layout
        emailEditText = findViewById(R.id.etCorreo);
        passwordEditText = findViewById(R.id.etContrasena);
        loginButton = findViewById(R.id.btnIniciarSesion);
        registerLink = findViewById(R.id.btnRegistrarse);

        // Configurar el listener para el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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

                            // Verificar si el correo es el de administrador
                            if (email.equals("andurena@estudiantec.cr")) {
                                // Redirigir a la ventana view_home_admin (HomeAdmin)
                                Intent intent = new Intent(iniciar_sesion.this, HomeAdmin.class);
                                startActivity(intent);
                            } else {
                                // Redirigir a la pantalla principal (MainScreenActivity)
                                Intent intent = new Intent(iniciar_sesion.this, MainScreenActivity.class);
                                startActivity(intent);
                                finish();
                            }


                            SessionManager.setEmail(email);


                        } else {
                            // Si ocurre un error, mostrar un mensaje
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error desconocido";
                            Toast.makeText(iniciar_sesion.this, "Error de inicio de sesión: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
