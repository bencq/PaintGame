package com.example.deep.paintgame;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    RadioGroup radioGroup_SA_music;
    RadioButton radioButton_SA_music1;
    RadioButton radioButton_SA_music2;
    RadioButton radioButton_SA_music3;
    Switch switch_SA_music;

    public static final int[] music_raw = {-1 ,R.raw.soccer,R.raw.soccer,R.raw.soccer};

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mediaPlayer != null)
        {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        radioGroup_SA_music = findViewById(R.id.radioGroup_SA_music);
        radioButton_SA_music1 = findViewById(R.id.radioButton_SA_music1);
        radioButton_SA_music2 = findViewById(R.id.radioButton_SA_music2);
        radioButton_SA_music3 = findViewById(R.id.radioButton_SA_music3);
        switch_SA_music = findViewById(R.id.switch_SA_music);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean musicSwitch = sharedPreferences.getBoolean(MainActivity.KEY_MUSIC_SWITCH,true);
        int musicRadio = sharedPreferences.getInt(MainActivity.KEY_MUSIC_RADIO, 1);

        switch_SA_music.setChecked(musicSwitch);
        if(!musicSwitch)
        {
            radioButton_SA_music1.setEnabled(false);
            radioButton_SA_music2.setEnabled(false);
            radioButton_SA_music3.setEnabled(false);
        }

        switch (musicRadio)
        {
            case 1:
                radioButton_SA_music1.setChecked(true);
                break;

            case 2:
                radioButton_SA_music2.setChecked(true);
                break;

            case 3:
                radioButton_SA_music3.setChecked(true);
                break;
        }


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


                mediaPlayer = MediaPlayer.create(SettingsActivity.this, music_raw[1]);
                mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,1);
                editor.apply();
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

                mediaPlayer = MediaPlayer.create(SettingsActivity.this, music_raw[2]);
                mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,2);
                editor.apply();
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
                mediaPlayer = MediaPlayer.create(SettingsActivity.this, music_raw[3]);
                mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,3);
                editor.apply();
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
                    editor.putBoolean(MainActivity.KEY_MUSIC_SWITCH,true);
                    editor.apply();
                }
                else
                {
                    radioButton_SA_music1.setEnabled(false);
                    radioButton_SA_music2.setEnabled(false);
                    radioButton_SA_music3.setEnabled(false);

                    if(mediaPlayer != null)
                    {
                        if(mediaPlayer.isPlaying())
                        {
                            mediaPlayer.stop();
                        }
                    }
                    editor.putBoolean(MainActivity.KEY_MUSIC_SWITCH,false);
                    editor.apply();
                }
            }
        });






    }

}
