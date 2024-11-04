package com.example.crowfunding;

import java.util.Date;

public class Donacion {

    private String idUser;        // ID del usuario que realiza la donación
    private String idProyecto;    // ID del proyecto al que se realiza la donación
    private float monto;          // Monto de la donación
    private Date fecha;           // Fecha en que se realiza la donación

    // Constructor vacío necesario para Firebase Firestore
    public Donacion() {}

    // Constructor completo
    public Donacion(String idUser, String idProyecto, float monto, Date fecha) {
        this.idUser = idUser;
        this.idProyecto = idProyecto;
        this.monto = monto;
        this.fecha = fecha;
    }

    // Getters y setters para cada atributo

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
