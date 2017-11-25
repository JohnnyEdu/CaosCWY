package com.example5.lilian.caos_cwy.fragments;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example5.lilian.caos_cwy.MapsActivity;
import com.example5.lilian.caos_cwy.R;
import com.example5.lilian.caos_cwy.database.Incidente;
import com.example5.lilian.caos_cwy.dummy.IncidentesContent;
import com.example5.lilian.caos_cwy.incidenteDetailActivity;
import com.example5.lilian.caos_cwy.incidenteListActivity;
import com.example5.lilian.caos_cwy.tasks.ImagenesSELECTTask;

/**
 * A fragment representing a single incidente detail screen.
 * This fragment is either contained in a {@link incidenteListActivity}
 * in two-pane mode (on tablets) or a {@link incidenteDetailActivity}
 * on handsets.
 */
public class incidenteDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    //public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Incidente mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public incidenteDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(incidenteDetailActivity.ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = IncidentesContent.ITEM_MAP.get(getArguments().getString(incidenteDetailActivity.ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTipo());
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.incidente_detail, container, false);
        Button btnLocation2 = (Button)rootView.findViewById(R.id.btnLocation2);
        btnLocation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
        ImageView capturaTomada = (ImageView) rootView.findViewById(R.id.capturaTomada);
        ProgressBar progressBar = (ProgressBar)rootView.findViewById(R.id.cargandoimagen);


        //traer la captura del detalle
        /*ImagenesSELECTTask imgSelect = new ImagenesSELECTTask(getContext());
        imgSelect.setNroIncidente(String.valueOf(mItem.getId()));
        imgSelect.setVista(capturaTomada );
        imgSelect.setProgressBar(progressBar );
        imgSelect.execute();*/
        /**ya no se consulta denuevo en MySQL**/
        TextView comentarios = (TextView) rootView.findViewById(R.id.comentarios);
        TextView users = (TextView) rootView.findViewById(R.id.user);
        TextView date = (TextView) rootView.findViewById(R.id.date);
        TextView location = (TextView) rootView.findViewById(R.id.location);
        ImageView captura = (ImageView) rootView.findViewById(R.id.capturaTomada);
        comentarios.setText(mItem.getComentario());
        users.setText(mItem.getUsuario());
        date.setText(mItem.getFechaYhora());
        if(mItem.getCaptura()!=null) {
            captura.setImageBitmap(mItem.getCaptura().getImagen());
        }
        location.setText(mItem.getZona());
        return rootView;
    }
}
