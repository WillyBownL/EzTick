package com.example.eztick;

public class Comentario {
    private String rol;
    private String texto;
    private long timestamp;

    public Comentario() {
        // Constructor vacío necesario para Firestore
    }

    public Comentario(String rol, String texto, long timestamp) {
        this.rol = rol;
        this.texto = texto;
        this.timestamp = timestamp;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
