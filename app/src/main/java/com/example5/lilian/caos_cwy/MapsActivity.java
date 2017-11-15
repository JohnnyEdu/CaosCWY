package com.example5.lilian.caos_cwy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //creo un objeto Marker para el marcador y declaro dos variables para la posicion actual dela longitud y latitud
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the Support   MapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();

    }
    // creo un metodo que sirve para agregar el marcador al mapa, creo un objeto Latlng en el cual se incluye la longitud y la latitud
    //luego utilizo el elemento CameraUpdate, para centrar la camara en la posisicon del marker.
    private void agregarMarcador(Double lat, Double lng) {
        final LatLng coordenadas= new LatLng(lat, lng);
        CameraUpdate miUbicacion=CameraUpdateFactory.newLatLngZoom(coordenadas,16);
        //si el marcador es diferente de null se debera remover. Agrego unas propiedades al marker, como titulo y una imagen
        //y se le agrega un metodo animateCamera para  mover la camara desde una posicion a otra.
        if (marcador != null)marcador.remove();
        marcador =mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Mi posición actual")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                mMap.animateCamera(miUbicacion);

        FloatingActionButton imgbtn = (FloatingActionButton)findViewById(R.id.coords);
        imgbtn.setVisibility(View.VISIBLE);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"guardando...",Toast.LENGTH_LONG).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("coordLat", String.valueOf(coordenadas.latitude));
                resultIntent.putExtra("coordLong", String.valueOf(coordenadas.longitude));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

    }
    //creo un metodo que sirve para obtener la longitud y la latitud de mi posicion actual utilizando los metodos metodos que proporciona
    //la clase Location, la cual utilizo como parametro en el medodo.
    //compruebo que la localizacion recibida es diferente de null, antes de asignar valor a las variables,
    // para evitar que la app se cierre al ejecutar.

    private void actualizarUbicacion(Location location){

        if (location!=null){
            lat=location.getLatitude();
            lng=location.getLongitude();
            agregarMarcador(lat,lng);
        }
    }

    //implemento un objeto de tipo LocationListener el cual tiene una funcion de estar atento a cualquier cambio de localidad
    // recibido del gps del dispositivo, al instanciar el objeto se crean automaticamente una serie de motodos asociados
    // a los distintos eventos que reciba el proveedor de localizacion
    LocationListener locListener = new LocationListener() {

        // este metodo se lanza cada vez que se recibe una actualizacion de la posicion
        //dentro de este metodo llamo a el metodo actualiarUbicacion. para refrescar la posicion actual del mapa.
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
//creo un metodo en el cual hacemos referencia a la clase LocationManager, la cual es utilizada para obtener servicion de geo posicionamiento
    //mediante el metodo  getLastKnowLocation obtengo la ultima posicion conocida y luego llamo al metodo
    //actualizarUbicacion, mediante el medotod requestLocationUpdates se solicita la GPS actualizaciones de la posicion cada 15 segundos.
    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000,0,locListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,0,locListener);


    }

}
