package com.example.deep.paintgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;

import java.util.Timer;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    public static final String KEY_MUSIC_RADIO = "key_musicRadio";
    public static final String KEY_MUSIC_SWITCH = "key_musicSwitch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageButton imageButton_main_StartGame = findViewById(R.id.imageButton_main_StartGame);
        final ImageButton imageButton_main_ManageProblem = findViewById(R.id.imageButton_main_ManageProblem);
        final ImageButton imageButton_main_GameHelp = findViewById(R.id.imageButton_main_GameHelp);
        final ImageButton imageButton_main_Settings = findViewById(R.id.imageButton_main_Settings);

        dealFirstTimeRunApp();

        imageButton_main_StartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation_alpha = new AlphaAnimation(0.1f,1.0f);
                animation_alpha.setDuration(100);
                animation_alpha.setRepeatCount(4);
                animation_alpha.setRepeatMode(Animation.REVERSE);

                animation_alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(MainActivity.this, StartGameActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                imageButton_main_StartGame.startAnimation(animation_alpha);


            }
        });

        imageButton_main_ManageProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation_alpha = new AlphaAnimation(0.1f,1.0f);
                animation_alpha.setDuration(100);
                animation_alpha.setRepeatCount(4);
                animation_alpha.setRepeatMode(Animation.REVERSE);

                animation_alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(MainActivity.this, ManageProblemActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                imageButton_main_ManageProblem.startAnimation(animation_alpha);


            }
        });

        imageButton_main_GameHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Animation animation_alpha = new AlphaAnimation(0.1f,1.0f);
                animation_alpha.setDuration(100);
                animation_alpha.setRepeatCount(4);
                animation_alpha.setRepeatMode(Animation.REVERSE);

                animation_alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        //debug
                        // 启动游戏帮助Activity
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                imageButton_main_GameHelp.startAnimation(animation_alpha);

            }
        });

        imageButton_main_Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation_alpha = new AlphaAnimation(0.1f,1.0f);
                animation_alpha.setDuration(100);
                animation_alpha.setRepeatCount(4);
                animation_alpha.setRepeatMode(Animation.REVERSE);

                animation_alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //debug
                        //Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        //startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                imageButton_main_Settings.startAnimation(animation_alpha);
            }
        });
    }


    //初始化游戏数据 完成后将该函数debug处putBoolean 改为false
    public void dealFirstTimeRunApp()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean isFirstTimeRunApp = sharedPreferences.getBoolean("isFirstTimeRunApp",true);
        Log.d(TAG, "dealFirstTimeRunApp: " + "isFirstTimeRunApp: " + isFirstTimeRunApp);

        //第一次启动程序
        if(isFirstTimeRunApp)
        {
            //题目名
            String problemNames[] = {"test","origin_1","origin_2","origin_3"};
            //题目尺寸
            int problemSizes[] = {8,10,6,12};
            //题目数据
            String problemData[] =
                    {
                            "04040400*04040400*44444444*04040400*04040400*04444440*04444440*04444440*",
                            "4444444444*4444444444*4444444444*4444444444*4444444444*0000000000*0000000000*0000000000*0000000000*0000000000*",
                            "040404*040404*040404*040404*040404*040404*",
                            "040440400440*040440400440*040440400440*040440400440*040440400440*040440400440*040440400440*040440400440*040440400440*040440400440*040440400440*040440400440*"
                    };


            //处理题目名字放到SP中
            SharedPreferences.Editor editor = sharedPreferences.edit();
            StringBuilder stringBuilder = new StringBuilder();

            for (String problemName : problemNames) {
                stringBuilder.append(problemName).append("*");
            }
            editor.putString("problemNames",stringBuilder.toString());
            editor.putBoolean("isFirstTimeRunApp",false);//debug
            editor.apply();


            //初始化设置
            editor.putInt(KEY_MUSIC_RADIO,1);
            editor.putBoolean(KEY_MUSIC_SWITCH,true);


            //SP记录具体每个题目的数据
            for (int i = 0; i < problemNames.length; ++i)
            {
                String name = problemNames[i];
                SharedPreferences sharedPreferences_problem = getSharedPreferences("problem_" + name, MODE_PRIVATE);
                SharedPreferences.Editor editor_problem = sharedPreferences_problem.edit();
                editor_problem.putInt("size", problemSizes[i]);
                editor_problem.putString("data", problemData[i]);
                editor_problem.apply();
            }

            //debug
            //处理题目初始图片



        }
    }
}
