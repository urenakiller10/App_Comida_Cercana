package com.example.crowfunding;

import android.content.Context;
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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class comentarioAdapter extends RecyclerView.Adapter<comentarioAdapter.ComentarioViewHolder> {

    private Context context;
    private List<comentario> listaComentarios;
    private FirebaseFirestore db;

    public comentarioAdapter(Context context, List<comentario> listaComentarios) {
        this.context = context;
        this.listaComentarios = listaComentarios;
        this.db = FirebaseFirestore.getInstance(); // Instancia de Firestore
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

        // Aquí puedes establecer la fecha y hora del comentario
        holder.fechaHoraTextView.setText(comentarioActual.getFechaHora().toDate().toString());
    }

    private void setupLikeDislikeButtons(ComentarioViewHolder holder, comentario comentarioActual, String nombreUsuario) {
        // Configurar listener para el botón de like
        holder.likeButton.setOnClickListener(v -> {
            int newLikes = comentarioActual.getLikes() + 1;
            comentarioActual.setLikes(newLikes);
            db.collection("comentariosForoGeneral").document(comentarioActual.getId())
                    .update("likes", newLikes)
                    .addOnSuccessListener(aVoid -> {
                        holder.comentarioTextView.setText(nombreUsuario + ": " + comentarioActual.getTexto() + " (Likes: " + newLikes + ") " + " (Dislikes: " + comentarioActual.getDislikes() + ")" );
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error al dar like: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        // Configurar listener para el botón de dislike
        holder.dislikeButton.setOnClickListener(v -> {
            int newDislikes = comentarioActual.getDislikes() + 1;
            comentarioActual.setDislikes(newDislikes);
            db.collection("comentariosForoGeneral").document(comentarioActual.getId())
                    .update("dislikes", newDislikes)
                    .addOnSuccessListener(aVoid -> {
                        holder.comentarioTextView.setText(nombreUsuario + ": " + comentarioActual.getTexto() + " (Likes: " + comentarioActual.getLikes() + ") "  + " (Dislikes: " + newDislikes + ")");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context );
        View dialogView = LayoutInflater.from(context ).inflate(R.layout.dialog_comentario, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        TextView textoComentario = dialogView.findViewById(R.id.textoComentario);
        EditText campoRespuesta = dialogView.findViewById(R.id.campoRespuesta);
        Button botonEnviarRespuesta = dialogView.findViewById(R.id.botonEnviarRespuesta);
        RecyclerView recyclerViewRespuestas = dialogView.findViewById(R.id.recyclerViewRespuestas);

        // Mostrar el texto del comentario
        textoComentario.setText(nombreUsuario + ": " + comentarioActual.getTexto());

        // Configurar el RecyclerView para mostrar respuestas
        List<respuesta> listaRespuestas = obtenerRespuestas(comentarioActual.getId()); // Método para obtener respuestas
        respuestaAdapter respuestaAdapter = new respuestaAdapter(listaRespuestas);
        recyclerViewRespuestas.setAdapter(respuestaAdapter);
        recyclerViewRespuestas.setLayoutManager(new LinearLayoutManager(context ));

        // Configurar el botón para enviar la respuesta
        botonEnviarRespuesta.setOnClickListener(v -> {
            String respuestaTexto = campoRespuesta.getText().toString().trim();
            if (!respuestaTexto.isEmpty()) {
                enviarRespuesta(comentarioActual.getId(), respuestaTexto); // Método para enviar respuesta
                campoRespuesta.setText(""); // Limpiar el campo
                listaRespuestas.add(new respuesta(respuestaTexto)); // Agregar respuesta localmente
                respuestaAdapter.notifyItemInserted(listaRespuestas.size() - 1); // Notificar al adaptador
            }
        });

        dialog.show();
    }

    // Método para obtener respuestas del Firestore (puedes implementarlo según tu estructura)
    private List<respuesta> obtenerRespuestas(String comentarioId) {
        // Lógica para obtener las respuestas desde Firestore
        // Devuelve una lista de respuestas
        return java.util.Collections.emptyList();
    }

    // Método para enviar una respuesta a Firestore
    private void enviarRespuesta(String comentarioId, String respuestaTexto) {
        // Lógica para enviar la respuesta a Firestore
    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder {
        TextView comentarioTextView;
        TextView fechaHoraTextView; // ID del TextView para fecha y hora
        Button likeButton;
        Button dislikeButton;
        Button responderButton;

        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            comentarioTextView = itemView.findViewById(R.id.texto_comentario); // ID del TextView en item_comentario.xml
            fechaHoraTextView = itemView.findViewById(R.id.fecha_hora_text_view); // ID del TextView para fecha y hora
            likeButton = itemView.findViewById(R.id.like_button); // ID del botón like
            dislikeButton = itemView.findViewById(R.id.dislike_button); // ID del botón dislike
            responderButton = itemView.findViewById(R.id.responder_button); // ID del botón de respuesta
        }
    }
}