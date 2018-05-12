package com.example.deep.paintgame.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

/**
 * Created by Bencq on 2018/04/26.
 */

public class Utils {
    public static Bitmap loadBitmapFromView(View view) {
        int width = view.getWidth();
        int height = view.getHeight();

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        canvas.drawColor(Color.WHITE);
        /* 如果不设置canvas画布为白色，则生成透明 */

        view.layout(0, 0, width, height);
        view.draw(canvas);

        return bmp;
    }
}
