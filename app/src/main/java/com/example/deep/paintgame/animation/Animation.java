package com.example.deep.paintgame.animation;

import android.animation.AnimatorSet;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Bencq on 2018/05/17.
 */

public class Animation {

    public static final TranslateAnimation animation_swing = new TranslateAnimation(-10, 10, 0 ,0);
    public static final TranslateAnimation animation_shake_1 = new TranslateAnimation(-5, 5, 0 ,0);
    //public static final TranslateAnimation animation_shake_2 = new TranslateAnimation(5, -5, -5 ,5);

    public static final AnimatorSet animation_shake = new AnimatorSet();
    static
    {
        animation_swing.setInterpolator(new OvershootInterpolator());
        animation_swing.setDuration(100);
        animation_swing.setRepeatCount(3);
        animation_swing.setRepeatMode(android.view.animation.Animation.REVERSE);

        animation_shake_1.setInterpolator(new OvershootInterpolator());
        animation_shake_1.setDuration(50);
        animation_shake_1.setRepeatCount(2);
        animation_shake_1.setRepeatMode(android.view.animation.Animation.REVERSE);


        //animation_shake.playSequentially(animation_shake_1,animation_shake_2);
    }

    public static void startShakeByViewAnimation(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }

        //由小变大
        ScaleAnimation scaleAnimation = new ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge);
        //从左向右
        RotateAnimation rotateAnimation = new RotateAnimation(-shakeDegrees, shakeDegrees, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f,android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setDuration(duration);
        rotateAnimation.setDuration(duration / 10);
        rotateAnimation.setRepeatMode(android.view.animation.Animation.REVERSE);
        rotateAnimation.setRepeatCount(10);

        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnimation);
        smallAnimationSet.addAnimation(rotateAnimation);

        view.startAnimation(smallAnimationSet);
    }

}
