package com.example.crowfunding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class foroProyecto extends AppCompatActivity {
    private RecyclerView recyclerViewForum;
    private EditText forumInputText;
    private Button forumSendButton;
    private TextView tvTituloForo;

    private comentarioAdapter comentarioAdapter;
    private List<comentario> comentarioList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String proyectoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_forogeneral);

        // Inicializar FirebaseAuth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        proyectoId = getIntent().getStringExtra("proyectoId");

        if (mAuth.getCurrentUser() == null) {
            // Si no hay usuario autenticado, puedes redirigirlo a la pantalla de login o mostrar un mensaje
            Toast.makeText(this, "No has iniciado sesión", Toast.LENGTH_SHORT).show();
            // Puedes redirigir a la pantalla de login
            startActivity(new Intent(foroProyecto.this, iniciar_sesion.class));
            finish();
        }

        // Configurar RecyclerView
        recyclerViewForum = findViewById(R.id.recyclerView_forum);
        recyclerViewForum.setLayoutManager(new LinearLayoutManager(this));
        comentarioList = new ArrayList<>();
        comentarioAdapter = new comentarioAdapter(this, comentarioList);
        recyclerViewForum.setAdapter(comentarioAdapter);

        // Inicializar campo de entrada y botón de enviar
        tvTituloForo = findViewById(R.id.tvTituloForo);
        forumInputText = findViewById(R.id.forum_input_text);
        forumSendButton = findViewById(R.id.forum_send_button);

        db.collection("proyectos")
                .document(proyectoId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String nombreProyecto = document.getString("nombre");
                            tvTituloForo.setText("Foro de " + nombreProyecto);
                        } else {
                            Toast.makeText(foroProyecto.this, "Proyecto no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(foroProyecto.this, "Error al obtener proyecto", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(foroProyecto.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


        // Configurar el botón de enviar
        forumSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoComentario = forumInputText.getText().toString().trim();
                if (!textoComentario.isEmpty()) {
                    enviarComentario(textoComentario);
                    forumInputText.setText(""); // Limpiar campo de texto después de enviar
                } else {
                    Toast.makeText(foroProyecto.this, "Escribe un comentario", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cargarComentarios();
    }

    private void cargarComentarios() {
        db.collection("comentariosForos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        comentarioList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            comentario comentario = document.toObject(comentario.class);
                            if (proyectoId.equals(comentario.getIdProyecto())) {
                                comentarioList.add(comentario);
                            }
                        }
                        comentarioAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(foroProyecto.this, "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(foroProyecto.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void enviarComentario(String textoComentario) {
        String userId = mAuth.getCurrentUser().getUid();
        Timestamp fechaHora = Timestamp.now();
        comentario nuevoComentario = new comentario(null, textoComentario, userId, fechaHora, proyectoId); // ID se establece después

        db.collection("comentariosForos")
                .add(nuevoComentario)
                .addOnSuccessListener(documentReference -> {
                    String generatedId = documentReference.getId();
                    documentReference.update("id", generatedId);
                    nuevoComentario.setId(generatedId); // Asignar el ID generado a nuevoComentario
                    comentarioList.add(nuevoComentario);
                    comentarioAdapter.notifyItemInserted(comentarioList.size() - 1);
                    recyclerViewForum.scrollToPosition(comentarioList.size() - 1);
                    Toast.makeText(foroProyecto.this, "Comentario enviado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(foroProyecto.this, "Error al enviar comentario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}