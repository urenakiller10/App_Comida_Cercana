package com.example.crowfunding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PermisosAdapter extends RecyclerView.Adapter<PermisosAdapter.PermissionViewHolder> {

    private List<Permiso> permissions;
    private OnPermissionActionListener actionListener;

    // Constructor principal con la lista de permisos y el listener
    public PermisosAdapter(List<Permiso> permissions) {
        this.permissions = permissions != null ? permissions : new ArrayList<>();
    }

    // Método para actualizar la lista de permisos y refrescar el adaptador
    public void setPermisos(List<Permiso> permisos) {
        this.permissions = permisos != null ? permisos : new ArrayList<>();
        notifyDataSetChanged(); // Notificar cambios al adaptador
    }

    @NonNull
    @Override
    public PermissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_role_permission, parent, false);
        return new PermissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PermissionViewHolder holder, int position) {
        String permissionName = permissions.get(position).getNombre();
        holder.permissionTextView.setText(permissionName);

        if (actionListener != null) {
            holder.btnDeletePermission.setOnClickListener(v -> actionListener.onDeletePermission(permissionName));
        }
    }

    @Override
    public int getItemCount() {
        return permissions.size();
    }

    // ViewHolder para los permisos
    public static class PermissionViewHolder extends RecyclerView.ViewHolder {
        TextView permissionTextView;
        Button btnDeletePermission;

        public PermissionViewHolder(@NonNull View itemView) {
            super(itemView);
            permissionTextView = itemView.findViewById(R.id.tvRolePermissionName);
            btnDeletePermission = itemView.findViewById(R.id.btnAgregarEliminar);
        }
    }

    // Interfaz para manejar acciones en permisos (como eliminación)
    public interface OnPermissionActionListener {
        void onDeletePermission(String permission);
    }
}