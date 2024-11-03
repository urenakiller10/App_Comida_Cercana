package com.example.crowfunding;

public class Proyecto {
    private String idUser;
    private String idProyecto; // Campo para almacenar el ID del proyecto
    private String nombre;
    private String descripcion;
    private String fechaCreacion;
    private String fechaLimite;
    private float dineroDonado;
    private String objetivoFinanciacion;
    private String categoria;

    // Constructor sin argumentos (requerido por Firestore)
    public Proyecto() {
    }

    // Constructor
    public Proyecto(String idUser, String nombre, String descripcion, String fechaCreacion, String fechaLimite, String objetivoFinanciacion, String categoria) {
        this.idUser = idUser;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaLimite = fechaLimite;
        this.dineroDonado = 0;
        this.objetivoFinanciacion = objetivoFinanciacion;
        this.categoria = categoria;
    }

    // Getters y Setters
    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String id) {
        this.idProyecto = id;
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

    public float getDineroActual() {return dineroDonado;}
}
