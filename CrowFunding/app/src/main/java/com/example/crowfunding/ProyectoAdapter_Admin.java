package com.example.crowfunding;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProyectoAdapter_Admin extends RecyclerView.Adapter<ProyectoAdapter_Admin.ProyectoViewHolder> {
    private List<Proyecto> proyectos;
    private Context context;

    public ProyectoAdapter_Admin(List<Proyecto> proyectos, Context context) {
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

        // Asigna los datos del proyecto a los elementos de la vista
        holder.textNombre.setText(proyecto.getNombre());
        holder.textDescripcion.setText(proyecto.getDescripcion());
        holder.textFechaLimite.setText(proyecto.getFechaLimite());
        holder.textObjetivo.setText(proyecto.getObjetivoFinanciacion());
        holder.textFechaCreacion.setText(proyecto.getFechaCreacion());

        // Aquí NO se configura ningún listener de clic para itemView, solo se muestran los datos.
    }

    @Override
    public int getItemCount() {
        return proyectos.size();
    }

    static class ProyectoViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textDescripcion, textFechaLimite, textObjetivo, textFechaCreacion;

        public ProyectoViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textNombre);
            textDescripcion = itemView.findViewById(R.id.textDescripcion);
            textFechaLimite = itemView.findViewById(R.id.textFechaLimite);
            textObjetivo = itemView.findViewById(R.id.textObjetivo);
            textFechaCreacion = itemView.findViewById(R.id.textFechaCreacion);
        }
    }
}
