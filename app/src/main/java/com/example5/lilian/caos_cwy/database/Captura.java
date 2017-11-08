package com.example5.lilian.caos_cwy.database;

import android.graphics.Bitmap;

/**
 * Created by Johnny on 8/11/2017.
 */

public class Captura {
    private String usuario;
    private Bitmap imagen;


    public Captura(String usuario, Bitmap imagen) {
        this.usuario = usuario;
        this.imagen = imagen;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
