package com.example5.lilian.caos_cwy.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

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

public class IncidenteSELECTTask extends AsyncTask<Boolean, Void, ArrayList<Incidente>> {
    private Activity activity;
    private Boolean mTwoPane;
    private ProgressBar progressBar;


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    protected ArrayList<Incidente> doInBackground(Boolean... mTwoPane) {
        IncidentesContent.reset();
        //mTwoPane parametro que viene de la vista desde maestro detalle, es predeterminado de android
        this.mTwoPane = mTwoPane[0];
        ArrayList<Incidente> resultado = null;
        if(IncidentesContent.TODOS_LOS_INCIDENTES.isEmpty()) {
            BDServidorPublico bdpub = new BDServidorPublico("https://johnny032295.000webhostapp.com/servidor_cwy_android/consultarIncidentes.php");

            //BUSCARZONA
            resultado = bdpub.consultarIncidentesZona(-35.0, -58.0);

        }else{
            resultado = new ArrayList<>();
        }
        return resultado;
    }

    @Override
    protected void onPostExecute(ArrayList<Incidente> incidentes) {
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
