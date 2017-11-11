package com.example5.lilian.caos_cwy.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Johnny on 4/11/2017.
 */

/* Recibe un link para conectarse, en este caso es localhost/servidor_cwy_android/main.php*/
public class BDServidorPublico {
    private URL url;

    public BDServidorPublico(String urlLink) {
        try {
            this.url = new URL(urlLink);
        } catch (MalformedURLException mue) {
            //TODO: catch
        }
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
        }
        return conexion;
    }

    public List<Incidente> consultarIncidentesZona(String zona) {
        String msgResp = "";
        List<Incidente> resultado = new ArrayList<>();
        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("zona", zona);
            JSONArray incidentes = new JSONArray(realizarPeticion(postDataParams));
            for (int i = 0; i < incidentes.length(); i++) {
                Incidente incidente = new Incidente();
                JSONObject objecto = (JSONObject) incidentes.get(i);
                incidente.setId(Integer.valueOf(objecto.get("id").toString()));
                incidente.setTipo((String) objecto.get("tipo"));
                incidente.setZona((String) objecto.get("zona"));
                incidente.setCantidad(Integer.valueOf(objecto.get("cantidad").toString()));
                resultado.add(incidente);
            }
        } catch (JSONException json) {
            msgResp = json.getMessage();
        }

        return resultado;
    }


    public String insertarIncidente(Incidente incidente) {
        String msgResp = "";
        try {
            JSONObject postDataParams = new JSONObject();
            //TODO: hacer ID autoincremental en BD
            postDataParams.put("id", incidente.getId());
            postDataParams.put("usuario", incidente.getUsuario());
            postDataParams.put("tipo", incidente.getTipo());
            postDataParams.put("zona", incidente.getZona());
            postDataParams.put("comentario", incidente.getComentario());
            if(incidente.getCaptura()!=null){
                String base64encode = Base64.encodeToString(ConvertirBitmapEnByteArray.convertir(incidente.getCaptura().getImagen()), Base64.DEFAULT);
                postDataParams.put("imagen",base64encode);
            }
            realizarPeticion(postDataParams);
        } catch (JSONException json) {
            msgResp = json.getMessage();
        }
        return msgResp;
    }


    //@Deprecated no se usa ya que nunca se inserta una imagen sola sin incidente
    //Codifica a Base64 una imagen Bitmap y la envía al servidor para insertar en un MEDIUMTEXT de MySQL
    //MEDIUM TEXT ya que van a ser imágenes de:  64KB >  tamañoimagen < 16MB
    //@return string JSON, en base a los echo del PHP
    public String insertarImagen(String usuario, byte[] imagen, Integer id_incidente) {
        String msgResp = "";

        try {
            String base64encode = Base64.encodeToString(imagen, Base64.DEFAULT);
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("usuario", usuario);
            postDataParams.put("imagen", base64encode);
            postDataParams.put("id_incidente", id_incidente);
            //HttpConnection a la URL
            realizarPeticion(postDataParams);
        } catch (JSONException json) {
            msgResp = json.getMessage();
        }
        return msgResp;
    }


    private List<Captura> convertirBase64BD(String json) {
        List<Captura> capturas = new ArrayList<>();
        try {
            JSONArray respuesta  = new JSONArray(json);
            for(int i = 0; i < respuesta.length();i++){
                JSONObject obj = (JSONObject)respuesta.get(i);
                String base64bd = (String) obj.get("imagen");
                Bitmap img = ConvertirBitmapEnByteArray.convertirByteArrayToBitmap(Base64.decode(base64bd, Base64.DEFAULT));
                Captura captura = new Captura((String) obj.get("usuario"), img);
                capturas.add(captura);
            }
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        }
        return capturas;
    }


//trae las capturas de la tabla IMAGENES por usuario
    public List<Captura> selectImagenesPorUsuario(String usuario) {
        String msgResp="";
        List<Captura> capturas = null;
        try{
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("usuario", usuario);
            msgResp = realizarPeticion(postDataParams);
            capturas = convertirBase64BD(msgResp);

        }
        catch(JSONException json){
            msgResp = json.getMessage();
        }
        return capturas;
    }

    //trae todas las capturas de todos los usuarios
    //de la tabla IMAGENES
    public List<Captura> selectImagenes() {
        String msgResp="";
        List<Captura> capturas = null;
        try{
            msgResp = realizarPeticion(new JSONObject());
            JSONArray respuesta  = new JSONArray(msgResp);
            capturas = convertirBase64BD(msgResp);
        }
        catch(JSONException json){
            msgResp = json.getMessage();
        }
        return capturas;
    }



    //metodo genérico para consultar a un servidor
    public String realizarPeticion(JSONObject jsonparams){
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
        } catch (IOException ioE) {
            ioE.printStackTrace();
            msgResp = ioE.getMessage();
        } catch (Exception e ) {
             e.printStackTrace();
             msgResp = e.getMessage();
         }
        return msgResp;
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
