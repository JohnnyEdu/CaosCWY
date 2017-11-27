package com.example5.lilian.caos_cwy;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example5.lilian.caos_cwy.database.Incidente;
import com.example5.lilian.caos_cwy.dummy.IncidentesContent;
import com.example5.lilian.caos_cwy.fragments.FormularioFragment;

public class EditarIncidenteActivity extends AppCompatActivity
        implements FormularioFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_incidente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        String idIncidente = (String)getIntent().getStringExtra("id_incidente");
        Incidente incidente = IncidentesContent.ITEM_MAP.get(idIncidente);

        FormularioFragment formulario = new FormularioFragment();
        formulario.setEditar(true);
        formulario.setTipoIncidente(incidente.getTipo());
        formulario.setIncidenteEditar(incidente);
        if(incidente.getCaptura()!=null) {
            formulario.setCaptura(incidente.getCaptura().getImagen());
        }
        formulario.setComentario(incidente.getComentario());


        ViewGroup formularioContainer = (ViewGroup)findViewById(R.id.formularioEditarContainer);
        getSupportFragmentManager().beginTransaction()
                .add(formularioContainer.getId(),formulario)
                .commit();


        setSupportActionBar(toolbar);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
