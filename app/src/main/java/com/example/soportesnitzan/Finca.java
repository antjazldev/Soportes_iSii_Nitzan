package com.example.soportesnitzan;

public class Finca {
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Finca(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    public Finca() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    private String nombre;
    private String correo;
}
