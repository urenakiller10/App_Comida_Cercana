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

    private List<DonacionData> donacionesList;
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public DonacionAdapter(List<DonacionData> donacionesList, Context context) {
        this.donacionesList = donacionesList;
        this.context = context;
    }

    @NonNull
    @Override
    public DonacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donation, parent, false);
        return new DonacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonacionViewHolder holder, int position) {
        DonacionData donacion = donacionesList.get(position);

        // Establecer el texto para mostrar el nombre del proyecto y el monto
        holder.tvProjectNameAndAmount.setText("Proyecto: " + donacion.getNombreProyecto() + " - Monto: $" + donacion.getMonto());

        // Establecer el texto para mostrar el nombre del usuario y la fecha
        String formattedDate = dateFormat.format(donacion.getFecha());
        holder.tvDonationDate.setText("Donado por: " + donacion.getNombrePersona() + " el " + formattedDate);
    }

    @Override
    public int getItemCount() {
        return donacionesList.size();
    }

    public static class DonacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectNameAndAmount, tvDonationDate;

        public DonacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectNameAndAmount = itemView.findViewById(R.id.tvProjectNameAndAmount);
            tvDonationDate = itemView.findViewById(R.id.tvDonationDate);
        }
    }
}
