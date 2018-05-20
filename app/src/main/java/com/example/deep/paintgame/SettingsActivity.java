package com.example.deep.paintgame;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    RadioButton radioButton_SA_music1;
    RadioButton radioButton_SA_music2;
    RadioButton radioButton_SA_music3;
    Switch switch_SA_music;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        radioButton_SA_music1 = findViewById(R.id.radioButton_SA_music1);
        radioButton_SA_music1.setChecked(true);
        mediaPlayer = MediaPlayer.create(SettingsActivity.this, R.raw.soccer);
        radioButton_SA_music2 = findViewById(R.id.radioButton_SA_music2);
        radioButton_SA_music3 = findViewById(R.id.radioButton_SA_music3);
        switch_SA_music = findViewById(R.id.switch_SA_music);
        switch_SA_music.setChecked(true);

        radioButton_SA_music1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null)
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                    }
                }


                mediaPlayer = MediaPlayer.create(SettingsActivity.this, R.raw.soccer);
                mediaPlayer.start();
            }
        });
        radioButton_SA_music2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null)
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                    }
                }

                mediaPlayer = MediaPlayer.create(SettingsActivity.this, R.raw.soccer);
                mediaPlayer.start();
            }
        });
        radioButton_SA_music3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null)
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                    }
                }
                mediaPlayer = MediaPlayer.create(SettingsActivity.this, R.raw.soccer);
                mediaPlayer.start();
            }
        });

        switch_SA_music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    radioButton_SA_music1.setEnabled(true);
                    radioButton_SA_music2.setEnabled(true);
                    radioButton_SA_music3.setEnabled(true);
                }
                else
                {
                    radioButton_SA_music1.setEnabled(false);
                    radioButton_SA_music2.setEnabled(false);
                    radioButton_SA_music3.setEnabled(false);
                    mediaPlayer.stop();
                }
            }
        });






    }

}
