package com.example.crowfunding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        setContentView(R.layout.view_iniciar_sesion);

        // Referenciar los elementos del layout
        loginButton = findViewById(R.id.btnIniciarSesion);
        registerLink = findViewById(R.id.btnRegistrarse);

        // Listener para el botón de registro
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la pantalla de registro
                Intent intent = new Intent(MainActivity.this, registrarse.class);
                startActivity(intent);
            }
        });

        // Listener para el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la pantalla de iniciar sesión
                Intent intent = new Intent(MainActivity.this, iniciar_sesion.class);
                startActivity(intent);
            }
        });
    }
}
