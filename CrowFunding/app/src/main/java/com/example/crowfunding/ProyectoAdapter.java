package com.example.crowfunding;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProyectoAdapter extends RecyclerView.Adapter<ProyectoAdapter.ProyectoViewHolder> {
    private List<Proyecto> proyectos;
    private Context context;

    public ProyectoAdapter(List<Proyecto> proyectos, Context context) {
        this.proyectos = proyectos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProyectoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proyecto, parent, false);
        return new ProyectoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProyectoViewHolder holder, int position) {
        Proyecto proyecto = proyectos.get(position);
        holder.textNombre.setText(proyecto.getNombre());
        holder.textDescripcion.setText(proyecto.getDescripcion());
        holder.textFechaLimite.setText(proyecto.getFechaLimite());
        holder.textObjetivo.setText(proyecto.getObjetivoFinanciacion());
        holder.itemView.setOnClickListener(v -> {
            // Aquí puedes agregar la lógica para redirigir a otra actividad
            // Intent intent = new Intent(context, DetalleProyectoActivity.class);
            // intent.putExtra("proyectoId", proyecto.getId()); // O lo que necesites
            // context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return proyectos.size();
    }

    static class ProyectoViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textDescripcion, textFechaCreacion, textFechaLimite, textObjetivo;

        public ProyectoViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textNombre);
            textDescripcion = itemView.findViewById(R.id.textDescripcion);
            textFechaCreacion = itemView.findViewById(R.id.textFechaCreacion);
            textFechaLimite = itemView.findViewById(R.id.textFechaLimite);
            textObjetivo = itemView.findViewById(R.id.textObjetivo);
        }
    }
}

