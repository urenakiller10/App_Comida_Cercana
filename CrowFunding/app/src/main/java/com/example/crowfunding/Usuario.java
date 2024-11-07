package com.example.crowfunding;

public class Usuario {
    private String name;

    public Usuario(String name) {
        this.name = name;
    }

    // Getter para el nombre
    public String getName() {
        return name;
    }

    // Setter para el nombre
    public void setName(String name) {
        this.name = name;
    }
}