package com.example5.lilian.cwy2.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lilian on 05/11/2017.
 */

public class SeccionesAdapter extends FragmentStatePagerAdapter {
//
    private final List<Fragment> listaFragments = new ArrayList<>();
    private final List<String> listaTitulos = new ArrayList<>();
    public SeccionesAdapter(FragmentManager fm) {
        super(fm);
    }

    // se van a ir guardando dependiendo de la posicion con que se vayan guardando
    public void addFragments(Fragment fragment, String titulo){
        listaFragments.add(fragment);
        listaTitulos.add(titulo);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return listaTitulos.get(position);
    }
//este metodo va a retornar de la lista de fragments en fragment que esta en la posicion
    @Override
    public Fragment getItem(int position) {
        return listaFragments.get(position);
    }
// cuantos fragmentos voy a mostrar con sea con viewPager o con pestañas
    @Override
    public int getCount() {
        return listaFragments.size();
    }
}
