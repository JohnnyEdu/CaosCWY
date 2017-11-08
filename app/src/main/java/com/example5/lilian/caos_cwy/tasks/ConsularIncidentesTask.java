package com.example5.lilian.caos_cwy.tasks;

import android.os.AsyncTask;

import com.example5.lilian.caos_cwy.database.BDServidorPublico;
import com.example5.lilian.caos_cwy.database.Incidente;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Johnny on 5/11/2017.
 */

public class ConsularIncidentesTask extends AsyncTask<Void,Void,List<Incidente>>{
    private String zona;


    @Override
    protected List<Incidente> doInBackground(Void... voids) {
        if(getZona()!=null) {
            BDServidorPublico bdmysql = new BDServidorPublico("https://johnny032295.000webhostapp.com/servidor_cwy_android/consultarIncidentes.php");
            List<Incidente> result = bdmysql.consultarIncidentesZona(getZona());
            return result;
        }else{
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Incidente> result) {

    }



    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }
}
