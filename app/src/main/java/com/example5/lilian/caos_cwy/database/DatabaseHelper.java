package com.example5.lilian.caos_cwy.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;

import com.example5.lilian.caos_cwy.encriptado.AES256Cipher;
import com.example5.lilian.caos_cwy.utils.ConvertirBitmapEnByteArray;
import com.example5.lilian.caos_cwy.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Johnny on 28/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String BASE_DE_DATOS = "cwy";
    private static final String TABLA_USUARIOS="USUARIOS";
    private static final String TABLA_IMAGENES="IMAGENES";
    private static final String SQL_CREATE_USUARIOS = "CREATE TABLE "+TABLA_USUARIOS+
            "(USUARIO VARCHAR(100) PRIMARY KEY,PASSWORD VARCHAR(255))";
    private static final String SQL_CREATE_IMAGENES = "CREATE TABLE "+TABLA_IMAGENES+
            "(USUARIO TEXT PRIMARY KEY,IMAGEN BLOB)";
    private static final String SQL_DELETE_USUARIOS = "DROP TABLE IF EXISTS "+TABLA_USUARIOS;
    private static final String SQL_DELETE_IMAGENES = "DROP TABLE IF EXISTS "+TABLA_IMAGENES;

    private String error;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, BASE_DE_DATOS, null, DATABASE_VERSION);
        this.context = context;
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USUARIOS);
        db.execSQL(SQL_CREATE_IMAGENES);
        String passwrdD = null;
        try {
            passwrdD = new String(AES256Cipher.encrypt(new String("gmKxWAMTEBSk6K8w").getBytes(), new String("BLukTQoFfyOD6Mo4GIxHcfd2zrEXU4kf").getBytes(), new String("11111111").getBytes()));
        }catch(Exception e){
            e.printStackTrace();
        }
        //db.execSQL("INSERT INTO USUARIOS (USUARIO,PASSWORD)VALUES ('adm@adm',"+ passwrdD + "'");
        db.execSQL("INSERT INTO USUARIOS (USUARIO,PASSWORD)VALUES ('adm@adm','11111111')");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //al crear la base de datos, se borran los datos actuales.
        db.execSQL(SQL_DELETE_USUARIOS);
        db.execSQL(SQL_DELETE_IMAGENES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public String getError(){
        return error;
    }

    public void setError(String e){
        this.error = e;
    }

    public void insertarImagen(String usuario, Bitmap imagenParam){
        //inserto en blob
        try {
            SQLiteDatabase basededatos = this.getReadableDatabase();

            String deletePrimero = "DELETE FROM IMAGENES WHERE USUARIO = ?";
            basededatos.execSQL(deletePrimero, new String[]{usuario});

            String consultaInsert = "INSERT INTO IMAGENES (USUARIO,IMAGEN) VALUES (?,?)";
            SQLiteStatement sentencia = basededatos.compileStatement(consultaInsert);
            sentencia.clearBindings();
            sentencia.bindString(1, usuario);
            //convertidor de Bitmap de android en byte[] java
            sentencia.bindBlob(2, ConvertirBitmapEnByteArray.convertir(imagenParam));
            sentencia.executeInsert();
            basededatos.close();
        }catch(Exception noSePudoInsertar){
            setError(context.getResources().getString(R.string.errorIsertarImagen));
        }
    }
//no se utiliza
    private byte[] traerImagenDeInternet(String imgurl){
        try {
            URL url = new URL(imgurl);
            URLConnection con = url.openConnection();
            InputStream input = con.getInputStream();
            //guardo los bytes de respuesta de la URL en un decorator que almacena en el espacio reservado para el proceso
            // por cada acceso a la memoria un conjunto de bytes en vez de sólo un byte por cada acceso (mas eficiente)
            BufferedInputStream bis = new BufferedInputStream(input);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int actual =0;
            //mientras haya bytes en el buffer
            while ((actual = bis.read()) != -1){
                baos.write((byte)actual);
            }
            return baos.toByteArray();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //traer un usuario por su username
    public User getUsuario(String usuario){
        SQLiteDatabase basededatos = this.getReadableDatabase();
        String consultaSelect = "SELECT * FROM "+ TABLA_USUARIOS + " WHERE USUARIO = ?";
        Cursor cursor = basededatos.rawQuery(consultaSelect,new String[] {usuario});
        //Cursor cursor = basededatos.rawQuery("SELECT * FROM USUARIOS",null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        User user = null;
        try {
            user = new User(cursor.getString(cursor.getColumnIndex("USUARIO")),
                    cursor.getString(cursor.getColumnIndex("PASSWORD")));
            cursor.close();
        }catch(Exception e) {
            setError(context.getResources().getString(R.string.errorNoExisteElUsuario));
        }
        basededatos.close();
        return user;
    }



    //traer una imagen por su username
    public byte[] getImagen(String usuario){
        SQLiteDatabase basededatos = this.getReadableDatabase();
        String consultaSelect = "SELECT * FROM "+ TABLA_IMAGENES + " WHERE USUARIO = ?";
        Cursor cursor = basededatos.rawQuery(consultaSelect,new String[] {usuario});
        if(cursor != null){
            cursor.moveToFirst();
        }
        byte[] imagenBd = null;
        try {
            imagenBd = cursor.getBlob(cursor.getColumnIndex("IMAGEN"));
            cursor.close();
        }catch(Exception e) {
            setError(context.getResources().getString(R.string.errorObtenerImagen));
            e.printStackTrace();
        }
        basededatos.close();
        return imagenBd;
    }
}
