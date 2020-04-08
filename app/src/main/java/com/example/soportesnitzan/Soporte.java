package com.example.soportesnitzan;

import android.os.Parcel;

import java.io.Serializable;

public class Soporte implements Serializable {


    private String personaRecibe;
    private String categoria;
    private String comentarios;
    private String comentariost;
    private String detalle;
    private String fecha;
    private String finalizado;
    private String finca;
    private int hbase;
    private String solicitante;
    private String telefono;
    private String id;
    private String tecnico;
    private Long valor;
    private String costomov;
    private String garantia;



    private String notadecredito;
    private String costoTotal;


    public Soporte(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
         }



    public Soporte(String finca, String categoria, String detalle, String comentarios, String comentariost, String fecha, String id, String finalizado, int hbase, String tecnico, Long valor, String personaRecibe,String solicitante,String telefono,String garantia,String notadecredito,String costomov,String costoTotal) {
        this.fecha=fecha;
        this.comentariost=comentariost;
        this.finca = finca;
        this.categoria = categoria;
        this.detalle = detalle;
        this.comentarios = comentarios;
        this.id = id;
        this.finalizado=finalizado;
        this.hbase = hbase;
        this.tecnico = tecnico;
        this.valor = valor;
        this.personaRecibe=personaRecibe;
        this.solicitante=solicitante;
        this.telefono=telefono;
        this.garantia=garantia;
        this.notadecredito=notadecredito;
        this.costomov=costomov;
        this.costoTotal=costoTotal;

    }

    protected Soporte(Parcel in) {
        fecha = in.readString();
        comentariost = in.readString();
        finca = in.readString();
        categoria = in.readString();
        detalle = in.readString();
        comentarios = in.readString();
        id = in.readString();
        finalizado = in.readString();
        hbase = in.readInt();
        tecnico = in.readString();
        valor = in.readLong();
        personaRecibe = in.readString();
        solicitante = in.readString();
        telefono = in.readString();
        garantia = in.readString();
        notadecredito = in.readString();
        costomov = in.readString();
        costoTotal = in.readString();
    }

    public int getHbase() {
        return hbase;
    }

    public void setHbase(int hbase) {
        this.hbase = hbase;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(String finalizado) {
        this.finalizado = finalizado;
    }

    public String getFinca() {
        return finca;
    }

    public void setFinca(String finca) {
        this.finca = finca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
    public String getComentariost() {
        return comentariost;
    }

    public void setComentariost(String comentariost) {
        this.comentariost = comentariost;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }


    public String getPersonaRecibe() {
        return personaRecibe;
    }

    public void setPersonaRecibe(String personaRecibe) {
        this.personaRecibe = personaRecibe;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }
    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public String getNotadecredito() {
        return notadecredito;
    }

    public void setNotadecredito(String notadecredito) {
        this.notadecredito = notadecredito;
    }

    public String getCostomov() {
        return costomov;
    }

    public void setCostomov(String costomov) {
        this.costomov = costomov;
    }
    public String getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(String costoTotal) {
        this.costoTotal = costoTotal;
    }





}
