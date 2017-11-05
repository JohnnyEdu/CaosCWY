package com.example5.lilian.caos_cwy.database;

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
import java.util.Iterator;

/**
 * Created by Johnny on 4/11/2017.
 */

/* Recibe un link para conectarse, en este caso es localhost/servidor_cwy_android/main.php*/
public class BDServidorPublico {
    private URL url;

    public BDServidorPublico(String urlLink){
        try{
            this.url = new URL(urlLink);
        }catch(MalformedURLException mue){
            //TODO: catch
        }
    }

    public HttpURLConnection conectarServidorPost(){
        HttpURLConnection conexion = null;
        try{
            conexion= (HttpURLConnection)this.url.openConnection();
            conexion.setReadTimeout(15000 /* milliseconds */);
            conexion.setConnectTimeout(15000 /* milliseconds */);
            conexion.setRequestMethod("POST");
            conexion.setDoInput(true);
            conexion.setDoOutput(true);

        }catch(Exception e){
            e.printStackTrace();
        }
        return conexion;
    }

    public String consultarIncidentesZona(String zona){
        String msgResp="";
        try{
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("zona", zona);
            realizarPeticion(postDataParams);
        }
        catch(JSONException json){
            msgResp = json.getMessage();
        }
        return msgResp;
    }


    public String insertarIncidente(Incidente incidente){
        String msgResp="";
        try{
            JSONObject postDataParams = new JSONObject();
            //TODO: hacer ID autoincremental en BD
            postDataParams.put("id", incidente.getId());
            postDataParams.put("usuario", incidente.getUsuario());
            postDataParams.put("tipo", incidente.getTipo());
            postDataParams.put("zona", incidente.getZona());
            realizarPeticion(postDataParams);
        }
        catch(JSONException json){
            msgResp = json.getMessage();
        }
        return msgResp;
    }
    //TODO: IMPORTANTE, no carga imagen en la BD todavía porque no encontre manera de enviarla al servidor PHP.
    public String insertarImagen(String usuario,byte[] imagen) {
        String msgResp="";
        try{
            JSONObject postDataParams = new JSONObject();
            //pasar parametro para CRUD
            //postDataParams.put("tipoconsulta", "insert");
            postDataParams.put("usuario", usuario);
            postDataParams.put("imagen", imagen);
            realizarPeticion(postDataParams);
        }
        catch(JSONException json){
            msgResp = json.getMessage();
        }
        return msgResp;
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
