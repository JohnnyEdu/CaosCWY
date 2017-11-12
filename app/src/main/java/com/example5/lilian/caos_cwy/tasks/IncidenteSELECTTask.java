package com.example5.lilian.caos_cwy.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example5.lilian.caos_cwy.R;
import com.example5.lilian.caos_cwy.database.BDServidorPublico;
import com.example5.lilian.caos_cwy.database.Incidente;
import com.example5.lilian.caos_cwy.dummy.IncidentesContent;
import com.example5.lilian.caos_cwy.incidenteListActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Johnny on 5/11/2017.
 */

public class IncidenteSELECTTask extends AsyncTask<Boolean, Void, HashMap<String,List<Incidente>>> {
    private Activity activity;
    private Boolean mTwoPane;


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected HashMap<String,List<Incidente>> doInBackground(Boolean... mTwoPane) {
        //mTwoPane parametro que viene de la vista desde maestro detalle, es predeterminado de android
        this.mTwoPane = mTwoPane[0];
        BDServidorPublico bdpub = new BDServidorPublico("https://johnny032295.000webhostapp.com/servidor_cwy_android/consultarIncidentes.php");

        //BUSCARZONA
        HashMap<String,List<Incidente>> resultado = bdpub.consultarIncidentesZona("PODRIAN SER COORDENADAS");

        return resultado;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new incidenteListActivity.SimpleItemRecyclerViewAdapter((incidenteListActivity) getActivity(), IncidentesContent.ITEMS,IncidentesContent.ITEMS_SIN_AGRUPAR, mTwoPane));
    }

    @Override
    protected void onPostExecute(HashMap<String,List<Incidente>> incidentes) {
        for(Incidente incidente: incidentes.get("agrupada")){
            IncidentesContent.addItem(incidente);
        }
        for(Incidente incidente: incidentes.get("sinagrupar")){
            IncidentesContent.addItemSinAgrupar(incidente);
        }

        View recyclerView = activity.findViewById(R.id.incidente_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }


}
