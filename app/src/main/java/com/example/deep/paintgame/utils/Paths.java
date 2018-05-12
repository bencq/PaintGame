package com.example.deep.paintgame.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by Bencq on 2018/04/27.
 */

public class Paths {

    public static String getImageFileName(String problemName)
    {
        return "img_" + problemName + ".jpg";
    }
    public static File getImageFile(Context context,String problemName)
    {
        return new File(context.getApplicationContext().getFilesDir(),getImageFileName(problemName));
    }
}
