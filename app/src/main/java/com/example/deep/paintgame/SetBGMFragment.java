package com.example.deep.paintgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.deep.paintgame.deprecated.SettingsActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetBGMFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetBGMFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetBGMFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    static boolean soundEffect = false;
    RadioGroup radioGroup_SA_music1;
    RadioGroup radioGroup_SA_music2;
    int music_Number = 6;
    static int music_number = 0;
    RadioButton[]radioButton_SA_music_List=new RadioButton[6];
    Switch switch_SA_music;
    Switch switch_sound_effect;

    SeekBar seekBar_BGM;
    SeekBar seekBar_soundEffect;


    public static final int[] music_raw = {-1 ,R.raw.bgm_soccer,R.raw.bgm_karma,R.raw.bgm_zelda,R.raw.bgm_zeldaa,R.raw.bgm_quweigongfang,R.raw.bgm_heilmittel};

    static int musicRadio = 1;

    private PlayGameActivity activity;
    private OnFragmentInteractionListener mListener;

    public SetBGMFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetBGMFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetBGMFragment newInstance(String param1, String param2) {
        SetBGMFragment fragment = new SetBGMFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_bgm, container, false);
        radioGroup_SA_music1 = view.findViewById(R.id.radioGroup_SA_music1_f);
        radioGroup_SA_music2 = view.findViewById(R.id.radioGroup_SA_music2_f);
        radioButton_SA_music_List[0] = view.findViewById(R.id.radioButton_SA_music1_f);
        radioButton_SA_music_List[1]= view.findViewById(R.id.radioButton_SA_music2_f);
        radioButton_SA_music_List[2] = view.findViewById(R.id.radioButton_SA_music3_f);
        radioButton_SA_music_List[3]= view.findViewById(R.id.radioButton_SA_music4_f);
        radioButton_SA_music_List[4] = view.findViewById(R.id.radioButton_SA_music5_f);
        radioButton_SA_music_List[5] = view.findViewById(R.id.radioButton_SA_music6_f);
        switch_SA_music = view.findViewById(R.id.switch_SA_music_f);
        switch_sound_effect = view.findViewById(R.id.switch_sound_effect_f);
        seekBar_BGM = view.findViewById(R.id.seekBar_BGM);
        seekBar_soundEffect = view.findViewById(R.id.seekBar_soundEffect);

        view.setClickable(true);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //获取Activity
        activity = (PlayGameActivity)getActivity();


        //
        seekBar_soundEffect.setProgress(100);
        seekBar_BGM.setProgress(100);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean musicSwitch = sharedPreferences.getBoolean(MainActivity.KEY_MUSIC_SWITCH,true);
        musicRadio = sharedPreferences.getInt(MainActivity.KEY_MUSIC_RADIO, 1);

        PlayGameActivity.soundEffect = sharedPreferences.getBoolean(MainActivity.KEY_SOUND_EFFECT, true);
        music_number = musicRadio;
        switch_SA_music.setChecked(musicSwitch);
        switch_sound_effect.setChecked(PlayGameActivity.soundEffect);
        if(!musicSwitch)
        {
            for(int i=0;i<music_Number;i++)
            {
                radioButton_SA_music_List[i].setEnabled(false);
            }
        }

        radioButton_SA_music_List[musicRadio-1].setChecked(true);
        if(musicSwitch)
        {
            if(PlayGameActivity.mediaPlayer != null)
            {
                if(PlayGameActivity.mediaPlayer.isPlaying())
                {
                    PlayGameActivity.mediaPlayer.stop();
                }
                PlayGameActivity.mediaPlayer.release();
            }
            PlayGameActivity.mediaPlayer = MediaPlayer.create(activity, music_raw[musicRadio]);
            PlayGameActivity.mediaPlayer.setLooping(true);
            PlayGameActivity.mediaPlayer.start();
        }

        radioButton_SA_music_List[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioGroup_SA_music2.clearCheck();
                music_number =0;
                if(PlayGameActivity.mediaPlayer != null)
                {
                    if(PlayGameActivity.mediaPlayer.isPlaying())
                    {
                        PlayGameActivity.mediaPlayer.stop();
                    }
                    PlayGameActivity.mediaPlayer.release();
                }
                PlayGameActivity.mediaPlayer  = MediaPlayer.create(activity, music_raw[1]);
                PlayGameActivity.mediaPlayer.setLooping(true);
                PlayGameActivity.mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,1);
                editor.apply();
            }
        });
        radioButton_SA_music_List[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup_SA_music2.clearCheck();
                music_number =1;
                if(PlayGameActivity.mediaPlayer != null)
                {
                    if(PlayGameActivity.mediaPlayer.isPlaying())
                    {
                        PlayGameActivity.mediaPlayer.stop();
                    }
                    PlayGameActivity.mediaPlayer.release();
                }
                PlayGameActivity.mediaPlayer = MediaPlayer.create(activity, music_raw[2]);
                PlayGameActivity.mediaPlayer.setLooping(true);
                PlayGameActivity.mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,2);
                editor.apply();
            }
        });
        radioButton_SA_music_List[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup_SA_music2.clearCheck();
                music_number =2;
                if(PlayGameActivity.mediaPlayer != null)
                {
                    if(PlayGameActivity.mediaPlayer.isPlaying())
                    {
                        PlayGameActivity.mediaPlayer.stop();
                    }
                    PlayGameActivity.mediaPlayer.release();
                }
                PlayGameActivity.mediaPlayer = MediaPlayer.create(activity, music_raw[3]);
                PlayGameActivity.mediaPlayer.setLooping(true);
                PlayGameActivity.mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,3);
                editor.apply();
            }
        });
        radioButton_SA_music_List[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup_SA_music1.clearCheck();
                music_number =3;
                if(PlayGameActivity.mediaPlayer != null)
                {
                    if(PlayGameActivity.mediaPlayer.isPlaying())
                    {
                        PlayGameActivity.mediaPlayer.stop();
                    }
                    PlayGameActivity.mediaPlayer.release();
                }
                PlayGameActivity.mediaPlayer = MediaPlayer.create(activity, music_raw[4]);
                PlayGameActivity.mediaPlayer.setLooping(true);
                PlayGameActivity.mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,4);
                editor.apply();
            }
        });
        radioButton_SA_music_List[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup_SA_music1.clearCheck();
                music_number =4;
                if(PlayGameActivity.mediaPlayer != null)
                {
                    if(PlayGameActivity.mediaPlayer.isPlaying())
                    {
                        PlayGameActivity.mediaPlayer.stop();
                    }
                    PlayGameActivity.mediaPlayer.release();
                }
                PlayGameActivity.mediaPlayer = MediaPlayer.create(activity, music_raw[5]);
                PlayGameActivity.mediaPlayer.setLooping(true);
                PlayGameActivity.mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,5);
                editor.apply();
            }
        });
        radioButton_SA_music_List[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup_SA_music1.clearCheck();
                music_number =5;
                if(PlayGameActivity.mediaPlayer != null)
                {
                    if(PlayGameActivity.mediaPlayer.isPlaying())
                    {
                        PlayGameActivity.mediaPlayer.stop();
                    }
                    PlayGameActivity.mediaPlayer.release();
                }
                PlayGameActivity.mediaPlayer = MediaPlayer.create(activity, music_raw[6]);
                PlayGameActivity.mediaPlayer.setLooping(true);
                PlayGameActivity.mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,6);
                editor.apply();
            }
        });

        switch_SA_music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    for(int i=0;i<music_Number;i++)
                    {
                        radioButton_SA_music_List[i].setEnabled(true);
                    }
                    if(PlayGameActivity.mediaPlayer != null)
                    {
                        if(PlayGameActivity.mediaPlayer.isPlaying())
                        {
                            PlayGameActivity.mediaPlayer.stop();
                        }
                        PlayGameActivity.mediaPlayer.release();
                    }
                    PlayGameActivity.mediaPlayer = MediaPlayer.create(activity, music_raw[music_number]);
                    PlayGameActivity.mediaPlayer.setLooping(true);
                    PlayGameActivity.mediaPlayer.start();
                    editor.putBoolean(MainActivity.KEY_MUSIC_SWITCH,true);
                    editor.apply();
                }
                else
                {

                    for(int i=0;i<music_Number;i++)
                    {
                        radioButton_SA_music_List[i].setEnabled(false);
                    }
                    if(PlayGameActivity.mediaPlayer != null)
                    {
                        if(PlayGameActivity.mediaPlayer.isPlaying())
                        {
                            PlayGameActivity.mediaPlayer.stop();
                        }
                        PlayGameActivity.mediaPlayer.release();
                    }
                    editor.putBoolean(MainActivity.KEY_MUSIC_SWITCH,false);
                    editor.apply();
                }
            }
        });
        switch_sound_effect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    PlayGameActivity.soundEffect = true;
                    editor.putBoolean(MainActivity.KEY_SOUND_EFFECT,true);
                    editor.apply();
                }
                else
                {
                    PlayGameActivity.soundEffect = false;
                    editor.putBoolean(MainActivity.KEY_SOUND_EFFECT,false);
                    editor.apply();
                }
            }
        });

        seekBar_BGM.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                activity.volume_BGM = (float)(i) / 100;
                if(PlayGameActivity.mediaPlayer != null)
                {
                    PlayGameActivity.mediaPlayer.setVolume(activity.volume_BGM,activity.volume_BGM);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_soundEffect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                activity.volume_soundEffect = (float)(i) / 100;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });





    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
