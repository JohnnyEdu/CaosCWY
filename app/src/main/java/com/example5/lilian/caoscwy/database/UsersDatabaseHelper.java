package com.example5.lilian.caoscwy.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example5.lilian.caoscwy.encriptado.AES256Cipher;

/**
 * Created by Johnny on 28/10/2017.
 */

public class UsersDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String BASE_DE_DATOS = "cwy";
    private static final String TABLA_PRINCIPAL="USUARIOS";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+TABLA_PRINCIPAL+"(USUARIO TEXT PRIMARY KEY," +
                    "PASSWORD TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS "+TABLA_PRINCIPAL;

    public UsersDatabaseHelper(Context context) {
        super(context, BASE_DE_DATOS, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
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
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //traer un usuario por su username
    public User getUsuario(String usuario){
        SQLiteDatabase basededatos = this.getReadableDatabase();
        String consultaSelect = "SELECT * FROM "+ TABLA_PRINCIPAL + " WHERE USUARIO = ?";
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
            e.printStackTrace();
        }
        basededatos.close();
        return user;
    }
}
