package com.example.eztick;

public class ListElementTicket {
    public String id;
    public String titulo;
    public String descripcion;
    public String fecha;
    public String lvlPeligro;
    private long fecha_resolucion;
    public String estado;

    public ListElementTicket() {
    }

    public ListElementTicket(String tiulo, String descripcion, String fecha, String lvlPeligro) {
        this.titulo = tiulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.lvlPeligro = lvlPeligro;
        this.fecha_resolucion = 0;
        this.estado = "pendiente";
    }


    public String getId(){  return id;  }

    public void setId(String id){   this.id = id;   }

    public String getTitulo() { return titulo;  }

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

    public long getFecha_resolucion() { return fecha_resolucion; }

    public void setFecha_resolucion(long fecha_resolucion) { this.fecha_resolucion = fecha_resolucion; }

    public String getEstado() { return estado;}

}