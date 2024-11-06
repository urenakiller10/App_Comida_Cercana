package com.example.crowfunding;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class EmailSender {

    private static final String SENDGRID_API_URL = "https://api.sendgrid.com/v3/mail/send";
    private final String apiKey;

    // Constructor de EmailSender
    public EmailSender(String apiKey) {
        this.apiKey = "Bearer " + apiKey; // Prefijo para la API Key
    }

    // Método para enviar el correo
    public void enviarCorreo(String destinatario, String asunto, String contenido) {
        OkHttpClient client = new OkHttpClient();

        // Construir el cuerpo de la solicitud en formato JSON
        JSONObject json = new JSONObject();
        try {
            json.put("personalizations", new JSONArray()
                    .put(new JSONObject()
                            .put("to", new JSONArray()
                                    .put(new JSONObject().put("email", destinatario)))));
            json.put("from", new JSONObject().put("email", "Ub.andrey06105338@gmail.com")); // Cambia a tu correo registrado en SendGrid
            json.put("subject", asunto);
            json.put("content", new JSONArray()
                    .put(new JSONObject()
                            .put("type", "text/plain")
                            .put("value", contenido)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear la solicitud POST
        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(SENDGRID_API_URL)
                .addHeader("Authorization", apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        // Enviar la solicitud en un hilo de fondo
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("Correo enviado exitosamente");
                } else {
                    System.out.println("Error al enviar correo: " + response.body().string());
                }
            }
        });




    }
}
