package com.example.deep.paintgame;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button_main_StartGame = (Button) findViewById(R.id.button_main_StartGame);
        Button button_main_ManageProblem = (Button) findViewById(R.id.button_main_ManageProblem);
        Button button_main_GameHelp = (Button) findViewById(R.id.button_main_GameHelp);
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
}
