package com.example5.lilian.caos_cwy.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.example5.lilian.caos_cwy.database.BDServidorPublico;
import com.example5.lilian.caos_cwy.database.DatabaseHelper;
import com.example5.lilian.caos_cwy.utils.ConvertirBitmapEnByteArray;

/**
 * Created by Johnny on 31/10/2017.
 */

//parametros para ejecutar, en ejecución y despues de ejecución (resultado)
public class ImagenesINSERTTask extends AsyncTask <Void,Void,Void>{
    Context context = null;
    String usuario;
    Bitmap imagen = null;
    ImageView vista = null;
    DatabaseHelper imgDbHelper  = null;

    public ImagenesINSERTTask(Context context){
        this.context = context;
        this.imgDbHelper = new DatabaseHelper(context);
    }

    public ImageView getVista(){
        return vista;
    }
    public void setVista(ImageView vista){
        this.vista = vista;
    }


    public String getUsuario(){
        return usuario;
    }

    public void setUsuario(String usuario){
        this.usuario = usuario;
    }

    public void setImagen(Bitmap imagen){
        this.imagen = imagen;
    }

    public Bitmap getImagen(){
        return imagen;
    }

    @Override
    protected Void doInBackground(Void... params) {
        //img.insertarImagen("adm@adm","https://38.media.tumblr.com/aea79d3c26acdcfe325e94c7ae251717/tumblr_inline_nqmfxalvCS1qk1op9_500.gif");
        imgDbHelper.insertarImagen(getUsuario(),getImagen());
        //creo las tablas y la BD
        BDServidorPublico bdmysql = new BDServidorPublico("https://johnny032295.000webhostapp.com/servidor_cwy_android/insertarimagenapi.php");
        bdmysql.insertarImagen(getUsuario(), ConvertirBitmapEnByteArray.convertir(getImagen()));


        //TODO agregar validacion si se inserto
        return null;
    }
    @Override
    protected void onPostExecute(Void success) {
        //traer la imagen luego de insertarla
        byte[] imagen = imgDbHelper.getImagen(getUsuario());
        if(imgDbHelper.getError()==null){
            vista.setImageBitmap(BitmapFactory.decodeByteArray(imagen,0,imagen.length));
        }else{
            Toast.makeText(context,imgDbHelper.getError(),Toast.LENGTH_LONG).show();
        }
    }

}
