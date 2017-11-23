package com.example5.lilian.caos_cwy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example5.lilian.caos_cwy.fragments.FormularioFragment;
import com.example5.lilian.caos_cwy.fragments.Fragment1;
import com.example5.lilian.caos_cwy.fragments.Fragment2;
import com.example5.lilian.caos_cwy.utils.Utilidades;
import com.example5.lilian.caos_cwy.fragments.ContenedorFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Fragment1.OnFragmentInteractionListener,
        Fragment2.OnFragmentInteractionListener ,
        FormularioFragment.OnFragmentInteractionListener ,
        ContenedorFragment.OnFragmentInteractionListener {

    int REQUEST_IMAGE_CAPTURE = 1;
    public static String coordLat;
    public static String coordLong;
    private boolean resetTabs = false;

    @Override
    protected void onResume() {

        super.onResume();
        if(resetTabs){
            Fragment  fragment = new ContenedorFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //cuando haces click en el boton de la lupa para buscar incidentes en tu zona
        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent verIncidentes = new Intent(getApplicationContext(),incidenteListActivity.class);
                startActivity(verIncidentes);
            }
        };
        fab.setOnClickListener(onclick);*/



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        /********
         * Aca inicia los tabs, poniendo el de formulario primero y lo pega en content_main
         * *******/
        //if (Utilidades.validaPantalla== true){
            //Fragment  fragment = new com.example5.lilian.caos_cwy.fragments.FormularioFragment();

        //}
        Fragment  fragment = new ContenedorFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
        //Utilidades.validaPantalla=false;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mis incidentes
        if(requestCode == 3){
           resetTabs = true;
        }
        //camara
        else if(requestCode == 1){
            //el Bundle toma los parametros del activity
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //muestro el contenedor de la imagen
            final ImageView imageView = (ImageView)findViewById(R.id.imagenPrueba);
            imageView.setVisibility(View.VISIBLE);

            //oculto la cámara
            final Button capturarIncidente =  (Button)findViewById(R.id.capturarIncidente);
            capturarIncidente.setVisibility(View.GONE);
            imageView.setImageBitmap(imageBitmap);


            //muestro el boton de eliminar imagen
            final ImageButton eliminar = (ImageButton)findViewById(R.id.eliminarFoto);
            eliminar.setVisibility(View.VISIBLE);
            //logica para la crucecita de borrar imagen
            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageDrawable(null);
                    imageView.setVisibility(View.GONE);
                    eliminar.setVisibility(View.GONE);
                    capturarIncidente.setVisibility(View.VISIBLE);

                }
            });

        }
        else{
            //mapa
            if(data!=null) {
                /***vuelta del mis incidentes ***/
                //tomo las coordenadas que devuelve el MapsActivity cuando se la llama desde el click
                //en el boton "Zona", ver el código de MapsActivity cuando se le da click al boton R.id.coords
                if (data.getStringExtra("coordLat") != null && !data.getStringExtra("coordLat").equals("")) {
                    coordLat = data.getStringExtra("coordLat");
                }
                if (data.getStringExtra("coordLong") != null && !data.getStringExtra("coordLong").equals("")) {
                    coordLong = data.getStringExtra("coordLong");
                }
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.logout){
            SharedPreferences sharedPreferences = getSharedPreferences("sesion",MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove("usuario");
            //se podria utilizar apply para hacerlo en background.
            edit.commit();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            this.finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //indica cuando selecciono algun elemento del menu
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment miFragment = null;
        boolean fragmentSeleccionado = false;


        if (id == R.id.nav_camera) {
            miFragment = new FormularioFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_slideshow) {
            miFragment = new Fragment2();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_share) {
            miFragment = new ContenedorFragment();
            fragmentSeleccionado= true;
        } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.verincidentesmenulateral) {
            //en el menu lateral, para ver los incidentes por zona tambien
            Intent verIncidentes = new Intent(getApplicationContext(),incidenteListActivity.class);
            startActivity(verIncidentes);
        }

        if (fragmentSeleccionado == true){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, miFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        
    }
}
