package com.example.crowfunding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Proyecto {
    private String nombre;
    private String descripcion;
    private String fechaLimite;
    private String objetivoFinanciacion;
    private String categoria;
    private String fechaCreacion;
    private String idCreador;

    // Constructor vacío para Firebase
    public Proyecto() {}

    // Constructor completo
    public Proyecto(String nombre, String descripcion, String fechaLimite, String objetivoFinanciacion, String categoria, String idCreador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.objetivoFinanciacion = objetivoFinanciacion;
        this.categoria = categoria;
        this.idCreador = idCreador;

        // Generar la fecha de creación con la fecha actual
        this.fechaCreacion = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getFechaLimite() { return fechaLimite; }
    public String getObjetivoFinanciacion() { return objetivoFinanciacion; }
    public String getCategoria() { return categoria; }
    public String getFechaCreacion() { return fechaCreacion; }
    public String getIdCreador() { return idCreador; }
}
