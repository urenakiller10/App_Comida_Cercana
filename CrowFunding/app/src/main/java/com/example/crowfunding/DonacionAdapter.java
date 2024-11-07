package com.example.crowfunding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DonacionAdapter extends RecyclerView.Adapter<DonacionAdapter.DonacionViewHolder> {
    private List<Donacion> donaciones;
    private Context context;

    // Constructor del adaptador
    public DonacionAdapter(List<Donacion> donaciones, Context context) {
        this.donaciones = donaciones;
        this.context = context;
    }

    @NonNull
    @Override
    public DonacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para cada item de donación
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donacion, parent, false);
        return new DonacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonacionViewHolder holder, int position) {
        Donacion donacion = donaciones.get(position);

        // Formatear la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaFormateada = sdf.format(donacion.getFecha());

        // Componer el texto para mostrar
        String projectNameAndAmount = "Proyecto ID: " + donacion.getIdProyecto() + " - Monto Donado: $" + donacion.getMonto();
        String donationDate = "El " + fechaFormateada + ", realizaste una donación";

        // Asignar los valores a los TextViews
        holder.tvProjectNameAndAmount.setText(projectNameAndAmount);
        holder.tvDonationDate.setText(donationDate);
    }

    @Override
    public int getItemCount() {
        return donaciones.size();
    }

    static class DonacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectNameAndAmount, tvDonationDate;

        public DonacionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Mapeo de los elementos del layout
            tvProjectNameAndAmount = itemView.findViewById(R.id.tvProjectNameAndAmount);
            tvDonationDate = itemView.findViewById(R.id.tvDonationDate);
        }
    }
}
