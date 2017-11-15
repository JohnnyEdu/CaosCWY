package com.example5.lilian.caos_cwy.database;

import java.util.Date;

/**
 * Created by Johnny on 4/11/2017.
 */

public class Incidente {
    private Integer id;
    private String usuario;
    private String tipo;
    private String zona;
    private String fechaYhora;
    private Captura captura;
    private String comentario;
    private Integer cantidad;
    private Double latitud;
    private Double longitud;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getFechaYhora() {
        return fechaYhora;
    }

    public void setFechaYhora(String fechaYhora) {
        this.fechaYhora = fechaYhora;
    }

    public Captura getCaptura() {
        return captura;
    }

    public void setCaptura(Captura captura) {
        this.captura = captura;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
