package com.example.deep.paintgame.utils;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    public static String getOkHttpResponseBodyString(String address) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}

