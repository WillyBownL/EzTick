package com.example.eztick;

/**
 * Clase que representa un comentario asociado a un ticket.
 * Cada comentario contiene el rol de la persona que lo hizo, el texto del comentario
 * y un timestamp que indica el momento en que se realizó el comentario.
 */
public class Comentario {

    // El rol de la persona que realiza el comentario (por ejemplo: "Supervisor", "Operador", etc.)
    private String rol;
    
    // El texto del comentario proporcionado por el usuario.
    private String texto;
    
    // El timestamp del comentario, representado como un valor largo (en milisegundos desde la época Unix).
    private long timestamp;

    /**
     * Constructor vacío necesario para inicializar objetos de la clase Comentario,
     * especialmente en casos donde los datos se establecen posteriormente.
     */
    public Comentario() {
    }

    /**
     * Constructor de la clase Comentario, que permite inicializar un objeto
     * con un rol, texto y timestamp específicos.
     * 
     * @param rol El rol de la persona que realiza el comentario.
     * @param texto El contenido del comentario.
     * @param timestamp El momento en que se realizó el comentario (en milisegundos).
     */
    public Comentario(String rol, String texto, long timestamp) {
        this.rol = rol;
        this.texto = texto;
        this.timestamp = timestamp;
    }

    /**
     * Obtiene el rol de la persona que realizó el comentario.
     * 
     * @return El rol de la persona que escribió el comentario.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol de la persona que realizó el comentario.
     * 
     * @param rol El rol de la persona que escribió el comentario.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Obtiene el texto del comentario.
     * 
     * @return El contenido textual del comentario.
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Establece el texto del comentario.
     * 
     * @param texto El contenido textual del comentario.
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Obtiene el timestamp del comentario.
     * 
     * @return El timestamp del comentario, en milisegundos desde la época Unix.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Establece el timestamp del comentario.
     * 
     * @param timestamp El momento en que se realizó el comentario, en milisegundos.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}