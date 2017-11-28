package com.example5.lilian.caos_cwy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;

public class FullscreenActivity extends AppCompatActivity {
    private final Handler mHideHandler = new Handler();
    private Configuration config = new Configuration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("sesion",MODE_PRIVATE);
        String lenguaje = sharedPreferences.getString("locale","");
        config.locale = new Locale(lenguaje);
        getResources().updateConfiguration(config, null);
        //oculto la barra de notficaciones
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //oculto la barra de titulo
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fullscreen);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try{
            SharedPreferences sharedpreferences = getSharedPreferences("sesion",MODE_PRIVATE);
            if(String.valueOf(sharedpreferences.getString("usuario","")).equals("")){
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }else{
                mHideHandler.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                }, 5000);
            }
        }catch(Exception e){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
    }
}
