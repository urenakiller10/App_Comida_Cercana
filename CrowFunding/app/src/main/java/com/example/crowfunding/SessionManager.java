package com.example.crowfunding;

public class SessionManager {
    private static String email;

    // Método para establecer el email
    public static void setEmail(String userEmail) {
        email = userEmail;
    }

    // Método para obtener el email
    public static String getEmail() {
        return email;
    }
}
