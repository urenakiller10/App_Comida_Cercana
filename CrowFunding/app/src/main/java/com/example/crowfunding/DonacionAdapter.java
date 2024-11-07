package com.example.crowfunding;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crowfunding.Donacion;
import com.example.crowfunding.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DonacionAdapter extends RecyclerView.Adapter<DonacionAdapter.DonacionViewHolder> {

    private List<Donacion> donacionesList;
    private Context context;

    public DonacionAdapter(List<Donacion> donacionesList, Context context) {
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
        Donacion donacion = donacionesList.get(position);
        holder.tvProjectNameAndAmount.setText("Proyecto: " + donacion.getIdProyecto() + " - Monto: $" + donacion.getMonto());
        holder.tvDonationDate.setText("Fecha: " + donacion.getFecha().toString());
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
