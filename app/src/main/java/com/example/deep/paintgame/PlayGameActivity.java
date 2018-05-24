package com.example.deep.paintgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deep.paintgame.animation.Animation;

import java.util.HashMap;


public class PlayGameActivity extends AppCompatActivity {
    //
    private static final String TAG = "PlayGameActivity";





    //方块状态
    public static final int PANE_NOT_EXISTED = 0;
    public static final int PANE_DEFAULT = 1;
    public static final int PANE_MARKED = 2;
    public static final int PANE_ERROR = 3;
    public static final int PANE_BLUE = 4;
    public static final int PANE_BLACK = 5;
    public static final int PANE_YELLOW = 6;

    public static final int IS_FINISHED_TRUE = 1;
    public static final int IS_FINISHED_FALSE = 0;


    //
    MediaPlayer mediaPlayer;
    int[] musicId;
    SoundPool soundPool;

    private int problem_size; // 当前题目的尺寸大小
    private String problem_name; // 当前题目的名字
    private boolean isFinish = false; // 玩家是否完成当前题目

    private int totalCorrectCount = 0;//总的需要涂色的方块
    private int remainCount = 0;//剩下需要涂色的方块
    private int correctCount = 0;//正确数
    private int totalPaneCount = 0;//总方格数

    private int border_width = 1;//stroke宽度private int border_width = 1;//stroke宽度

    private Button[][] buttons; // 按钮对象
    private int[][] problem_rightAnswer; // 当前题目的正确答案
    private int[][] problem_currentAnswer; // 当前玩家的答案
    private int currentColor = PANE_DEFAULT; // 当前的颜色，注意：0为不存在的方块，1为白色即默认方块颜色，2为绿色即标记颜色，3为红色即错误颜色
    private int errorCount = 0; // 当前的错误数
    private int time_minute = 0; // 游戏时间的分钟数
    private int time_second = 0; // 游戏时间的秒数
    private ImageButton imageButton_playGame_knock; // 敲打按钮对象
    private ImageButton imageButton_playGame_mark;  // 绘图按钮对象
    private TextView textView_errorCountNumber; // 错误数字文字对象
    private TextView textView_remainCountNumber;
    private TextView textView_time; // 时间文字对象
    private Thread timeThread; // 计时器线程对象
    private Thread buttonThread; // 按钮事件线程对象





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playgame);


        //初始化组件
        imageButton_playGame_knock = findViewById(R.id.imageButton_playGame_knock);
        imageButton_playGame_mark = findViewById(R.id.imageButton_playGame_mark);
        textView_errorCountNumber = findViewById(R.id.textView_playGame_ErrorCountNumber);
        textView_remainCountNumber = findViewById(R.id.textView_playGame_RemainCountNumber);
        textView_time = findViewById(R.id.textView_playGame_TimeNumber);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PlayGameActivity.this);
        //music
        boolean musicSwitch = sharedPreferences.getBoolean(MainActivity.KEY_MUSIC_SWITCH,true);
        int musicRadio = sharedPreferences.getInt(MainActivity.KEY_MUSIC_RADIO, 1);
        if(musicSwitch)
        {
            mediaPlayer = MediaPlayer.create(PlayGameActivity.this,SettingsActivity.music_raw[musicRadio]);
            mediaPlayer.start();
        }
        soundPool = new SoundPool(4, 0, 5);
        musicId = new int[4];
        musicId[0] = soundPool.load(this, R.raw.brush, 1);
        musicId[1] = soundPool.load(this, R.raw.broken, 1);
        musicId[2] = soundPool.load(this, R.raw.wrong, 1);
        musicId[3] = soundPool.load(this, R.raw.hint, 1);



        //获取Intent传递信息
        Intent intent = getIntent();
        problem_name = intent.getStringExtra("name");
        problem_size = intent.getIntExtra("size", 0);

        //开始游戏
        createGame();

        //按钮事件
        imageButton_playGame_knock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentColor == PANE_NOT_EXISTED)
                {
                    currentColor = PANE_DEFAULT;
                    imageButton_playGame_knock.setBackgroundColor(Color.LTGRAY);
                    imageButton_playGame_mark.setBackgroundColor(Color.LTGRAY);
                }
                else
                {
                    currentColor = PANE_NOT_EXISTED;
                    imageButton_playGame_knock.setBackgroundColor(Color.RED);
                    imageButton_playGame_mark.setBackgroundColor(Color.LTGRAY);
                }
            }
        });

        imageButton_playGame_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentColor == PANE_MARKED)
                {
                    currentColor = PANE_DEFAULT;
                    imageButton_playGame_knock.setBackgroundColor(Color.LTGRAY);
                    imageButton_playGame_mark.setBackgroundColor(Color.LTGRAY);
                }
                else
                {
                    currentColor = PANE_MARKED;
                    imageButton_playGame_knock.setBackgroundColor(Color.LTGRAY);
                    imageButton_playGame_mark.setBackgroundColor(Color.GREEN);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {


        if(mediaPlayer != null)
        {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }

        buttons = null;
        problem_rightAnswer = null;
        problem_currentAnswer = null;
        timeThread.interrupt();
        buttonThread.interrupt();
        super.onDestroy();
    }

    /**
     * 渲染单个答案按钮的对应颜色
     */
    public void drawSingleButtonByAnswer(int row, int col) {
        // 若玩家已经完成题目，则渲染正确答案，否则渲染玩家的当前答案
        int targetAnswer = isFinish ? problem_rightAnswer[row][col] : problem_currentAnswer[row][col];

        // 设置颜色
        int drawable = 0;
        switch (targetAnswer) {
            case PANE_NOT_EXISTED:
                break;
            case PANE_DEFAULT:
                drawable = R.drawable.gray;
                break;
            case PANE_MARKED:
                drawable = R.drawable.green;
                break;
            case PANE_ERROR:
                drawable = isFinish ? R.drawable.red : R.drawable.wrong;
                break;
            case PANE_BLUE:
                drawable = R.drawable.green;
                break;
            case PANE_BLACK:
                drawable = R.drawable.green;
                break;
            case PANE_YELLOW:
                drawable = R.drawable.yellow;
                break;
            default:
                break;
        }
        buttons[row][col].setBackgroundResource(drawable);
    }

    /**
     * 渲染全部答案按钮的对应颜色
     */
    public void drawButtonsByAnswer() {
        for (int row = 0; row < problem_size; ++row) {
            for (int col = 0; col < problem_size; ++col) {
                // 若玩家已经完成题目，则渲染正确答案，否则渲染玩家的当前答案
                int targetAnswer = isFinish ? problem_rightAnswer[row][col] : problem_currentAnswer[row][col];

                // 设置颜色
                int drawable = 0;
                switch (targetAnswer) {
                    case PANE_NOT_EXISTED:
                        break;
                    case PANE_DEFAULT:
                        drawable = R.drawable.gray;
                        break;
                    case PANE_MARKED:
                        drawable = R.drawable.green;
                        break;
                    case PANE_ERROR:
                        drawable = isFinish ? R.drawable.red : R.drawable.wrong;
                        break;
                    case PANE_BLUE:
                        drawable = R.drawable.green;
                        break;
                    case PANE_BLACK:
                        drawable = R.drawable.green;
                        break;
                    case PANE_YELLOW:
                        drawable = R.drawable.yellow;
                        break;
                    default:
                        break;
                }
                buttons[row][col].setBackgroundResource(drawable);
            }
        }
    }

    /**
     * 判断玩家当前是否完成游戏
     */
    public void judgeGameIsFinish() {
        /*for (int row = 0; row < problem_size; ++row) {
            for (int col = 0; col < problem_size; ++col) {
                if (problem_rightAnswer[row][col] == 0 && problem_currentAnswer[row][col] != 0) {
                    return;
                }
            }
        }*/

        if(errorCount >= totalCorrectCount || correctCount >= totalPaneCount - totalCorrectCount) {
            isFinish = true;
            Toast.makeText(PlayGameActivity.this, "游戏结束啦！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 初始化游戏
     */
    public void createGame() {

        /*初始化游戏属性*/

        // 获取题目数据
        SharedPreferences sharedPreferences = getSharedPreferences("problem_" + problem_name, MODE_PRIVATE);
        problem_size = sharedPreferences.getInt("size", 0);
        String problem_data_string = sharedPreferences.getString("data", "");
        if (problem_size == 0 || problem_data_string.equals("")) {
            Toast.makeText(PlayGameActivity.this, "读取题目数据失败！", Toast.LENGTH_SHORT).show();
            finish();
        }

        totalPaneCount = problem_size * problem_size;




        // 处理题目数据
        String[] problem_data_array = problem_data_string.split("#");
        problem_rightAnswer = new int[problem_size][problem_size];
        problem_currentAnswer = new int[problem_size][problem_size];
        for (int i = 0; i < problem_size; ++i) {
            for (int j = 0; j < problem_size; ++j) {
                problem_rightAnswer[i][j] = Character.getNumericValue(problem_data_array[i].charAt(j));
                if(problem_rightAnswer[i][j] != PANE_NOT_EXISTED)
                {
                    ++totalCorrectCount;
                }
                problem_currentAnswer[i][j] = PANE_DEFAULT;
            }
        }
        remainCount = problem_size * problem_size - totalCorrectCount;


        for (int i = 0; i < problem_size; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < problem_size; ++j) {
                stringBuilder.append(problem_rightAnswer[i][j]);
            }
            Log.i("PlayGameActivity", "row" + i + " is " + stringBuilder.toString());
        }

        /*初始化布局对象*/
        RelativeLayout relativeLayout_playGame_button = findViewById(R.id.relativeLayout_playGame_button);

        /*获取Activity大小*/
        DisplayMetrics displayMetrics = new DisplayMetrics();
        PlayGameActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        /*动态改变布局对象属性*/
        int rightMargin = 50;
        ViewGroup.LayoutParams layoutParams = relativeLayout_playGame_button.getLayoutParams();
        layoutParams.width = width / 20 * 19;
        layoutParams.height = width / 20 * 19;
        relativeLayout_playGame_button.setLayoutParams(layoutParams);

        Log.i("PlayGameActivity", "Activity width is " + width);
        Log.i("PlayGameActivity", "Activity height is " + height);
        Log.i("PlayGameActivity", "RelativeLayout width is " + layoutParams.width);
        Log.i("PlayGameActivity", "RelativeLayout height is " + layoutParams.height);

        /*创建按钮对象*/
        int textView_size = 100;//layoutParams.width / (problem_size + 1);
        int button_size = (layoutParams.width - textView_size) / problem_size; // 每个按钮的大小
        buttons = new Button[problem_size][problem_size];

        for (int row = 0; row < problem_size; ++row) {
            for (int col = 0; col < problem_size; ++col) {
                // 设置按钮属性
                buttons[row][col] = new Button(PlayGameActivity.this);


                // 设置按钮样式
                RelativeLayout.LayoutParams buttonParam = new RelativeLayout.LayoutParams(button_size, button_size);
                buttonParam.leftMargin = textView_size + button_size * col; // 横坐标定位
                buttonParam.topMargin = textView_size + button_size * row; // 纵坐标定位
                relativeLayout_playGame_button.addView(buttons[row][col], buttonParam); // 将按钮放入layout组件
            }
        }

        /*创建提示数字信息*/
        int textSize;
        if (problem_size >= 11) {
            textSize = 12;
        } else if (10 >= problem_size && problem_size >= 6) {
            textSize = 14;
        } else {
            textSize = 14;
        }
        for (int i = 0; i < problem_size; ++i) {
            // 设置每一行的数字信息
            int rowPaneCount = 0; // 该行的方格数量
            int rowPartCount = 0; // 该行的方格部分数量
            boolean isInRowPart = false;
            for (int j = 0; j < problem_size; ++j) {
                if (problem_rightAnswer[i][j] != PANE_NOT_EXISTED) {
                    rowPaneCount++;
                    isInRowPart = true;
                }
                else if (isInRowPart) {
                    rowPartCount++;
                    isInRowPart = false;
                }
            }
            if (isInRowPart) {
                rowPartCount++;
            }

            TextView rowHint = new TextView(PlayGameActivity.this);
            String rowHintString = rowPaneCount + "/" + rowPartCount;
            rowHint.setText(rowHintString);
            rowHint.setTextSize(textSize);
            rowHint.setGravity(Gravity.CENTER);
            RelativeLayout.LayoutParams rowHintParams = new RelativeLayout.LayoutParams(textView_size, button_size);
            rowHintParams.rightMargin = 0;
            rowHintParams.topMargin = textView_size + button_size * i;
            relativeLayout_playGame_button.addView(rowHint, rowHintParams);

            // 设置每一列的数字信息
            int colPaneCount = 0; // 该列的方格数量
            int colPartCount = 0; // 该列的方格部分数量
            boolean isInColPart = false;
            for (int j = 0; j < problem_size; ++j) {
                if (problem_rightAnswer[j][i] != PANE_NOT_EXISTED) {
                    colPaneCount++;
                    isInColPart = true;
                }
                else if (isInColPart)
                {
                    colPartCount++;
                    isInColPart = false;
                }
            }
            if (isInColPart) {
                colPartCount++;
            }

            TextView colHint = new TextView(this);
            String colHintString =  colPaneCount + "/" + colPartCount;
            colHint.setText(colHintString);
            colHint.setTextSize(textSize);
            colHint.setGravity(Gravity.CENTER);
            RelativeLayout.LayoutParams colHintParams = new RelativeLayout.LayoutParams(button_size, textView_size);
            colHintParams.leftMargin = textView_size + button_size * i;
            colHintParams.bottomMargin = 0;
            relativeLayout_playGame_button.addView(colHint, colHintParams);
        }

        /*渲染按钮颜色*/
        drawButtonsByAnswer();


        /*监听按钮事件*/
        buttonThread = new Thread(new ButtonThread());
        buttonThread.start();

        /*设置文字互动*/
        String errorCountString = Integer.toString(errorCount);
        textView_errorCountNumber.setText(errorCountString);

        textView_remainCountNumber.setText("" + totalCorrectCount) ;

        timeThread = new Thread(new TimeThread());
        timeThread.start();
    }


    // 计时器线程
    public class TimeThread extends Thread {
        // thread
        @Override
        public void run() {
            while (true) {
                if (isInterrupted())
                {
                    return;
                }
                try {
                    Thread.sleep(1000);     // sleep 1000ms
                    if (isFinish)
                    {
                        break;
                    }
                    else
                    {
                        ++time_second;
                        if (time_second == 60)
                        {
                            time_second = 0;
                            ++time_minute;
                        }
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run() {
                                String timeString = (time_minute>=10?time_minute:"0"+time_minute) + ":" + (time_second>=10?time_second:"0"+time_second);
                                textView_time.setText(timeString);
                            }
                        });
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    // 按钮事件线程
    public class ButtonThread extends Thread {
        @Override
        public void run() {
            for (int row = 0; row < problem_size; ++row) {
                for (int col = 0; col < problem_size; ++col) {
                    if (isInterrupted()) {
                        return;
                    }

                    final int rowNumber = row;
                    final int colNumber = col;

                    buttons[row][col].setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isInterrupted()) {
                                return;
                            }
                            if (isFinish) {
                                return;
                            }
                            switch (currentColor) {
                                case PANE_NOT_EXISTED:
                                    // 敲打状态
                                    // 若为非默认色，则敲打失败
                                    if (problem_currentAnswer[rowNumber][colNumber] != PANE_DEFAULT) {
                                        if (problem_currentAnswer[rowNumber][colNumber] == PANE_MARKED ||
                                                problem_currentAnswer[rowNumber][colNumber] == PANE_ERROR) {
                                            soundPool.play(musicId[3],1,1, 0, 0, 1);
                                        }
                                        break;
                                    }
                                    if (problem_rightAnswer[rowNumber][colNumber] == PANE_NOT_EXISTED)
                                    {
                                        problem_currentAnswer[rowNumber][colNumber] = PANE_NOT_EXISTED;
                                        ++correctCount;
                                        --remainCount;
                                        textView_remainCountNumber.setText("" + remainCount) ;
                                        soundPool.play(musicId[1],1,1, 0, 0, 1);
                                    }
                                    else
                                    {
                                        problem_currentAnswer[rowNumber][colNumber] = PANE_ERROR;

                                        //抖动动画
                                        AnimationSet animationSet = new AnimationSet(true);
                                        animationSet.addAnimation(Animation.animation_shake_1);
                                        animationSet.addAnimation(Animation.animation_shake_2);
                                        //buttons[rowNumber][colNumber].startAnimation();
                                        //buttons[rowNumber][colNumber].startAnimation();
                                        buttons[rowNumber][colNumber].startAnimation(animationSet);

                                        ++errorCount;
                                        String errorCountString = Integer.toString(errorCount);
                                        textView_errorCountNumber.setText(errorCountString);
                                        soundPool.play(musicId[2],1,1, 0, 0, 1);
                                    }
                                    break;
                                case PANE_DEFAULT:
                                    // 默认状态
                                    break;
                                case PANE_MARKED:
                                    // 标记状态
                                    if (problem_currentAnswer[rowNumber][colNumber] == PANE_DEFAULT)
                                    {
                                        problem_currentAnswer[rowNumber][colNumber] = PANE_MARKED;
                                        soundPool.play(musicId[0],1,1, 0, 0, 1);
                                    }
                                    else if (problem_currentAnswer[rowNumber][colNumber] == PANE_MARKED)
                                    {
                                        problem_currentAnswer[rowNumber][colNumber] = PANE_DEFAULT;
                                        soundPool.play(musicId[0],1,1, 0, 0, 1);
                                    }
                                    break;
                                default:
                                    break;
                            }
                            judgeGameIsFinish();
                            // 若题目已经完成，渲染所有按钮，否则只渲染被点击的按钮，减少性能损耗
                            if (isFinish)
                            {
                                drawButtonsByAnswer();
                            }
                            else
                            {
                                drawSingleButtonByAnswer(rowNumber, colNumber);
                            }
                        }
                    });
                }
            }

        }
    }
}