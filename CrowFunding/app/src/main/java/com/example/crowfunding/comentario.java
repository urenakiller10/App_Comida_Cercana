package com.example.crowfunding;

import com.google.firebase.Timestamp;

public class comentario {

    private String texto;
    private String idUser;
    private Timestamp fechaHora;

    // Constructor vacío para Firebase
    public comentario() {}

    // Constructor con parámetros
    public comentario(String texto, String userId, Timestamp fechaHora) {
        this.texto = texto;
        this.idUser = userId;
        this.fechaHora = fechaHora;
    }

    // Getters y Setters
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUserId() {
        return idUser;
    }

    public void setUserId(String userId) {
        this.idUser = userId;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }
}