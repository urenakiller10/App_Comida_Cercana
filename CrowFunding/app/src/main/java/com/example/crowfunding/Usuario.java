package com.example.crowfunding;

public class Usuario {
    private String name;
    private String idUser;

    public Usuario(String name, String idUser) {
        this.name = name;
        this.idUser = idUser;
    }

    // Getter para el nombre
    public String getName() {
        return name;
    }

    // Setter para el nombre
    public void setName(String name) {
        this.name = name;
    }

    public String getIdUser() {
        return idUser;
    }
}
