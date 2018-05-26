package com.example.deep.paintgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Eric Zhang on 2018/5/23.
 */

public class GameHelpActivity extends AppCompatActivity {

    private int picture_number = 1 ;

    ImageView imageview_help;

    Button button_front;
    Button button_next;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setTitle("游戏帮助");

        imageview_help = (ImageView) findViewById(R.id.imageView_help);
        button_front = (Button) findViewById(R.id.button_front);
        button_next = (Button) findViewById(R.id.button_next);

        button_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( picture_number != 1)
                    picture_number--;
                ChangePicture();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( picture_number != 9)
                    picture_number++;
                ChangePicture();
            }
        });
    }

    protected void ChangePicture(){
        switch (picture_number){
            case 1:
                imageview_help.setImageDrawable(getResources().getDrawable(R.drawable.help_home_start));
                break;
            case 2:
                imageview_help.setImageDrawable(getResources().getDrawable(R.drawable.help_begin));
                break;
            case 3:
                imageview_help.setImageDrawable(getResources().getDrawable(R.drawable.help_play));
                break;
            case 4:
                imageview_help.setImageDrawable(getResources().getDrawable(R.drawable.help_play_setting));
                break;
            case 5:
                imageview_help.setImageDrawable(getResources().getDrawable(R.drawable.help_explanation));
                break;
            case 6:
                imageview_help.setImageDrawable(getResources().getDrawable(R.drawable.help_end));
                break;
            case 7:
                imageview_help.setImageDrawable(getResources().getDrawable(R.drawable.help_home_edit));
                break;
            case 8:
                imageview_help.setImageDrawable(getResources().getDrawable(R.drawable.help_edit));
                break;
            case 9:
                imageview_help.setImageDrawable(getResources().getDrawable(R.drawable.help_create));
                break;
            default:
        }
    }
}
