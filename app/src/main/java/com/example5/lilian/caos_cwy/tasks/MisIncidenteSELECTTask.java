package com.example5.lilian.caos_cwy.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example5.lilian.caos_cwy.R;
import com.example5.lilian.caos_cwy.database.BDServidorPublico;
import com.example5.lilian.caos_cwy.database.DatabaseHelper;
import com.example5.lilian.caos_cwy.database.Incidente;
import com.example5.lilian.caos_cwy.dummy.IncidentesContent;
import com.example5.lilian.caos_cwy.incidenteListActivity;

import java.util.ArrayList;

/**
 * Created by Johnny on 5/11/2017.
 */

public class MisIncidenteSELECTTask extends AsyncTask<Void, Void, ArrayList<Incidente>> {
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
        if(IncidentesContent.TODOS_LOS_INCIDENTES.isEmpty()) {
            //BDServidorPublico bdpub = new BDServidorPublico("https://johnny032295.000webhostapp.com/servidor_cwy_android/consultarIncidentes.php");
            //bdpub.consultarIncidentesUsuario(getUsuario())
            DatabaseHelper dblocal = new DatabaseHelper(getActivity().getApplicationContext());
            //BUSCARZONA
            resultado = dblocal.traerIncidentesLocalmentePorUsuario(usuario);
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

        /*IncidenteSELECTTask incidenteSELECTTask = new IncidenteSELECTTask();
        incidenteSELECTTask.setActivity(getActivity());
        incidenteSELECTTask.setProgressBar(progressBar);
        incidenteSELECTTask.execute();*/
    }

}
