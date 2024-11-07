package com.example.crowfunding;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class comentarioAdapter extends RecyclerView.Adapter<comentarioAdapter.ComentarioViewHolder> {

    private Context context;
    private List<comentario> listaComentarios;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public comentarioAdapter(Context context, List<comentario> listaComentarios) {
        this.context = context;
        this.listaComentarios = listaComentarios;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();  // Inicializa FirebaseAuth aquí
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comentario, parent, false);
        return new ComentarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        comentario comentarioActual = listaComentarios.get(position);
        String userId = comentarioActual.getUserId();

        if (db != null) {
            CollectionReference comentariosRef = db.collection("comentarios");
            // El resto de tu código para obtener datos
        } else {
            Log.e("Firestore", "Firestore no está inicializado");
        }

        // Obtener el nombre del usuario de la colección "users" usando el userId
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombreUsuario = documentSnapshot.getString("name");
                        holder.comentarioTextView.setText(nombreUsuario + ": " + comentarioActual.getTexto());

                        // Configurar listeners para los botones
                        setupLikeDislikeButtons(holder, comentarioActual, nombreUsuario);
                    } else {
                        holder.comentarioTextView.setText("Desconocido: " + comentarioActual.getTexto());
                    }
                })
                .addOnFailureListener(e -> {
                    holder.comentarioTextView.setText("Error al cargar el usuario");
                });

        db.collection("comentariosForos").document(comentarioActual.getId()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        int likes = documentSnapshot.getLong("likes").intValue();
                        int dislikes = documentSnapshot.getLong("dislikes").intValue();
                        comentarioActual.setLikes(likes);
                        comentarioActual.setDislikes(dislikes);

                        holder.like_count_text_view.setText("Likes: " + likes);
                        holder.dislike_count_text_view.setText("Dislikes: " + dislikes);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al obtener likes y dislikes", e);
                });

        holder.fechaHoraTextView.setText(comentarioActual.getFechaHora().toDate().toString());
    }

    private void setupLikeDislikeButtons(ComentarioViewHolder holder, comentario comentarioActual, String nombreUsuario) {
        // Configurar listener para el botón de like
        holder.likeButton.setOnClickListener(v -> {
            int newLikes = comentarioActual.getLikes() + 1;
            comentarioActual.setLikes(newLikes);
            db.collection("comentariosForos").document(comentarioActual.getId())
                    .update("likes", newLikes)
                    .addOnSuccessListener(aVoid -> {
                        holder.like_count_text_view.setText("Likes: " + comentarioActual.getLikes());
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error al dar like: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        // Configurar listener para el botón de dislike
        holder.dislikeButton.setOnClickListener(v -> {
            int newDislikes = comentarioActual.getDislikes() + 1;
            comentarioActual.setDislikes(newDislikes);
            db.collection("comentariosForos").document(comentarioActual.getId())
                    .update("dislikes", newDislikes)
                    .addOnSuccessListener(aVoid -> {
                        holder.dislike_count_text_view.setText("Dislikes: " + comentarioActual.getDislikes());
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error al dar dislike: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        holder.responderButton.setOnClickListener(v -> {
            abrirDialogoComentario(comentarioActual, nombreUsuario);
        });
    }

    private void abrirDialogoComentario(comentario comentarioActual, String nombreUsuario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_comentario, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        TextView textoComentario = dialogView.findViewById(R.id.textoComentario);
        TextView likesDislikesTextView = dialogView.findViewById(R.id.likesDislikesTextView);
        EditText campoRespuesta = dialogView.findViewById(R.id.campoRespuesta);
        Button botonEnviarRespuesta = dialogView.findViewById(R.id.botonEnviarRespuesta);
        RecyclerView recyclerViewRespuestas = dialogView.findViewById(R.id.recyclerViewRespuestas);

        // Mostrar el texto del comentario
        textoComentario.setText(nombreUsuario + ": " + comentarioActual.getTexto());
        likesDislikesTextView.setText("Likes: " + comentarioActual.getLikes() + " Dislikes: " + comentarioActual.getDislikes());

        // Configurar el RecyclerView para mostrar respuestas
        List<respuesta> listaRespuestas = new ArrayList<>();
        respuestaAdapter respuestaAdapter = new respuestaAdapter(listaRespuestas);
        recyclerViewRespuestas.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewRespuestas.setAdapter(respuestaAdapter);

        // Cargar respuestas desde Firebase
        cargarRespuestas(comentarioActual.getId(), listaRespuestas, respuestaAdapter);

        // Configurar el botón para enviar la respuesta
        botonEnviarRespuesta.setOnClickListener(v -> {
            String respuestaTexto = campoRespuesta.getText().toString().trim();
            if (!respuestaTexto.isEmpty()) {
                enviarRespuesta(comentarioActual.getId(), respuestaTexto, respuestaAdapter, listaRespuestas);
                campoRespuesta.setText(""); // Limpiar el campo
            } else {
                Toast.makeText(context, "Por favor escribe una respuesta", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void cargarRespuestas(String comentarioId, List<respuesta> listaRespuestas, respuestaAdapter adapter) {
        db.collection("comentariosForoGeneral").document(comentarioId).collection("respuestas")
                .orderBy("fecha", Query.Direction.ASCENDING)  // Ordena por fecha ascendente
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d("Firebase", "No hay respuestas para este comentario.");
                        Toast.makeText(context, "No hay respuestas para este comentario.", Toast.LENGTH_SHORT).show();
                    } else {
                        listaRespuestas.clear(); // Limpiar la lista antes de agregar nuevas respuestas
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            String texto = doc.getString("texto");
                            String userId = doc.getString("userId");
                            Date fecha = doc.getDate("fecha");

                            // Consultar el nombre del usuario directamente aquí
                            db.collection("users").document(userId).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        String nombreUsuario;
                                        if (documentSnapshot.exists()) {
                                            nombreUsuario = documentSnapshot.getString("name");
                                        } else {
                                            nombreUsuario = "Usuario desconocido";
                                        }

                                        // Agregar la respuesta a la lista con el nombre de usuario obtenido
                                        listaRespuestas.add(new respuesta(comentarioId, texto, userId, fecha, nombreUsuario));

                                        // Notificar al adaptador después de agregar cada respuesta
                                        adapter.notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firebase", "Error al obtener nombre de usuario", e);
                                        listaRespuestas.add(new respuesta(comentarioId, texto, userId, fecha, "Error al obtener usuario"));
                                        adapter.notifyDataSetChanged();
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al cargar respuestas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Error al cargar respuestas", e);
                });
    }

    private List<respuesta> obtenerRespuestas(String comentarioId) {
        List<respuesta> respuestas = new ArrayList<>();
        db.collection("comentariosForoGeneral")
                .document(comentarioId)
                .collection("respuestas")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String texto = doc.getString("texto");
                        String userId = doc.getString("userId");
                        Date fecha = doc.getDate("fecha");
                        String nombreUsuario = doc.getString("nombreUsuario");
                        respuestas.add(new respuesta(comentarioId, texto, userId, fecha, nombreUsuario));
                    }

                    // Crear el adaptador después de cargar las respuestas
                    respuestaAdapter adapter = new respuestaAdapter(respuestas);
                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al cargar respuestas", Toast.LENGTH_SHORT).show();
                });
        return respuestas;
    }

    private void enviarRespuesta(String comentarioId, String respuestaTexto, respuestaAdapter adapter, List<respuesta> listaRespuestas) {
        // Obtener el userId del usuario autenticado
        String userId = mAuth.getCurrentUser().getUid();

        // Obtener el nombre del usuario
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombreUsuario = documentSnapshot.getString("name");

                        // Crear la nueva respuesta con los datos necesarios
                        Date fechaActual = new Date();
                        respuesta nuevaRespuesta = new respuesta(comentarioId, respuestaTexto, userId,  fechaActual, nombreUsuario);

                        // Guardar la respuesta en la subcolección "respuestas" dentro del comentario
                        db.collection("comentariosForoGeneral")
                                .document(comentarioId)
                                .collection("respuestas")
                                .add(nuevaRespuesta)
                                .addOnSuccessListener(documentReference -> {
                                    // Agregar la nueva respuesta a la lista local
                                    listaRespuestas.add(nuevaRespuesta);
                                    // Notificar al adaptador que los datos han cambiado
                                    adapter.notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error al agregar la respuesta", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(context, "Error al obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al obtener el nombre de usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder {
        TextView comentarioTextView, like_count_text_view, dislike_count_text_view;
        TextView fechaHoraTextView;
        Button likeButton, dislikeButton, responderButton;

        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            comentarioTextView = itemView.findViewById(R.id.texto_comentario);
            like_count_text_view = itemView.findViewById(R.id.like_count_text_view);
            dislike_count_text_view = itemView.findViewById(R.id.dislike_count_text_view);
            fechaHoraTextView = itemView.findViewById(R.id.fecha_hora_text_view);
            likeButton = itemView.findViewById(R.id.like_button);
            dislikeButton = itemView.findViewById(R.id.dislike_button);
            responderButton = itemView.findViewById(R.id.responder_button);
        }
    }
}