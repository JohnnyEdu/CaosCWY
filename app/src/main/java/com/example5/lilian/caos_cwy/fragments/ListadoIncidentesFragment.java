package com.example5.lilian.caos_cwy.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example5.lilian.caos_cwy.R;
import com.example5.lilian.caos_cwy.dummy.IncidentesContent;
import com.example5.lilian.caos_cwy.tasks.IncidenteSELECTTask;

import java.util.List;

/**
 * Created by Johnny on 15/11/2017.
 */

public class ListadoIncidentesFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListadoIncidentesFragment.OnFragmentInteractionListener mListener;

    /***
     * Esta clase, es la representacion en fragment de incidenteListActivity (maestro del detalle)
     * Se comentó en incidenteListActivity la linea 136, ya que intenta adjuntar el detalle en un fragment y
     * cuando se implementó la representación del maestro en Fragment, el activity padre (mParentActivity de incidenteListActivity)
     * es el MainActivity que extiende de Activity y no tiene el método getSupportFragmentManager, ya que no es un FragmentActivity
     * como incidenteListActivity (herencia en cascada)  public class incidenteListActivity extends AppCompatActivity  y
     *AppCompatActivity extends FragmentActivity implements AppCompatCallback
     *
     * **/

    public ListadoIncidentesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static ListadoIncidentesFragment newInstance(String param1, String param2) {
        ListadoIncidentesFragment fragment = new ListadoIncidentesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vista = inflater.inflate(R.layout.listado_incidentes_fragment, container, false);
        //inicializo el contenedor de los incidentes del maestro - detalle
        IncidentesContent inc = new IncidentesContent();

        if (vista.findViewById(R.id.incidente_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
        }

        /***Aca hace exactamente lo mismo que el maestro / detalle original , va a buscar a la base
         * y cuando vuelve, crea los RecyclerView
        ***/

        /**ahora lo ejecuto en el click del tab**/
        /*ProgressBar progressBar = (ProgressBar)vista.findViewById(R.id.cargandoIncidentes);
        //traer los incidentes para mostrar
        IncidenteSELECTTask selct = new IncidenteSELECTTask();
        selct.setActivity(getActivity());
        selct.setProgressBar(progressBar);
        selct.execute(false);
*/
        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}