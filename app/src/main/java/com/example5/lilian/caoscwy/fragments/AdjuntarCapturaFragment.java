package com.example5.lilian.caoscwy.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example5.lilian.caoscwy.R;

/**
 * Created by Johnny on 2/11/2017.
 */

public class AdjuntarCapturaFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.adjuntar_imagen_fragment,container,false);
    }
}
