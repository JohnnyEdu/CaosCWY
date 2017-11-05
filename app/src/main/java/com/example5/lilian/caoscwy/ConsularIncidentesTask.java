package com.example5.lilian.caoscwy;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example5.lilian.caoscwy.database.BDServidorPublico;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johnny on 5/11/2017.
 */

public class ConsularIncidentesTask extends AsyncTask<Void,Void,JSONObject>{
    private String zona;


    @Override
    protected JSONObject doInBackground(Void... voids) {
        if(getZona()!=null) {
            BDServidorPublico bdmysql = new BDServidorPublico("https://johnny032295.000webhostapp.com/servidor_cwy_android/consultarIncidentes.php");
            String result = bdmysql.consultarIncidentesZona(getZona());
            JSONObject json = null;
            try {
                json = new JSONObject(result);
            } catch (JSONException jse) {
                //TODO: CATCHEAR
            }
            return json;
        }else{
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject result) {

    }



    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }
}
