package com.example5.lilian.caos_cwy.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example5.lilian.caos_cwy.database.BDServidorPublico;
import com.example5.lilian.caos_cwy.database.DatabaseHelper;
import com.example5.lilian.caos_cwy.database.Incidente;

/**
 * Created by Johnny on 5/11/2017.
 */

public class IncidenteINSERTTask extends AsyncTask<Incidente, Void, Void> {
    private Activity activity;


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Incidente... incidentes) {
        BDServidorPublico bdpub = BDServidorPublico.getInstancia("https://johnny032295.000webhostapp.com/servidor_cwy_android/insertarincidenteapi.php",activity.getApplicationContext());

        //TODO: preguntar al profe si es correcto iterar y conectar por cada incidente
        for (Incidente incidente : incidentes){
            Log.d("INSERTAR","#######insertando incidente ########");
            bdpub.insertarIncidente(incidente);
           /* DatabaseHelper dblocal = new DatabaseHelper(getActivity().getApplicationContext());
            dblocal.guardarIncidentesLocalmente(incidente);*/
        }
        return null;
    }
}
