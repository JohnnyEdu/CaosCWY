package com.example5.lilian.caos_cwy.utils;

import android.graphics.Bitmap;

import com.example5.lilian.caos_cwy.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Johnny on 4/11/2017.
 */

public class ConvertirBitmapEnByteArray {
    public static byte[] convertir(Bitmap imagenParam){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagenParam.compress(Bitmap.CompressFormat.JPEG, 100,baos);
        return baos.toByteArray();
    }
}