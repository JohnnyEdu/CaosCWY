package com.example5.lilian.caos_cwy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example5.lilian.caos_cwy.encriptado.AES256Cipher;
import com.example5.lilian.caos_cwy.utils.ConvertirBitmapEnByteArray;
import com.example5.lilian.caos_cwy.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Johnny on 28/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String BASE_DE_DATOS = "cwy";
    private static final String TABLA_USUARIOS="USUARIOS";
    private static final String TABLA_IMAGENES="IMAGENES";
    private static final String TABLA_INCIDENTES="INCIDENTES";
    private static final String SQL_CREATE_USUARIOS = "CREATE TABLE "+TABLA_USUARIOS+
            "(USUARIO VARCHAR(100) PRIMARY KEY,PASSWORD VARCHAR(255))";
    private static final String SQL_CREATE_IMAGENES = "CREATE TABLE "+TABLA_IMAGENES+
            "(USUARIO TEXT PRIMARY KEY,IMAGEN BLOB)";
    private static final String SQL_CREATE_INCIDENTES = "CREATE TABLE "+TABLA_INCIDENTES+
            "(id INTEGER PRIMARY KEY,usuario TEXT, fechaYhora TEXT,tipo TEXT, zona TEXT,comentarios TEXT," +
            "latitud INTEGER, longitud INTEGER,imagen BLOB)";
    private static final String SQL_DELETE_USUARIOS = "DROP TABLE IF EXISTS "+TABLA_USUARIOS;
    private static final String SQL_DELETE_IMAGENES = "DROP TABLE IF EXISTS "+TABLA_IMAGENES;
    private static final String SQL_DELETE_INCIDENTES = "DROP TABLE IF EXISTS "+TABLA_INCIDENTES;

    private String error;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, BASE_DE_DATOS, null, DATABASE_VERSION);
        this.context = context;
    }
    public void onCreate(SQLiteDatabase db) {
        //se crean las tablas de la base de datos

        //la tabla imagenes se usaba para guardar las imagenes en el dispositivo,(una imagen de internet)

        db.execSQL(SQL_CREATE_USUARIOS);
        db.execSQL(SQL_CREATE_IMAGENES);
        db.execSQL(SQL_CREATE_INCIDENTES);
        String passwrdD = null;
        try {
            passwrdD = new String(AES256Cipher.encrypt(new String("gmKxWAMTEBSk6K8w").getBytes(), new String("BLukTQoFfyOD6Mo4GIxHcfd2zrEXU4kf").getBytes(), new String("11111111").getBytes()));
        }catch(Exception e){
            e.printStackTrace();
        }

        //TODO: sacar hardcode cuando pase a ser una aplicacón productiva

        //db.execSQL("INSERT INTO USUARIOS (USUARIO,PASSWORD)VALUES ('adm@adm',"+ passwrdD + "'");
        db.execSQL("INSERT INTO USUARIOS (USUARIO,PASSWORD)VALUES ('adm@adm','11111111')");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //al crear la base de datos, se borran los datos actuales.
        db.execSQL(SQL_DELETE_USUARIOS);
        db.execSQL(SQL_DELETE_IMAGENES);
        db.execSQL(SQL_DELETE_INCIDENTES);
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


    public ArrayList<Incidente>traerIncidentesLocalmente(){
        SQLiteDatabase bd = this.getReadableDatabase();
        ArrayList<Incidente> incidentes = new ArrayList<>();
        String consultaSelect = "SELECT * FROM incidentes";
        Cursor cursor = bd.rawQuery(consultaSelect,null);
        //Cursor cursor = basededatos.rawQuery("SELECT * FROM USUARIOS",null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        User user = null;
        try {
            while(cursor.moveToNext()){
                Incidente incidente = new Incidente();
                incidente.setId(cursor.getInt(cursor.getColumnIndex("id")));
                String usuario = cursor.getString(cursor.getColumnIndex("usuario"));
                incidente.setUsuario(usuario );
                incidente.setFechaYhora(cursor.getString(cursor.getColumnIndex("fechaYhora")));
                incidente.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
                incidente.setComentario(cursor.getString(cursor.getColumnIndex("comentarios")));
                incidente.setLatitud(Double.valueOf(cursor.getInt(cursor.getColumnIndex("latitud"))));
                incidente.setLongitud(Double.valueOf(cursor.getInt(cursor.getColumnIndex("longitud"))));
                byte[] imagen= cursor.getBlob(cursor.getColumnIndex("imagen"));
                Bitmap decodedByte = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
                if(decodedByte!=null){
                    incidente.setCaptura(new Captura(usuario ,decodedByte));
                }
                incidentes.add(incidente);
            }

        }catch(Exception e) {
            setError(context.getResources().getString(R.string.errorNoExisteElUsuario));
        }
        bd.close();
        return  incidentes;
    }

    public void guardarIncidentesLocalmente(ArrayList<Incidente> incidentese){
        SQLiteDatabase bd = this.getWritableDatabase();
        Iterator<Incidente> iterador = incidentese.iterator();
        while(iterador.hasNext()){
            Incidente incidente = iterador.next();
            ContentValues values = new ContentValues();
            values.put("id",incidente.getId());
            values.put("usuario",incidente.getUsuario());
            values.put("fechayhora",incidente.getFechaYhora());
            values.put("tipo",incidente.getTipo());
            values.put("zona",incidente.getZona());
            values.put("latitud",incidente.getLatitud());
            values.put("longitud",incidente.getLongitud());
            values.put("imagen",ConvertirBitmapEnByteArray.convertir(incidente.getCaptura().getImagen()));
            bd.insert("incidentes",null,values);
        }
        bd.close();

    }

    public void insertarImagen(String usuario, Bitmap imagenParam){
        //inserto en blob
        try {
            //borro cualquier imagen que tiene el usuario primero (al principio un usuario iba a poder)
            //ya no se usa porque el usuario puede insertar varios incidentes
            SQLiteDatabase basededatos = this.getReadableDatabase();

            String deletePrimero = "DELETE FROM IMAGENES WHERE USUARIO = ?";
            basededatos.execSQL(deletePrimero, new String[]{usuario});

            String consultaInsert = "INSERT INTO IMAGENES (USUARIO,IMAGEN) VALUES (?,?)";
            SQLiteStatement sentencia = basededatos.compileStatement(consultaInsert);
            sentencia.clearBindings();

            //le paso la imagen y el usuario a la BD
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
    //pero costo trabajo jaj
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
        //BLOB es el tipo de dato de SQL para almacenar binarios
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
