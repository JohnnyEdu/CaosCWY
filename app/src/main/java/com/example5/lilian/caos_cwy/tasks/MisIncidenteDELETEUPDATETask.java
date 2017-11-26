package com.example5.lilian.caos_cwy.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example5.lilian.caos_cwy.MisIncidentesActivity;
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

public class MisIncidenteDELETEUPDATETask extends AsyncTask<Void, Void, Void> {
    private Activity activity;
    private ProgressBar progressBar;
    private String usuario;
    private Incidente incidente;
    private String tipoOperacion;

    public MisIncidenteDELETEUPDATETask(String tipoOperacion){
        this.tipoOperacion = tipoOperacion;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

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
    protected Void doInBackground(Void... voids) {
        if(tipoOperacion.equals("DELETE")){
            BDServidorPublico bdpub = BDServidorPublico.getInstancia("https://johnny032295.000webhostapp.com/servidor_cwy_android/eliminarIncidente.php",activity.getApplicationContext());
            bdpub.eliminarIncidenteDeServidor(getIncidente());
            /*DatabaseHelper dblocal = new DatabaseHelper(getActivity().getApplicationContext());
            dblocal.eliminarIncidente(incidente.getId());*/
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Toast.makeText(getActivity().getApplicationContext(),"Eliminado...",Toast.LENGTH_LONG).show();
        IncidentesContent.removeItem(incidente);
        incidenteListActivity.SimpleItemRecyclerViewAdapter adapter =
                new incidenteListActivity.SimpleItemRecyclerViewAdapter(
                        getActivity(),IncidentesContent.TODOS_LOS_INCIDENTES,false,""
                );
        RecyclerView recyclerView = (RecyclerView)getActivity().findViewById(R.id.incidente_list);
        recyclerView.setAdapter(adapter);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
