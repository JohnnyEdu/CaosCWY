package com.example5.lilian.cwy2.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example5.lilian.cwy2.FormularioFragment;
import com.example5.lilian.cwy2.Fragment1;
import com.example5.lilian.cwy2.Fragment2;
import com.example5.lilian.cwy2.R;
import com.example5.lilian.cwy2.adapters.SeccionesAdapter;
import com.example5.lilian.cwy2.clases.Utilidades;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link ContenedorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContenedorFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    //componente view
    View vista;
    //declaro Propiedades
    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;


    public ContenedorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContenedorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContenedorFragment newInstance(String param1, String param2) {
        ContenedorFragment fragment = new ContenedorFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_contenedor, container, false);


        if (Utilidades.rotacion== 0){
            View parent= (View) container.getParent();
            if (appBar == null){
                appBar= (AppBarLayout) parent.findViewById(R.id.appBar);
                pestanas = new TabLayout(getActivity());
                appBar.addView(pestanas);
                //vincula la logica de navegacion con el gesto de arrastre de pantalla, es necesario de contruir un adaptador
                viewPager= (ViewPager) vista.findViewById(R.id.idViewPagerInformacion);

                llenarViewPeger(viewPager);
                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                });
                pestanas.setupWithViewPager(viewPager);

            }
            pestanas.setTabGravity(TabLayout.GRAVITY_FILL);
        }else{
            Utilidades.rotacion=1;
        }

        return vista;
    }
//creamos un adaptador con los fragments que se van a mostrar en cada pestaña, con el metodo add se van a ir guardando en la lista
    private void llenarViewPeger(ViewPager viewPager) {
        SeccionesAdapter adapter = new SeccionesAdapter(getFragmentManager());
        adapter.addFragments(new Fragment1(),"celeste");
        adapter.addFragments(new FormularioFragment(), "formulario");
        adapter.addFragments(new Fragment2(), "naranja");

        viewPager.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    //para evitar que en el onCreate se agregue siempre el ViewPager
    public void onDestroyView() {
        super.onDestroyView();
        if (Utilidades.rotacion== 0){
        appBar.removeView(pestanas);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
