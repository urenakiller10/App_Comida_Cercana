package com.example.crowfunding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crowfunding.respuesta;

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
    }

    @Override
    public int getItemCount() {
        return listaRespuestas.size();
    }

    public static class RespuestaViewHolder extends RecyclerView.ViewHolder {
        TextView respuestaTextView;

        public RespuestaViewHolder(@NonNull View itemView) {
            super(itemView);
            respuestaTextView = itemView.findViewById(R.id.respuestaTextView);
        }
    }
}