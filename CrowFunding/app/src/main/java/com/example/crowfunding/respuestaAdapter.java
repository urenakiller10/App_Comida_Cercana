package com.example.crowfunding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crowfunding.R;
import com.example.crowfunding.respuesta;

import java.text.SimpleDateFormat;
import java.util.List;

public class respuestaAdapter extends RecyclerView.Adapter<respuestaAdapter.RespuestaViewHolder> {

    private List<respuesta> listaRespuestas;

    public respuestaAdapter(List<respuesta> listaRespuestas) {
        this.listaRespuestas = listaRespuestas;
    }

    @NonNull
    @Override
    public RespuestaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_respuesta, parent, false);
        return new RespuestaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RespuestaViewHolder holder, int position) {
        respuesta respuesta = listaRespuestas.get(position);
        holder.respuestaTextView.setText(respuesta.getTexto());

        // Mostrar el nombre del usuario y la fecha
        String textoCompleto = respuesta.getNombreUsuario() + ": " + respuesta.getTexto();
        holder.respuestaTextView.setText(textoCompleto);

        // Aquí se puede mostrar la fecha en un formato adecuado
        String fechaFormateada = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(respuesta.getFecha());
        holder.fechaTextView.setText(fechaFormateada);
    }

    @Override
    public int getItemCount() {
        return listaRespuestas.size();
    }

    public static class RespuestaViewHolder extends RecyclerView.ViewHolder {
        TextView respuestaTextView;
        TextView fechaTextView;  // Para mostrar la fecha

        public RespuestaViewHolder(@NonNull View itemView) {
            super(itemView);
            respuestaTextView = itemView.findViewById(R.id.respuestaTextView);
            fechaTextView = itemView.findViewById(R.id.fechaTextView);  // Referencia para la fecha
        }
    }
}