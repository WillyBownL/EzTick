package com.example.eztick;

/**
 * Clase que representa un elemento de ticket, utilizado para almacenar y gestionar la información
 * asociada a un ticket en el sistema.
 */
public class ListElementTicket {

    public String id; // ID único del ticket
    public String titulo; // Título del ticket
    public String descripcion; // Descripción del problema o incidente
    public String fecha; // Fecha de creación del ticket
    public String lvlPeligro; // Nivel de peligro del ticket
    private long fecha_resolucion; // Fecha de resolución del ticket, en milisegundos
    public String estado; // Estado del ticket (pendiente, resuelto, etc.)

    /**
     * Constructor vacío necesario para Firestore y otras operaciones de serialización.
     */
    public ListElementTicket() {
    }

    /**
     * Constructor para crear un nuevo ticket con los datos proporcionados.
     *
     * @param titulo       El título del ticket.
     * @param descripcion  La descripción del ticket.
     * @param fecha        La fecha de creación del ticket.
     * @param lvlPeligro   El nivel de peligro del ticket.
     */
    public ListElementTicket(String titulo, String descripcion, String fecha, String lvlPeligro) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.lvlPeligro = lvlPeligro;
        this.fecha_resolucion = 0; // Inicialmente no resuelto
        this.estado = "pendiente"; // Inicialmente el ticket está pendiente
    }

    // Métodos de acceso (getters y setters)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLvlPeligro() {
        return lvlPeligro;
    }

    public void setLvlPeligro(String lvlPeligro) {
        this.lvlPeligro = lvlPeligro;
    }

    public long getFecha_resolucion() {
        return fecha_resolucion;
    }

    public void setFecha_resolucion(long fecha_resolucion) {
        this.fecha_resolucion = fecha_resolucion;
    }

    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del ticket.
     *
     * @param estado El nuevo estado del ticket (ejemplo: "pendiente", "resuelto").
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
