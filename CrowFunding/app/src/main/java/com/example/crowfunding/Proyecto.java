package com.example.crowfunding;

public class Proyecto {
    private String nombre;
    private String descripcion;
    private String fechaLimite;
    private String objetivoFinanciacion;
    private String categoria;

    // Constructor vacío para Firebase
    public Proyecto() {}

    public Proyecto(String nombre, String descripcion, String fechaLimite, String objetivoFinanciacion, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.objetivoFinanciacion = objetivoFinanciacion;
        this.categoria = categoria;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getFechaLimite() { return fechaLimite; }
    public String getObjetivoFinanciacion() { return objetivoFinanciacion; }
    public String getCategoria() { return categoria; }
}

