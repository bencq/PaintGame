package com.example.deep.paintgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;

import com.example.deep.paintgame.utils.Paths;
import com.example.deep.paintgame.utils.SHA1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    public static final String KEY_MUSIC_RADIO = "key_musicRadio";
    public static final String KEY_MUSIC_SWITCH = "key_musicSwitch";
    public static final String KEY_SOUND_EFFECT="key_soundeffect";

    //题目名
    public static final String problemNames[] = {"heart","mario","tortoise","mushroom"};

    MediaPlayer mediaPlayer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageButton imageButton_main_StartGame = findViewById(R.id.imageButton_main_StartGame);
        final ImageButton imageButton_main_ManageProblem = findViewById(R.id.imageButton_main_ManageProblem);
        final ImageButton imageButton_main_GameHelp = findViewById(R.id.imageButton_main_GameHelp);
        //final ImageButton imageButton_main_Settings = findViewById(R.id.imageButton_main_Settings);

        dealFirstTimeRunApp(MainActivity.this);
        //checkPermission();
        mediaPlayer = MediaPlayer.create(this,R.raw.soundeffect_selectmenuitem);



        //debug


        /*
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String identity = sharedPreferences.getString("identity","No id");
        Log.d(TAG, "dealFirstTimeRunApp: " + "identity: " + identity);
        */

        imageButton_main_StartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSoundEffect();
                Animation animation_alpha = new AlphaAnimation(0.1f, 1.0f);
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
                playSoundEffect();
                Animation animation_alpha = new AlphaAnimation(0.1f, 1.0f);
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
                playSoundEffect();
                Animation animation_alpha = new AlphaAnimation(0.1f, 1.0f);
                animation_alpha.setDuration(100);
                animation_alpha.setRepeatCount(4);
                animation_alpha.setRepeatMode(Animation.REVERSE);

                animation_alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {


                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        Intent intent = new Intent(MainActivity.this, GameHelpActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                imageButton_main_GameHelp.startAnimation(animation_alpha);


            }
        });

        /*
        imageButton_main_Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation_alpha = new AlphaAnimation(0.1f, 1.0f);
                animation_alpha.setDuration(100);
                animation_alpha.setRepeatCount(4);
                animation_alpha.setRepeatMode(Animation.REVERSE);

                animation_alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                imageButton_main_Settings.startAnimation(animation_alpha);
            }
        });
        */
    }

    public void playSoundEffect()
    {
        mediaPlayer.start();
    }


    //初始化游戏数据 完成后将该函数debug处putBoolean 改为false
    static public void dealFirstTimeRunApp(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean isFirstTimeRunApp = sharedPreferences.getBoolean("isFirstTimeRunApp", true);
        Log.d(TAG, "dealFirstTimeRunApp: " + "isFirstTimeRunApp: " + isFirstTimeRunApp);

        //第一次启动程序
        if (isFirstTimeRunApp) {

            editor.putBoolean("isFirstTimeRunApp",false);

            //生成sha-1
            Date date = new Date();
            Random random = new Random();
            int random_number = random.nextInt();

            String identity = SHA1.encode(String.valueOf(date) + random_number);

            editor.putString("identity",identity);
            editor.apply();

            //初始化设置
            editor.putInt(KEY_MUSIC_RADIO,4);
            editor.putBoolean(KEY_MUSIC_SWITCH,true);
            editor.putBoolean(KEY_SOUND_EFFECT,true);

            initProblems(context);

        }
    }
    /*
    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "checkPermission: 已经授权！");
        }
    }
    */

    public static void initProblems(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        int drawableId[] = {R.drawable.img_heart,R.drawable.img_mario,R.drawable.img_tortoise,R.drawable.img_mushroom};
        //题目尺寸
        int problemSizes[] = {9,12,10,12};
        //题目数据
        String problemData[] =
                {
                        "003000300*033303330*333333333*333333333*333333333*033333330*003333300*000333000*000030000*",
                        "000333330000*003333333330*001116656000*016166656660*016116661666*011666611110*000666666600*003343330000*033343343330*333344443333*663634436366*666444444666*",
                        "0000000000*0000000000*0022200222*0244420262*2422242222*2222222220*0222220000*0230230000*0000000000*0000000000*",
                        "000336633000*003366663300*033666666330*033663366330*366633336663*336333333633*336333333633*366663366663*666151151666*011151151110*001111111100*000111111000*"
                };



        //处理题目名字放到SP中
        StringBuilder stringBuilder = new StringBuilder();

        String existedProblemNames = sharedPreferences.getString("problemNames","");
        String existedNames[] = null;
        if(!existedProblemNames.equals(""))
        {
            existedNames = existedProblemNames.split("\\*");
        }
        else
        {
            existedNames = new String[0];
        }



        for (String problemName : problemNames) {
            stringBuilder.append(problemName).append("*");
        }
        for(int i = 0; i < existedNames.length; ++i)
        {
            boolean defaultName = false;
            for(int j = 0; j < MainActivity.problemNames.length; ++j)
            {
                if(existedNames[i].equals(MainActivity.problemNames[j]))
                {
                    defaultName = true;
                    break;
                }
            }
            if(!defaultName)
            {
                stringBuilder.append(existedNames[i]).append("*");
            }
        }
        editor.putString("problemNames",stringBuilder.toString());
        editor.apply();


        //SP记录具体每个题目的数据

        for (int i = 0; i < problemNames.length; ++i)
        {
            String name = problemNames[i];
            SharedPreferences sharedPreferences_problem = context.getSharedPreferences("problem_" + name, MODE_PRIVATE);
            SharedPreferences.Editor editor_problem = sharedPreferences_problem.edit();
            editor_problem.putInt("size", problemSizes[i]);
            editor_problem.putString("data", problemData[i]);
            editor_problem.apply();

            //处理题目初始图片

            try {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(context.openFileOutput(Paths.getImageFileName(problemNames[i]),MODE_PRIVATE));
                BufferedInputStream bufferedInputStream = new BufferedInputStream(context.getResources().openRawResource(drawableId[i]));
                byte[] bytes = new byte[1024];
                Log.d(TAG, "dealFirstTimeRunApp: " + "bufferedInputStream.available(): " + bufferedInputStream.available());
                int len = 0;
                while((len = bufferedInputStream.read(bytes)) > 0)
                {
                    bufferedOutputStream.write(bytes,0,len);
                }
                bufferedInputStream.close();
                bufferedOutputStream.flush();
                bufferedOutputStream.close();

                    /*
                    FileInputStream fileInputStream = openFileInput(Paths.getImageFileName(problemNames[i]));
                    Log.d(TAG, "dealFirstTimeRunApp: " + fileInputStream.available());
                    */

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        }
    }
}
