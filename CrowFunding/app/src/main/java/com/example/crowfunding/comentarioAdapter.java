package com.example.crowfunding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
                        String nombreUsuario = documentSnapshot.getString("nombre");
                    }
                })
                .addOnFailureListener(e -> {
                    holder.comentarioTextView.setText("Error al cargar el nombre del usuario: " + comentarioActual.getTexto());
                });
    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder {
        TextView comentarioTextView;

        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            comentarioTextView = itemView.findViewById(R.id.texto_comentario); // ID del TextView en item_comentario.xml
        }
    }
}