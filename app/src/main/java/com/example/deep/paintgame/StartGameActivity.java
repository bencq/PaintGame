package com.example.deep.paintgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StartGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startgame);
        Button button_startGame_PlayGame = (Button) findViewById(R.id.button_startGame_PlayGame);
        button_startGame_PlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 测试用的share文件，可删除此处代码
                String name = "test";
                SharedPreferences sharedPreferences = getSharedPreferences("problem_" + name, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("size", 8);
                editor.putString("data", "04040400#04040400#44444444#04040400#04040400#04444440#04444440#04444440");
                editor.apply();

                Intent intent = new Intent(StartGameActivity.this, PlayGameActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}
