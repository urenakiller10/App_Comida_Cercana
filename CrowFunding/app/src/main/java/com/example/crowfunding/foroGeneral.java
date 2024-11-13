package com.example.crowfunding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class foroGeneral extends AppCompatActivity {

    private RecyclerView recyclerViewForum;
    private EditText forumInputText;
    private Button forumSendButton;

    private comentarioAdapter comentarioAdapter;
    private List<comentario> comentarioList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_forogeneral);

        // Inicializar FirebaseAuth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() == null) {
            // Si no hay usuario autenticado, puedes redirigirlo a la pantalla de login o mostrar un mensaje
            Toast.makeText(this, "No has iniciado sesión", Toast.LENGTH_SHORT).show();
            // Puedes redirigir a la pantalla de login
            startActivity(new Intent(foroGeneral.this, iniciar_sesion.class));
            finish();
        }

        // Configurar RecyclerView
        recyclerViewForum = findViewById(R.id.recyclerView_forum);
        recyclerViewForum.setLayoutManager(new LinearLayoutManager(this));
        comentarioList = new ArrayList<>();
        comentarioAdapter = new comentarioAdapter(this, comentarioList);
        recyclerViewForum.setAdapter(comentarioAdapter);

        // Inicializar campo de entrada y botón de enviar
        forumInputText = findViewById(R.id.forum_input_text);
        forumSendButton = findViewById(R.id.forum_send_button);

        // Configurar el botón de enviar
        forumSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoComentario = forumInputText.getText().toString().trim();
                if (!textoComentario.isEmpty()) {
                    enviarComentario(textoComentario);
                    forumInputText.setText(""); // Limpiar campo de texto después de enviar
                } else {
                    Toast.makeText(foroGeneral.this, "Escribe un comentario", Toast.LENGTH_SHORT).show();
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
                            if ("0".equals(comentario.getIdProyecto())) {
                                comentarioList.add(comentario);
                            }
                        }
                        comentarioAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(foroGeneral.this, "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(foroGeneral.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void enviarComentario(String textoComentario) {
        String userId = mAuth.getCurrentUser().getUid();
        Timestamp fechaHora = Timestamp.now();
        comentario nuevoComentario = new comentario(null, textoComentario, userId, fechaHora, "0"); // ID se establece después

        db.collection("comentariosForos")
                .add(nuevoComentario)
                .addOnSuccessListener(documentReference -> {
                    String generatedId = documentReference.getId();
                    documentReference.update("id", generatedId);
                    nuevoComentario.setId(generatedId); // Asignar el ID generado a nuevoComentario
                    comentarioList.add(nuevoComentario);
                    comentarioAdapter.notifyItemInserted(comentarioList.size() - 1);
                    recyclerViewForum.scrollToPosition(comentarioList.size() - 1);
                    Toast.makeText(foroGeneral.this, "Comentario enviado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(foroGeneral.this, "Error al enviar comentario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}