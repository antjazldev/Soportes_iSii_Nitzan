package com.example.soportesnitzan;

public class Servicio {

    private String codigo;
    private String nombre;
    private String categoria;
    private int hbase;



    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Servicio(String codigo, String nombre, int hbase, String categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.hbase = hbase;
        this.categoria = categoria;

    }

    public Servicio() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getBase() {
        return nombre;
    }

    public void setNombre(String base) {
        this.nombre = base;
    }

    public int getHbase() {
        return hbase;
    }

    public void setHbase(int hbase) {
        this.hbase = hbase;
    }
    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }


}
