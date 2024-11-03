package com.example.crowfunding;

public class Proyecto {
    private String id; // Campo para almacenar el ID del proyecto
    private String nombre;
    private String descripcion;
    private String fechaLimite;
    private String objetivoFinanciacion;
    private String categoria;

    // Constructor sin argumentos (requerido por Firestore)
    public Proyecto() {
    }

    // Constructor
    public Proyecto(String nombre, String descripcion, String fechaLimite, String objetivoFinanciacion, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.objetivoFinanciacion = objetivoFinanciacion;
        this.categoria = categoria;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getObjetivoFinanciacion() {
        return objetivoFinanciacion;
    }

    public void setObjetivoFinanciacion(String objetivoFinanciacion) {
        this.objetivoFinanciacion = objetivoFinanciacion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
