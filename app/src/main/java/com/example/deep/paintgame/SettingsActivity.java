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

    RadioGroup radioGroup_SA_music1;
    RadioGroup radioGroup_SA_music2;
    int music_Number=6;
    static int music_no=0;
    RadioButton []radioButton_SA_music_List=new RadioButton[6];
    Switch switch_SA_music;

    public static final int[] music_raw = {-1 ,R.raw.soccer,R.raw.karma,R.raw.zelda,R.raw.zeldaa,R.raw.quweigongfang,R.raw.heilmittel};

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

        radioGroup_SA_music1 = findViewById(R.id.radioGroup_SA_music1);
        radioGroup_SA_music2 = findViewById(R.id.radioGroup_SA_music2);
        radioButton_SA_music_List[0] = findViewById(R.id.radioButton_SA_music1);
        radioButton_SA_music_List[1]= findViewById(R.id.radioButton_SA_music2);
        radioButton_SA_music_List[2] = findViewById(R.id.radioButton_SA_music3);
        radioButton_SA_music_List[3]= findViewById(R.id.radioButton_SA_music4);
        radioButton_SA_music_List[4] = findViewById(R.id.radioButton_SA_music5);
        radioButton_SA_music_List[5] = findViewById(R.id.radioButton_SA_music6);
        switch_SA_music = findViewById(R.id.switch_SA_music);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean musicSwitch = sharedPreferences.getBoolean(MainActivity.KEY_MUSIC_SWITCH,true);
        int musicRadio = sharedPreferences.getInt(MainActivity.KEY_MUSIC_RADIO, 1);

        switch_SA_music.setChecked(musicSwitch);
        if(!musicSwitch)
        {
            /*
            radioButton_SA_music1.setEnabled(false);
            radioButton_SA_music2.setEnabled(false);
            radioButton_SA_music3.setEnabled(false);
            radioButton_SA_music4.setEnabled(false);
            radioButton_SA_music5.setEnabled(false);
            radioButton_SA_music6.setEnabled(false);
            */
            for(int i=0;i<music_Number;i++)
            {
                radioButton_SA_music_List[i].setEnabled(false);
            }
        }

        radioButton_SA_music_List[musicRadio-1].setChecked(true);
        /*switch (musicRadio)
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
        */
/*
        for(int i =0;i<music_Number;i++)
        {
            music_no=musicRadio-1;
            radioButton_SA_music_List[i].setOnClickListener(new View.OnClickListener() {
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
                    editor.putInt(MainActivity.KEY_MUSIC_RADIO,music_no+1);
                    editor.apply();
                }
            });
        }
        */
        radioButton_SA_music_List[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null)
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                    }
                }
                radioGroup_SA_music2.clearCheck();
                music_no=0;
                mediaPlayer = MediaPlayer.create(SettingsActivity.this, music_raw[1]);
                mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,1);
                editor.apply();
            }
        });
        radioButton_SA_music_List[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null)
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                    }
                }
                radioGroup_SA_music2.clearCheck();
                music_no=1;
                mediaPlayer = MediaPlayer.create(SettingsActivity.this, music_raw[2]);
                mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,2);
                editor.apply();
            }
        });
        radioButton_SA_music_List[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null)
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                    }
                }
                radioGroup_SA_music2.clearCheck();
                music_no=2;
                mediaPlayer = MediaPlayer.create(SettingsActivity.this, music_raw[3]);
                mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,3);
                editor.apply();
            }
        });
        radioButton_SA_music_List[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null)
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                    }
                }
                radioGroup_SA_music1.clearCheck();
                music_no=3;
                mediaPlayer = MediaPlayer.create(SettingsActivity.this, music_raw[4]);
                mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,4);
                editor.apply();
            }
        });
        radioButton_SA_music_List[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null)
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                    }
                }
                radioGroup_SA_music1.clearCheck();
                music_no=4;
                mediaPlayer = MediaPlayer.create(SettingsActivity.this, music_raw[5]);
                mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,5);
                editor.apply();
            }
        });
        radioButton_SA_music_List[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer != null)
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.stop();
                    }
                }
                radioGroup_SA_music1.clearCheck();
                music_no=5;
                mediaPlayer = MediaPlayer.create(SettingsActivity.this, music_raw[6]);
                mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,6);
                editor.apply();
            }
        });
        /*
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
        */
        switch_SA_music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    for(int i=0;i<music_Number;i++)
                    {
                        radioButton_SA_music_List[i].setEnabled(true);
                    }
                    mediaPlayer = MediaPlayer.create(SettingsActivity.this, music_raw[music_no+1]);
                    mediaPlayer.start();
                    editor.putBoolean(MainActivity.KEY_MUSIC_SWITCH,true);
                    editor.apply();
                }
                else
                {

                    for(int i=0;i<music_Number;i++)
                    {
                        radioButton_SA_music_List[i].setEnabled(false);
                    }
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
