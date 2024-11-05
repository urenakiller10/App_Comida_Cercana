package com.example.crowfunding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
                    } else {
                        holder.comentarioTextView.setText("Desconocido: " + comentarioActual.getTexto());
                    }
                })
                .addOnFailureListener(e -> {
                    holder.comentarioTextView.setText("Error al cargar el usuario");
                });

        // Aquí puedes establecer la fecha y hora del comentario
        holder.fechaHoraTextView.setText(comentarioActual.getFechaHora().toDate().toString());

        // Configurar listeners para los botones
        holder.likeButton.setOnClickListener(v -> {
            //int newLikes = comentarioActual.getLikes() + 1;
            //comentarioActual.setLikes(newLikes);
            //db.collection("comentariosForoGeneral").document(comentarioActual.getId())
            //        .update("likes", newLikes)
            //        .addOnSuccessListener(aVoid -> {
            //            holder.comentarioTextView.setText("nombreUsuario" + ": " + comentarioActual.getTexto() + " (Likes: " + newLikes + ")");
            //        })
            //        .addOnFailureListener(e -> {
            //            Toast.makeText(context, "Error al dar like: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            //        });
        });

        holder.dislikeButton.setOnClickListener(v -> {
            //int newDislikes = comentarioActual.getDislikes() + 1;
            //comentarioActual.setDislikes(newDislikes);
            //db.collection("comentariosForoGeneral").document(comentarioActual.getId())
            //        .update("dislikes", newDislikes)
            //        .addOnSuccessListener(aVoid -> {
            //            // Actualiza la interfaz si es necesario
            //            holder.comentarioTextView.setText("nombreUsuario" + ": " + comentarioActual.getTexto() + " (Dislikes: " + newDislikes + ")");
            //        })
            //        .addOnFailureListener(e -> {
            //            Toast.makeText(context, "Error al dar dislike: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            //        });
        });

        holder.responderButton.setOnClickListener(v -> {
            // Lógica para responder al comentario
            // Abre un diálogo o un nuevo fragmento para que el usuario escriba su respuesta
        });

        // Aquí puedes cargar las respuestas anidadas si es necesario
        // holder.respuestasContainer debería llenarse con las respuestas al comentario actual
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