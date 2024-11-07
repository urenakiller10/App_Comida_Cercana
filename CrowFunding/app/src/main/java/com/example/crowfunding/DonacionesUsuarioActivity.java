package com.example.crowfunding;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crowfunding.Donacion;
import com.example.crowfunding.DonacionAdapter;
import com.example.crowfunding.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class DonacionesUsuarioActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private DonacionAdapter donacionAdapter;
    private List<Donacion> donacionesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donaciones_usuario);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerViewDonaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        donacionesList = new ArrayList<>();
        donacionAdapter = new DonacionAdapter(donacionesList, this);
        recyclerView.setAdapter(donacionAdapter);

        String userId = auth.getCurrentUser().getUid();
        loadDonaciones(userId);
    }

    private void loadDonaciones(String userId) {
        db.collection("donaciones")
                .whereEqualTo("idUser", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        donacionesList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Donacion donacion = document.toObject(Donacion.class);
                            donacionesList.add(donacion);
                        }
                        donacionAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(DonacionesUsuarioActivity.this, "Error al cargar las donaciones", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DonacionesUsuarioActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
