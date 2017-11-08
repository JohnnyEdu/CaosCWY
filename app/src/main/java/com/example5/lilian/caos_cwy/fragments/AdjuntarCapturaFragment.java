package com.example5.lilian.caos_cwy.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example5.lilian.caos_cwy.R;


import static android.app.Activity.RESULT_OK;

/**
 * Created by Johnny on 2/11/2017.
 */

public class AdjuntarCapturaFragment extends Fragment {
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private View vista;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragm = inflater.inflate(R.layout.adjuntar_imagen_fragment,container,false);
        vista = fragm;

        ImageButton eliminar = (ImageButton)vista.findViewById(R.id.eliminarFoto);
        eliminar.setVisibility(View.GONE);


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
        return  fragm;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //el Bundle toma los parametros del activity
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            final ImageView imageView = (ImageView)vista.findViewById(R.id.imagenPrueba);

            imageView.setImageBitmap(imageBitmap);

            /*ImagenesSELECTTask selectImg = new ImagenesSELECTTask(getActivity().getApplicationContext());
            ImageView img = (ImageView)getActivity().findViewById(R.id.imagenPrueba);
            selectImg.setVista(img);
            selectImg.execute();*/



            final ImageButton eliminar = (ImageButton)vista.findViewById(R.id.eliminarFoto);
            eliminar.setVisibility(View.VISIBLE);
            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageDrawable(null);
                    eliminar.setVisibility(View.GONE);
                }
            });
            //fin: insertar imagen
        }
    }
}
