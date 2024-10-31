package com.example.crowfunding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class cartera extends AppCompatActivity {

    private EditText saldoEditText, traerDineroEditText;
    private Button traerDineroButton, historialDonacionesButton;
    private double saldo = 0.0; // Mantener saldo como double
    private int cantidadDonaciones = 0;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_cartera);

        // Inicializar FirebaseAuth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();  // Obtener el ID del usuario actual

        // Referenciar los elementos del layout
        saldoEditText = findViewById(R.id.saldo_editText);
        traerDineroEditText = findViewById(R.id.traer_dinero_editText);
        traerDineroButton = findViewById(R.id.traer_dinero_button);
        historialDonacionesButton = findViewById(R.id.historial_donaciones_button);

        // Obtener y mostrar el saldo inicial del usuario
        obtenerSaldoDesdeFirestore();

        // Acción para traer dinero
        traerDineroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidadStr = traerDineroEditText.getText().toString();
                if (!cantidadStr.isEmpty()) {
                    double cantidad = Double.parseDouble(cantidadStr); // Convertir a double
                    saldo += cantidad; // Actualizar saldo localmente
                    actualizarCampos();
                    actualizarSaldoEnFirestore(saldo); // Actualizar saldo en Firestore
                } else {
                    Toast.makeText(cartera.this, "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Acción para historial de donaciones
        historialDonacionesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir a la pantalla de historial de donaciones
                // Intent intent = new Intent(cartera.this, donaciones.class);
                // startActivity(intent);
            }
        });
    }

    // Obtener el saldo desde Firestore y mostrarlo
    private void obtenerSaldoDesdeFirestore() {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Obtener el campo initialMoney de Firestore
                        saldo = document.getDouble("initialMoney");
                        actualizarCampos(); // Actualizar el campo de saldo en la interfaz
                    } else {
                        Toast.makeText(cartera.this, "El documento no existe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(cartera.this, "Error al obtener el saldo: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Actualizar el saldo en Firestore cuando el usuario agregue dinero
    private void actualizarSaldoEnFirestore(double nuevoSaldo) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.update("initialMoney", nuevoSaldo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(cartera.this, "Saldo actualizado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(cartera.this, "Error al actualizar el saldo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Actualizar los campos de la interfaz con el saldo y la cantidad de donaciones
    private void actualizarCampos() {
        saldoEditText.setText(String.format("$%.2f", saldo)); // Formatear el saldo como moneda
        // donacionesEditText.setText(String.valueOf(cantidadDonaciones)); // Si lo necesitas, asegúrate de incluir el EditText correspondiente en el XML
    }
}