package com.example5.lilian.caos_cwy.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example5.lilian.caos_cwy.R;
import com.example5.lilian.caos_cwy.database.BDServidorPublico;
import com.example5.lilian.caos_cwy.database.Incidente;
import com.example5.lilian.caos_cwy.dummy.IncidentesContent;
import com.example5.lilian.caos_cwy.fragments.ListadoIncidentesFragment;
import com.example5.lilian.caos_cwy.incidenteListActivity;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Johnny on 5/11/2017.
 */

public class IncidenteSELECTTask extends AsyncTask<Void, Void, ArrayList<Incidente>> {
    private Activity activity;
    private ProgressBar progressBar;
    private String usuario;


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    protected ArrayList<Incidente> doInBackground(Void... voids) {
        IncidentesContent.reset();
        //mTwoPane parametro que viene de la vista desde maestro detalle, es predeterminado de android
        ArrayList<Incidente> resultado = null;
        BDServidorPublico bdpub = BDServidorPublico.getInstancia("https://johnny032295.000webhostapp.com/servidor_cwy_android/consultarIncidentes2.php",activity.getApplicationContext());
        //BUSCARZONA
        Log.d("SELECT","#######trayendo incidentes ########");
        Log.i("SELECT","#######trayendo incidentes ########");
        if(usuario!=null) {
            resultado = bdpub.consultarIncidentesUsuario(usuario);
        }else{
            //TODO: pasar coordenadas de verdad
            resultado = bdpub.consultarIncidentesZona(-35.0,-58.0);
        }
        return resultado;
    }

    @Override
    protected void onPostExecute(ArrayList<Incidente> incidentes) {
        if(incidentes==null){
            Toast.makeText(getActivity().getApplicationContext(),"Error en el servidor",Toast.LENGTH_LONG).show();
        }else{
            IncidentesContent.reset();
            progressBar.setVisibility(View.GONE);
            IncidentesContent.TODOS_LOS_INCIDENTES = incidentes;
            IncidentesContent.fillContent();
            incidenteListActivity.SimpleItemRecyclerViewAdapter adapter =
                    new incidenteListActivity.SimpleItemRecyclerViewAdapter(
                            getActivity(),IncidentesContent.TODOS_LOS_INCIDENTES,false,""
                    );
            RecyclerView recyclerView = (RecyclerView)getActivity().findViewById(R.id.incidente_list);
            recyclerView.setAdapter(adapter);
        }
    }

}
