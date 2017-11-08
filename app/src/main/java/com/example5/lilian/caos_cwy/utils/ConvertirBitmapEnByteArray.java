package com.example5.lilian.caos_cwy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example5.lilian.caos_cwy.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Johnny on 4/11/2017.
 */

public class ConvertirBitmapEnByteArray {
    public static byte[] convertir(Bitmap imagenParam){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagenParam.compress(Bitmap.CompressFormat.JPEG, 50,baos);
        return baos.toByteArray();
    }
    public static Bitmap convertirByteArrayToBitmap(byte[] imagenParam){
        return BitmapFactory.decodeByteArray(imagenParam,0,imagenParam.length);
    }
}
