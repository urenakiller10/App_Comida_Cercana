package com.example.crowfunding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RolesAdapter extends RecyclerView.Adapter<RolesAdapter.RoleViewHolder> {

    private List<Rol> roles;
    private OnRoleActionListener actionListener;

    public RolesAdapter(List<Rol> roles) {
        this.roles = roles;
    }

    @NonNull
    @Override
    public RoleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_role_permission, parent, false);
        return new RoleViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RoleViewHolder holder, int position) {
        String role = String.valueOf(roles.get(position).getRoleName());
        holder.roleTextView.setText(role);

    }

    @Override
    public int getItemCount() {
        return roles.size();
    }

    public void setPermisos(List<Rol> roles) {
        this.roles = roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public static class RoleViewHolder extends RecyclerView.ViewHolder {
        TextView roleTextView;
        Button btnDeleteRole;

        public RoleViewHolder(@NonNull View itemView) {
            super(itemView);
            roleTextView = itemView.findViewById(R.id.tvRolePermissionName);
            btnDeleteRole = itemView.findViewById(R.id.btnAgregarEliminar);
        }
    }

    public interface OnRoleActionListener {
        void onDeleteRole(String role);
    }
}