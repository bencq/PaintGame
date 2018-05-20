package com.example.deep.paintgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    public static final String KEY_MUSIC_RADIO = "key_musicRadio";
    public static final String KEY_MUSIC_SWITCH = "key_musicSwitch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button_main_StartGame = (Button) findViewById(R.id.button_main_StartGame);
        Button button_main_ManageProblem = (Button) findViewById(R.id.button_main_ManageProblem);
        Button button_main_GameHelp = (Button) findViewById(R.id.button_main_GameHelp);
        Button button_main_Settings = findViewById(R.id.button_main_Settings);

        dealFirstTimeRunApp();

        button_main_StartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StartGameActivity.class);
                startActivity(intent);
            }
        });

        button_main_ManageProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageProblemActivity.class);
                startActivity(intent);
            }
        });

        button_main_GameHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //debug
                // 启动游戏帮助Activity
            }
        });

        button_main_Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
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
                            "04040400#04040400#44444444#04040400#04040400#04444440#04444440#04444440#",
                            "4444444444#4444444444#4444444444#4444444444#4444444444#0000000000#0000000000#0000000000#0000000000#0000000000#",
                            "040404#040404#040404#040404#040404#040404#",
                            "040440400440#040440400440#040440400440#040440400440#040440400440#040440400440#040440400440#040440400440#040440400440#040440400440#040440400440#040440400440#"
                    };


            //处理题目名字放到SP中
            SharedPreferences.Editor editor = sharedPreferences.edit();
            StringBuilder stringBuilder = new StringBuilder();

            for (String problemName : problemNames) {
                stringBuilder.append(problemName).append("#");
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
