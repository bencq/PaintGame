package com.example.deep.paintgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button_main_StartGame = (Button) findViewById(R.id.button_main_StartGame);
        Button button_main_ManageProblem = (Button) findViewById(R.id.button_main_ManageProblem);
        Button button_main_GameHelp = (Button) findViewById(R.id.button_main_GameHelp);

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
    }

    public void dealFirstTimeRunApp()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean isFirstTimeRunApp = sharedPreferences.getBoolean("isFirstTimeRunApp",true);
        Log.d(TAG, "dealFirstTimeRunApp: " + "isFirstTimeRunApp: " + isFirstTimeRunApp);
        if(isFirstTimeRunApp)
        {
            String problemNames[] = {"test","origin_1","origin_2"};
            int problemSizes[] = {8,6,6};
            String problemData[] =
                    {
                            "04040400#04040400#44444444#04040400#04040400#04444440#04444440#04444440#",
                            "444444#444444#444444#444444#444444#000000#",
                            "040404#040404#040404#040404#040404#040404#"
                    };

            SharedPreferences.Editor editor = sharedPreferences.edit();
            StringBuilder stringBuilder = new StringBuilder();

            for (String problemName : problemNames) {
                stringBuilder.append(problemName).append("#");
            }
            editor.putString("problemNames",stringBuilder.toString());
            editor.putBoolean("isFirstTimeRunApp",true);//debug
            editor.apply();

            for (int i = 0; i < problemNames.length; ++i)
            {
                String name = problemNames[i];
                SharedPreferences sharedPreferences_problem = getSharedPreferences("problem_" + name, MODE_PRIVATE);
                SharedPreferences.Editor editor_problem = sharedPreferences_problem.edit();
                editor_problem.putInt("size", problemSizes[i]);
                editor_problem.putString("data", problemData[i]);
                editor_problem.apply();
            }

        }
    }
}
