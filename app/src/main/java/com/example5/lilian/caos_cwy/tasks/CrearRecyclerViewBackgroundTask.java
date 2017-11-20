package com.example5.lilian.caos_cwy.tasks;

import android.app.ActionBar;
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
import com.example5.lilian.caos_cwy.incidenteListActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Johnny on 5/11/2017.
 */

public class CrearRecyclerViewBackgroundTask extends AsyncTask<Void, Void,Void> {

    private Activity activity;
    private incidenteListActivity.SimpleItemRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public CrearRecyclerViewBackgroundTask(Activity activity){
        this.activity = activity;
    }


    @Override
    protected Void doInBackground(Void... params) {
        adapter = new incidenteListActivity.SimpleItemRecyclerViewAdapter(getActivity(), IncidentesContent.TODOS_LOS_INCIDENTES,false, "");
        RecyclerView recyclerView = (RecyclerView)activity.findViewById(R.id.incidente_list);

        assert recyclerView != null;
        recyclerView.setAdapter(adapter);
        return null;
    }

    //se ejecuta en el hilo principal
    //sino operamos en el doInBackground: android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
