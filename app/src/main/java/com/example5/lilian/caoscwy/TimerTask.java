package com.example5.lilian.caoscwy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;

/**
 * Created by Johnny on 5/11/2017.
 */

public class TimerTask extends Service {
    private Timer timer = new Timer();


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        /* timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public @StartResult int onStartCommand{
               ConsularIncidentesTask incidenteHelper = new ConsularIncidentesTask();
               incidenteHelper.setZona("PODRIAN SER COORDENADAS");
               incidenteHelper.execute();
            }
        }, 0, 1*00*1000);//5 Minutes*/
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
