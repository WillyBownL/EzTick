package com.example.eztick;

public class ListElementTicket {
    public String id;
    public String titulo;
    public String descripcion;
    public String fecha;
    public String lvlPeligro;

    public ListElementTicket() {
    }

    public ListElementTicket(String tiulo, String descripcion, String fecha, String lvlPeligro) {
        this.titulo = tiulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.lvlPeligro = lvlPeligro;
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

}