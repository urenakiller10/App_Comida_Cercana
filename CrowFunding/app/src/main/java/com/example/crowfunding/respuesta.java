package com.example.crowfunding;

import java.util.Date;

public class respuesta {
    private String texto;
    private String userId; // Puedes agregar un ID de usuario si deseas asociar respuestas con usuarios específicos
    private Date fechaHora; // Puedes agregar una fecha y hora para la respuesta

    // Constructor
    public respuesta(String texto) {
        this.texto = texto;
        this.fechaHora = new Date(); // Asigna la fecha y hora actual
    }

    // Getter y Setter para el texto
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    // Getter y Setter para userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter y Setter para fechaHora
    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }
}