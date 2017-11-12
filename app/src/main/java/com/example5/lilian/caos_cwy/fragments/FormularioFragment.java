package com.example5.lilian.caos_cwy.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example5.lilian.caos_cwy.MapsActivity;
import com.example5.lilian.caos_cwy.R;
import com.example5.lilian.caos_cwy.database.Captura;
import com.example5.lilian.caos_cwy.database.Incidente;
import com.example5.lilian.caos_cwy.tasks.IncidenteINSERTTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FormularioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FormularioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormularioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FormularioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormularioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FormularioFragment newInstance(String param1, String param2) {
        FormularioFragment fragment = new FormularioFragment();
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
        View fragm = inflater.inflate(R.layout.fragment_formulario, container, false);
        Button subitIncidente=  (Button)fragm.findViewById(R.id.compartir);
        subitIncidente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Spinner spinner = (Spinner)getActivity().findViewById(R.id.spAnimals);
                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("sesion",getActivity().getApplication().MODE_PRIVATE);
                final String usuario = sharedpreferences.getString("usuario","");
                EditText comentarios = (EditText)getActivity().findViewById(R.id.comentario);
                String comentario = comentarios != null?comentarios.getText().toString():"";


                Incidente incidente = new Incidente();
                incidente.setUsuario(usuario);
                incidente.setTipo(spinner.getSelectedItem().toString());
                incidente.setZona("PODRIAN SER COORDENADAS");
                incidente.setComentario(comentario);

                try{
                    ImageView img = (ImageView) getActivity().findViewById(R.id.imagenPrueba);
                    Bitmap imagen = ((BitmapDrawable)img.getDrawable()).getBitmap();
                    incidente.setCaptura(new Captura(usuario,imagen));
                }catch(Exception e){

                }

                IncidenteINSERTTask taskIncidente = new IncidenteINSERTTask();
                taskIncidente.execute(incidente);
        //TODO: ver para la entrega que no se guarda el ID porque tengo que hacerlo desde php


                Toast.makeText(getContext(),"Insertando en BD, checkear...",Toast.LENGTH_LONG).show();
            }
        });
        ImageButton botonVerMapa = (ImageButton)fragm.findViewById(R.id.verMapa);
        botonVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vermapa = new Intent(getContext(),MapsActivity.class);
                startActivity(vermapa);
            }
        });

        return fragm;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
