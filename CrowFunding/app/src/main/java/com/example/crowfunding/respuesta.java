package com.example.crowfunding;

import java.util.Date;

public class respuesta {
    private String comentarioId;
    private String texto;
    private String userId;
    private Date fecha;  // Para almacenar la fecha
    private String nombreUsuario;

    // Constructor
    public respuesta(String comentarioId, String texto, String userId, Date fecha, String nombreUsuario) {
        this.comentarioId = comentarioId;
        this.texto = texto;
        this.userId = userId;
        this.fecha = fecha;
        this.nombreUsuario = nombreUsuario;
    }

    // Getters y setters
    public String getTexto() {
        return texto;
    }

    public String getUserId() {
        return userId;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}