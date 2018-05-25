package com.example.deep.paintgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link setBGMFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link setBGMFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class setBGMFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    static boolean soundEffect=false;
    RadioGroup radioGroup_SA_music1;
    RadioGroup radioGroup_SA_music2;
    int music_Number=6;
    static int music_no=0;
    RadioButton[]radioButton_SA_music_List=new RadioButton[6];
    Switch switch_SA_music;
    Switch switch_sound_effect;
    public static final int[] music_raw = {-1 ,R.raw.bgm_soccer,R.raw.bgm_karma,R.raw.bgm_zelda,R.raw.bgm_zeldaa,R.raw.bgm_quweigongfang,R.raw.bgm_heilmittel};

    static int musicRadio=1;

    private PlayGameActivity activity;
    private OnFragmentInteractionListener mListener;

    public setBGMFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment setBGMFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static setBGMFragment newInstance(String param1, String param2) {
        setBGMFragment fragment = new setBGMFragment();
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
        switch_SA_music =(Switch) view.findViewById(R.id.switch_SA_music_f);
        switch_sound_effect=(Switch) view.findViewById(R.id.switch_sound_effect_f);


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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean musicSwitch = sharedPreferences.getBoolean(MainActivity.KEY_MUSIC_SWITCH,false);
        musicRadio = sharedPreferences.getInt(MainActivity.KEY_MUSIC_RADIO, 1);

        SettingsActivity.soundEffect=sharedPreferences.getBoolean(MainActivity.KEY_SOUND_EFFECT, false);
        music_no=musicRadio;
        switch_SA_music.setChecked(musicSwitch);
        switch_sound_effect.setChecked(SettingsActivity.soundEffect);
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
            if(activity.mediaPlayer!=null)
            {
                activity.mediaPlayer.stop();
            }
            activity.mediaPlayer=MediaPlayer.create(activity, music_raw[musicRadio]);
            activity.mediaPlayer.start();

        }

        radioButton_SA_music_List[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioGroup_SA_music2.clearCheck();
                music_no=0;
                if(activity.mediaPlayer!=null)
                {
                    activity.mediaPlayer.stop();
                }
                activity.mediaPlayer=MediaPlayer.create(activity, music_raw[1]);
                activity.mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,1);
                editor.apply();
            }
        });
        radioButton_SA_music_List[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup_SA_music2.clearCheck();
                music_no=1;
                if(activity.mediaPlayer!=null)
                {
                    activity.mediaPlayer.stop();
                }
                activity.mediaPlayer=MediaPlayer.create(activity, music_raw[2]);
                activity.mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,2);
                editor.apply();
            }
        });
        radioButton_SA_music_List[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup_SA_music2.clearCheck();
                music_no=2;
                if(activity.mediaPlayer!=null)
                {
                    activity.mediaPlayer.stop();
                }
                activity.mediaPlayer=MediaPlayer.create(activity, music_raw[3]);
                activity.mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,3);
                editor.apply();
            }
        });
        radioButton_SA_music_List[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup_SA_music1.clearCheck();
                music_no=3;
                if(activity.mediaPlayer!=null)
                {
                    activity.mediaPlayer.stop();
                }
                activity.mediaPlayer=MediaPlayer.create(activity, music_raw[4]);
                activity.mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,4);
                editor.apply();
            }
        });
        radioButton_SA_music_List[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup_SA_music1.clearCheck();
                music_no=4;
                if(activity.mediaPlayer!=null)
                {
                    activity.mediaPlayer.stop();
                }
                activity.mediaPlayer=MediaPlayer.create(activity, music_raw[5]);
                activity.mediaPlayer.start();
                editor.putInt(MainActivity.KEY_MUSIC_RADIO,5);
                editor.apply();
            }
        });
        radioButton_SA_music_List[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup_SA_music1.clearCheck();
                music_no=5;
                if(activity.mediaPlayer!=null)
                {
                    activity.mediaPlayer.stop();
                }
                activity.mediaPlayer=MediaPlayer.create(activity, music_raw[6]);
                activity.mediaPlayer.start();
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
                    activity.mediaPlayer = MediaPlayer.create(activity, music_raw[music_no]);
                    activity.mediaPlayer.start();
                    editor.putBoolean(MainActivity.KEY_MUSIC_SWITCH,true);
                    editor.apply();
                }
                else
                {

                    for(int i=0;i<music_Number;i++)
                    {
                        radioButton_SA_music_List[i].setEnabled(false);
                    }
                    if(activity.mediaPlayer != null)
                    {
                        if(activity.mediaPlayer.isPlaying())
                        {
                            activity.mediaPlayer.stop();
                        }
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
                    SettingsActivity.soundEffect=true;
                    editor.putBoolean(MainActivity.KEY_SOUND_EFFECT,true);
                    editor.apply();
                }
                else
                {
                    SettingsActivity.soundEffect=false;
                    editor.putBoolean(MainActivity.KEY_SOUND_EFFECT,false);
                    editor.apply();
                }
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
