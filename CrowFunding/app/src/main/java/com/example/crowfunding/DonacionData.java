package com.example.crowfunding;

import java.util.Date;

public class DonacionData {
    private Date fecha;              // Fecha de la donación
    private float monto;             // Monto de la donación
    private String nombrePersona;    // Nombre de la persona que hizo la donación
    private String nombreProyecto;   // Nombre del proyecto al que se realizó la donación

    // Constructor vacío necesario para Firebase Firestore
    public DonacionData() {}

    // Constructor completo
    public DonacionData(Date fecha, float monto, String nombrePersona, String nombreProyecto) {
        this.fecha = fecha;
        this.monto = monto;
        this.nombrePersona = nombrePersona;
        this.nombreProyecto = nombreProyecto;
    }

    // Getters y setters
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
}
