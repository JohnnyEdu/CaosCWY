package com.example5.lilian.caos_cwy.tasks;

import android.content.Context;
import android.graphics.Bitmap;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.example5.lilian.caos_cwy.database.BDServidorPublico;
import com.example5.lilian.caos_cwy.database.Captura;


import java.util.List;

/**
 * Created by Johnny on 31/10/2017.
 */

//parametros para ejecutar, en ejecución y despues de ejecución (resultado)
public class ImagenesSELECTTask extends AsyncTask <Void,Void,List<Captura> >{
    Context context = null;
    String usuario;
    View vista;

    public ImagenesSELECTTask(Context context){
        this.context = context;
    }

    public String getUsuario(){
        return usuario;
    }

    public void setUsuario(String usuario){
        this.usuario = usuario;
    }


    public View getVista() {
        return vista;
    }

    public void setVista(View vista) {
        this.vista = vista;
    }

    @Override
    protected List<Captura> doInBackground(Void... params) {
        //creo las tablas y la BD
        BDServidorPublico bdmysql = new BDServidorPublico("https://johnny032295.000webhostapp.com/servidor_cwy_android/selectimagenapi.php");
        List<Captura>  btmp = bdmysql.selectImagenesPorUsuario(getUsuario());
        return btmp;
    }
    @Override
    protected void onPostExecute(List<Captura> imagen) {
        if(vista instanceof  ImageView){
            ((ImageView)vista).setImageBitmap(imagen.get(0).getImagen());
        }

        //traer la imagen luego de insertarla
        //codigo para imagen en SQLite
        /*byte[] imagen = imgDbHelper.getImagen(getUsuario());
        if(imgDbHelper.getError()==null){
            vista.setImageBitmap(BitmapFactory.decodeByteArray(imagen,0,imagen.length));
        }else{
            Toast.makeText(context,imgDbHelper.getError(),Toast.LENGTH_LONG).show();
        }*/
    }
}
