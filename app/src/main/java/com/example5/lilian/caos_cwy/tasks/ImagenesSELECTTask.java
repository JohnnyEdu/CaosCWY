package com.example5.lilian.caos_cwy.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.example5.lilian.caos_cwy.database.BDServidorPublico;
import com.example5.lilian.caos_cwy.database.DatabaseHelper;
import com.example5.lilian.caos_cwy.utils.ConvertirBitmapEnByteArray;

/**
 * Created by Johnny on 31/10/2017.
 */

//parametros para ejecutar, en ejecución y despues de ejecución (resultado)
public class ImagenesSELECTTask extends AsyncTask <Void,Void,Bitmap>{
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
    protected Bitmap doInBackground(Void... params) {
        //creo las tablas y la BD
        BDServidorPublico bdmysql = new BDServidorPublico("https://johnny032295.000webhostapp.com/servidor_cwy_android/selectimagenapi.php");
        Bitmap btmp = bdmysql.selectImagenesPorUsuario(getUsuario());
        return btmp;
    }
    @Override
    protected void onPostExecute(Bitmap imagen) {
        if(vista instanceof  ImageView){
            ((ImageView)vista).setImageBitmap(imagen);
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
