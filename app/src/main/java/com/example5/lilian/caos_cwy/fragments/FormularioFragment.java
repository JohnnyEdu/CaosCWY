package com.example5.lilian.caos_cwy.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example5.lilian.caos_cwy.MainActivity;
import com.example5.lilian.caos_cwy.MapsActivity;
import com.example5.lilian.caos_cwy.R;
import com.example5.lilian.caos_cwy.database.Captura;
import com.example5.lilian.caos_cwy.database.Incidente;
import com.example5.lilian.caos_cwy.tasks.IncidenteINSERTTask;

import static android.app.Activity.RESULT_OK;


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
    private final int REQUEST_IMAGE_CAPTURE = 1;
    final int REQUEST_MAP= 2;
    private View fragm;


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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //el Bundle toma los parametros del activity
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //muestro el contenedor de la imagen
            final ImageView imageView = (ImageView)fragm.findViewById(R.id.imagenPrueba);
            imageView.setVisibility(View.VISIBLE);

            //oculto la cámara
            final Button capturarIncidente =  (Button)getActivity().findViewById(R.id.capturarIncidente);
            capturarIncidente.setVisibility(View.GONE);
            imageView.setImageBitmap(imageBitmap);


            //muestro el boton de eliminar imagen
            final ImageButton eliminar = (ImageButton)fragm.findViewById(R.id.eliminarFoto);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragm = inflater.inflate(R.layout.fragment_formulario, container, false);

        //oculto el contenedor de la imagen
        ImageView imgview = (ImageView) fragm.findViewById(R.id.imagenPrueba);
        imgview.setVisibility(View.GONE);

        //oculto el boton eliminar imagen
        ImageButton eliminar = (ImageButton)fragm.findViewById(R.id.eliminarFoto);
        eliminar.setVisibility(View.GONE);

        //boton cámara
        Button capturarIncidente =  (Button)fragm.findViewById(R.id.capturarIncidente);
        capturarIncidente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent abrirCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (abrirCamara.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(abrirCamara, REQUEST_IMAGE_CAPTURE);
                }
            }
        });


          /*<fragment
        android:id="@+id/adjuntarCaptura"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:name="com.example5.lilian.caos_cwy.fragments.AdjuntarCapturaFragment"
                />*/


        Button subitIncidente=  (Button)fragm.findViewById(R.id.compartir);
        subitIncidente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //si no se obtuvieron las coordenadas
                if(MainActivity.coordLat == null || MainActivity.coordLat.isEmpty()
                        || MainActivity.coordLong == null || MainActivity.coordLong.isEmpty()){
                    Toast.makeText(getContext(),"Tenés que adjuntar una ubicación",Toast.LENGTH_LONG).show();
                    return ;
                }

                final Spinner spinner = (Spinner)getActivity().findViewById(R.id.spAnimals);
                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("sesion",getActivity().getApplication().MODE_PRIVATE);
                final String usuario = sharedpreferences.getString("usuario","");
                EditText comentarios = (EditText)getActivity().findViewById(R.id.comentario);
                String comentario = comentarios != null?comentarios.getText().toString():"";
                //se crea un incidente para guardarlo en la BD MySQL

                Incidente incidente = new Incidente();
                incidente.setUsuario(usuario);
                incidente.setTipo(spinner.getSelectedItem().toString());

                //coordLat y coordLong , las seteo en MainActivity cuando en el activity del Mapa, se le da al boton flotante
                //linea 77 de MapsActivity y lo agarra MainActivity en la linea 67
                incidente.setZona(MainActivity.coordLat + "/" + MainActivity.coordLong);
                incidente.setLatitud(Double.valueOf(MainActivity.coordLat));
                incidente.setLongitud(Double.valueOf(MainActivity.coordLong));
                incidente.setComentario(comentario);

                //le seteo la imagen al Incidente, tomando la que puse en el ImageView
                try{
                    ImageView img = (ImageView) getActivity().findViewById(R.id.imagenPrueba);
                    Bitmap imagen = ((BitmapDrawable)img.getDrawable()).getBitmap();
                    if(imagen!=null ){
                        incidente.setCaptura(new Captura(usuario, imagen));
                    }
                }catch(Exception e){

                }
                IncidenteINSERTTask taskIncidente = new IncidenteINSERTTask();
                taskIncidente.execute(incidente);
        //TODO: ver para la entrega que no se guarda el ID porque tengo que hacerlo desde php


                Toast.makeText(getContext(),"Guardando...",Toast.LENGTH_LONG).show();

            }
        });

        //boton de "Zona", abre el activity del mapa para setear coordLag y coordLong
        Button btnLocation = (Button)fragm.findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vermapa = new Intent(getContext(),MapsActivity.class);
                startActivityForResult(vermapa,REQUEST_MAP);
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
