package com.example.crowfunding;

import com.google.firebase.Timestamp;

public class comentario {

    private String id;        // ID del comentario
    private String texto;
    private String idUser;
    private Timestamp fechaHora;
    private int likes;
    private int dislikes;

    // Constructor vacío para Firebase
    public comentario() {}

    // Constructor con parámetros
    public comentario(String id, String texto, String userId, Timestamp fechaHora) {
        this.id = id;
        this.texto = texto;
        this.idUser = userId;
        this.fechaHora = fechaHora;
        this.likes = 0;       // Inicializa likes en 0
        this.dislikes = 0;    // Inicializa dislikes en 0
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}