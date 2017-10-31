package com.example5.lilian.caoscwy;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.example5.lilian.caoscwy.database.DatabaseHelper;

/**
 * Created by Johnny on 31/10/2017.
 */

//parametros para ejecutar, en ejecución y despues de ejecución (resultado)
public class ImagenEnRedTask extends AsyncTask <Void,Void,Boolean>{
    Context context = null;
    ImageView imgView= null;

    public ImagenEnRedTask(Context context,ImageView view){
        this.context = context;
        this.imgView = view;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        DatabaseHelper img = new DatabaseHelper(context);
        img.insertarImagen("adm@adm","https://38.media.tumblr.com/aea79d3c26acdcfe325e94c7ae251717/tumblr_inline_nqmfxalvCS1qk1op9_500.gif");
        //TODO agregar validacion si se inserto
        return Boolean.TRUE;
    }
    @Override
    protected void onPostExecute(final Boolean success) {
        DatabaseHelper img = new DatabaseHelper(context);
        byte[] imagen = img.getImagen("adm@adm");
        imgView.setImageBitmap(BitmapFactory.decodeByteArray(imagen,0,imagen.length));
    }

}
