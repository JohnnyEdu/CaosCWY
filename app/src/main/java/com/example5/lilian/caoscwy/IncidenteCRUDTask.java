package com.example5.lilian.caoscwy;

import android.os.AsyncTask;

import com.example5.lilian.caoscwy.database.BDServidorPublico;
import com.example5.lilian.caoscwy.database.Incidente;

/**
 * Created by Johnny on 5/11/2017.
 */

public class IncidenteCRUDTask extends AsyncTask<Incidente, Void, Void> {

    @Override
    protected Void doInBackground(Incidente... incidentes) {
        BDServidorPublico bdpub = new BDServidorPublico("https://johnny032295.000webhostapp.com/servidor_cwy_android/insertarincidenteapi.php");

        //TODO: preguntar al profe si es correcto iterar y conectar por cada incidente
        for (Incidente incidente : incidentes){
            bdpub.insertarIncidente(incidente);
        }
        return null;
    }
}
