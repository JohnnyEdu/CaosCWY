package com.example5.lilian.caos_cwy.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import com.example5.lilian.caos_cwy.utils.ConvertirBitmapEnByteArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Johnny on 4/11/2017.
 */

/* Recibe un link para conectarse, en este caso es localhost/servidor_cwy_android/main.php*/
public class BDServidorPublico {
    private static URL url;
    private static Context appContext;
    private static BDServidorPublico instanciaSingleton;

    public static BDServidorPublico getInstancia(String urlLink,Context context){
        if(instanciaSingleton==null){
            synchronized (BDServidorPublico.class){
                if(instanciaSingleton==null){
                    instanciaSingleton = new BDServidorPublico();
                }
            }
        }
        try {
            instanciaSingleton.url = new URL(urlLink);
            instanciaSingleton.appContext = context;
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            //TODO: mostrar mensaje
        }
        return instanciaSingleton;
    }


    public static URL getUrl() {
        return url;
    }

    public static void setUrl(URL url) {
        BDServidorPublico.url = url;
    }

    private BDServidorPublico() {
    }

    public HttpURLConnection conectarServidorPost() {
        HttpURLConnection conexion = null;
        try {
            conexion = (HttpURLConnection) this.url.openConnection();
            conexion.setReadTimeout(15000 /* milliseconds */);
            conexion.setConnectTimeout(15000 /* milliseconds */);
            conexion.setRequestMethod("POST");
            conexion.setDoInput(true);
            conexion.setDoOutput(true);

        } catch (Exception e) {
            e.printStackTrace();
            //TODO: mostrar mensaje
        }
        return conexion;
    }
/**
 * Toma un JSONObject y lo transforma en un Incidente
 * @param json
 * @return {@link Incidente}
 * */
    private Incidente mapIncidenteFromDB(JSONObject json){
        Incidente incidente = new Incidente();
        try {
            incidente.setId(Integer.valueOf(json.get("id").toString()));
            incidente.setTipo((String) json.get("tipo"));
            incidente.setUsuario((String) json.get("usuario"));
            incidente.setZona((String) json.get("zona"));
            incidente.setComentario(String.valueOf(json.get("comentarios")));
            incidente.setFechaYhora(String.valueOf(json.get("fechaYhora")));
            if (json.get("imagen") != null
                    && !"null".equalsIgnoreCase(String.valueOf(json.get("imagen")))
                    && !"".equals(json.get("imagen"))) {
                Bitmap img = ConvertirBitmapEnByteArray.convertirByteArrayToBitmap(Base64.decode(
                        String.valueOf(json.get("imagen")), Base64.DEFAULT));
                Captura captura = new Captura(incidente.getUsuario(), img);
                incidente.setCaptura(captura);
            }
        }catch(Exception jsone){
            jsone.printStackTrace();
            incidente = null;
        }
        return incidente;
    }
    public ArrayList<Incidente> consultarIncidentesUsuario(String usuario){
        ArrayList<Incidente> resultado = new ArrayList<>();
        JSONObject param = new JSONObject();
        try {
            param.put("usuario", usuario);
            JSONArray resultadoQuery = realizarPeticion(param);
            for (int i = 0; i < resultadoQuery.length(); i++) {
                resultado.add(mapIncidenteFromDB((JSONObject) resultadoQuery.get(i)));
            }
        }catch(Exception jsone){
            jsone.printStackTrace();
            resultado = null;
        }

        return resultado;
    }

    public void eliminarIncidenteDeServidor(Incidente incidente){
        ArrayList<Incidente> resultado = new ArrayList<>();
        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("id_incidente", incidente.getId());
            realizarPeticion(postDataParams);
        }catch(Exception e){
            resultado = null;
            e.printStackTrace();
        }
    }


    public ArrayList<Incidente> consultarIncidentesZona(Double latitud, Double longitud) {
        ArrayList<Incidente> resultado = new ArrayList<>();
        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("latitud", latitud);
            postDataParams.put("longitud", longitud);
            JSONArray incidentes  = realizarPeticion(postDataParams);
            for (int i = 0; i < incidentes.length(); i++) {
                Incidente incidente = mapIncidenteFromDB((JSONObject) incidentes.get(i));
                resultado.add(incidente);
            }
        } catch (Exception json) {
            json.printStackTrace();
            resultado = null;
        }

        return resultado;
    }

    public void editarIncidente(){

    }

    public void insertarIncidente(Incidente incidente) {
        try {
            JSONObject postDataParams = new JSONObject();
            //TODO: hacer ID autoincremental en BD
            postDataParams.put("id", incidente.getId());
            postDataParams.put("usuario", incidente.getUsuario());
            postDataParams.put("tipo", incidente.getTipo());
            postDataParams.put("zona", incidente.getZona());
            postDataParams.put("latitud", incidente.getLatitud());
            postDataParams.put("longitud", incidente.getLongitud());
            postDataParams.put("comentario", incidente.getComentario());
            if(incidente.getCaptura()!=null){
                String base64encode = Base64.encodeToString(ConvertirBitmapEnByteArray.convertir(incidente.getCaptura().getImagen()), Base64.DEFAULT);
                postDataParams.put("imagen",base64encode);
            }
            realizarPeticion(postDataParams);
        } catch (Exception json) {
            json.printStackTrace();
            //TODO: mostrar mensaje
        }
    }


    //@Deprecated no se usa ya que nunca se inserta una imagen sola sin incidente
    //Codifica a Base64 una imagen Bitmap y la envía al servidor para insertar en un MEDIUMTEXT de MySQL
    //MEDIUM TEXT ya que van a ser imágenes de:  64KB >  tamañoimagen < 16MB
    //@return string JSON, en base a los echo del PHP
    public void insertarImagen(String usuario, byte[] imagen, Integer id_incidente) {
        try {
            String base64encode = Base64.encodeToString(imagen, Base64.DEFAULT);
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("usuario", usuario);
            postDataParams.put("imagen", base64encode);
            postDataParams.put("id_incidente", id_incidente);
            //HttpConnection a la URL
            realizarPeticion(postDataParams);
        } catch (Exception json) {
            json.printStackTrace();
            //TODO: mostrar mensaje
        }
    }


    private List<Captura> convertirBase64BD(JSONArray json) throws Exception{
        List<Captura> capturas = new ArrayList<>();
        for(int i = 0; i < json.length();i++){
            JSONObject obj = (JSONObject)json.get(i);
            String base64bd = (String) obj.get("imagen");
            Bitmap img = ConvertirBitmapEnByteArray.convertirByteArrayToBitmap(Base64.decode(base64bd, Base64.DEFAULT));
            Captura captura = new Captura((String) obj.get("usuario"), img);
            capturas.add(captura);
        }
        return capturas;
    }


    public List<Captura> selectImagenesPorIncidente(String nroIncidente) {
        List<Captura> capturas = null;
        try{
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("nroincidente", nroIncidente);
            JSONArray respuesta = realizarPeticion(postDataParams);
            capturas = convertirBase64BD(respuesta);

        }
        catch(Exception json){
            json.printStackTrace();
            capturas = null;
        }
        return capturas;
    }


    //trae las capturas de la tabla IMAGENES por usuario
    public List<Captura> selectImagenesPorUsuario(String usuario) {
        List<Captura> capturas = null;
        try{
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("usuario", usuario);
            JSONArray msgResp = realizarPeticion(postDataParams);
            capturas = convertirBase64BD(msgResp);

        }
        catch(Exception json){
            json.printStackTrace();
            capturas = null;
        }
        return capturas;
    }

    //trae todas las capturas de todos los usuarios
    //de la tabla IMAGENES
    public List<Captura> selectImagenes() {
        List<Captura> capturas = null;
        try{
            JSONArray msgResp = realizarPeticion(new JSONObject());
            capturas = convertirBase64BD(msgResp);
        }
        catch(Exception json){
            capturas = null;
            json.printStackTrace();
        }
        return capturas;
    }

    public JSONArray parseResponseToJsonArray(String response){
        JSONArray result = new JSONArray();
        try{
            result = new JSONArray(response);
        }catch(Exception jsone){
            try{
                JSONObject resultOne = new JSONObject(response);
                result.put(resultOne);
            }catch(JSONException e){
                e.printStackTrace();
                result = null;
            }
        }
        return result;
    }



    //metodo genérico para consultar a un servidor
    public JSONArray realizarPeticion(JSONObject jsonparams){
        HttpURLConnection conexion= conectarServidorPost();
        String msgResp = "";
         try {
            OutputStream os = conexion.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(crearParametrosEnUrl(jsonparams));
            writer.flush();
            writer.close();
            os.close();

            int responseCode= conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in=new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";
                while((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                msgResp= sb.toString();
            }else{
                msgResp = "No se obtuvo respuesta";
            }
            //TODO: mostrar msj
        } catch (IOException ioE) {
            ioE.printStackTrace();
        } catch (Exception e ) {
             e.printStackTrace();
         }
         JSONArray respuestaJSONJava =parseResponseToJsonArray(msgResp);
        return respuestaJSONJava;
    }

    //crea a partir de un JSON Oject un request string con query params
    public String crearParametrosEnUrl(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
