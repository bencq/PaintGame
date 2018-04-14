package com.example.deep.paintgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;

public class PlayGameActivity extends AppCompatActivity {
    //方块状态
    public static final int PANE_NOT_EXISTED = 0;
    public static final int PANE_DEFAULT = 1;
    public static final int PANE_MARKED = 2;
    public static final int PANE_ERROR = 3;
    public static final int PANE_RIGHT = 4;

    public static final int IS_FINISHED_TRUE = 1;
    public static final int IS_FINISHED_FALSE = 0;

    private int problem_size; // 当前题目的尺寸大小
    private String problem_name; // 当前题目的名字
    private boolean isFinish = false; // 玩家是否完成当前题目
    private Button[][] buttons; // 按钮对象
    private int[][] problem_rightAnswer; // 当前题目的正确答案
    private int[][] problem_currentAnswer; // 当前玩家的答案
    private int currentColor = PANE_DEFAULT; // 当前的颜色，注意：0为不存在的方块，1为白色即默认方块颜色，2为绿色即标记颜色，3为红色即错误颜色
    private int errorCount = 0; // 当前的错误数
    private int time_minute = 0; // 游戏时间的分钟数
    private int time_second = 0; // 游戏时间的秒数
    private Button button_playGame_knock; // 敲打按钮对象
    private Button button_playGame_mark;  // 绘图按钮对象
    private TextView textView_errorCount; // 错误数字文字对象
    private TextView textView_time; // 时间文字对象
    private Thread timeThread; // 计时器线程对象
    private Thread buttonThread; // 按钮事件线程对象
    private Timer timer; //计时器



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playgame);


        //初始化组件
        button_playGame_knock = (Button) findViewById(R.id.button_playGame_knock);
        button_playGame_mark = (Button) findViewById(R.id.button_playGame_mark);
        textView_errorCount = (TextView) findViewById(R.id.textView_playGame_ErrorCountNumber);
        textView_time = (TextView) findViewById(R.id.textView_playGame_TimeNumber);

        //获取Intent传递信息
        Intent intent = getIntent();
        problem_name = intent.getStringExtra("name");
        problem_size = intent.getIntExtra("size", 0);

        //开始游戏
        createGame();

        //按钮事件
        button_playGame_knock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentColor == PANE_DEFAULT)
                {
                    currentColor = PANE_NOT_EXISTED;
                    button_playGame_knock.setBackgroundColor(Color.RED);
                    button_playGame_mark.setBackgroundColor(Color.LTGRAY);
                }
                else
                {
                    currentColor = PANE_DEFAULT;
                    button_playGame_knock.setBackgroundColor(Color.LTGRAY);
                    button_playGame_mark.setBackgroundColor(Color.LTGRAY);
                }
            }
        });

        button_playGame_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentColor == PANE_DEFAULT)
                {
                    currentColor = PANE_MARKED;
                    button_playGame_knock.setBackgroundColor(Color.LTGRAY);
                    button_playGame_mark.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    currentColor = PANE_DEFAULT;
                    button_playGame_knock.setBackgroundColor(Color.LTGRAY);
                    button_playGame_mark.setBackgroundColor(Color.LTGRAY);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
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
        int border_width = 1;
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(border_width, Color.BLACK);

        // 若玩家已经完成题目，则渲染正确答案，否则渲染玩家的当前答案
        int targetAnswer;
        if (isFinish)
        {
            targetAnswer = problem_rightAnswer[row][col];
        }
        else
        {
            targetAnswer = problem_currentAnswer[row][col];
        }
        switch (targetAnswer) {
            case PANE_NOT_EXISTED:
                drawable.setColor(Color.WHITE);
                break;
            case PANE_DEFAULT:
                drawable.setColor(Color.LTGRAY);
                break;
            case PANE_MARKED:
                drawable.setColor(Color.GREEN);
                break;
            case PANE_ERROR:
                drawable.setColor(Color.RED);
                break;
            default:
                break;
        }
        buttons[row][col].setBackgroundDrawable(drawable);
    }

    /**
     * 渲染全部答案按钮的对应颜色
     */
    public void drawButtonsByAnswer() {
        int border_width = 1;
        for (int row = 0; row < problem_size; ++row) {
            for (int col = 0; col < problem_size; ++col) {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(border_width, Color.BLACK);

                // 若玩家已经完成题目，则渲染正确答案，否则渲染玩家的当前答案
                int targetAnswer;
                if (isFinish) {
                    targetAnswer = problem_rightAnswer[row][col];
                }
                else
                {
                    targetAnswer = problem_currentAnswer[row][col];
                }
                switch (targetAnswer) {
                    case PANE_NOT_EXISTED:
                        drawable.setColor(Color.WHITE);
                        break;
                    case PANE_DEFAULT:
                        drawable.setColor(Color.LTGRAY);
                        break;
                    case PANE_MARKED:
                        drawable.setColor(Color.GREEN);
                        break;
                    case PANE_ERROR:
                        drawable.setColor(Color.RED);
                        break;
                    case PANE_RIGHT:
                        drawable.setColor(Color.BLUE);
                        break;
                    case 5:
                        drawable.setColor(Color.BLACK);
                        break;
                    case 6:
                        drawable.setColor(Color.YELLOW);
                        break;
                    default:
                        break;
                }
                buttons[row][col].setBackgroundDrawable(drawable);
            }
        }
    }

    /**
     * 判断玩家当前是否完成游戏
     */
    public void judgeGameIsFinish() {
        for (int row = 0; row < problem_size; ++row) {
            for (int col = 0; col < problem_size; ++col) {
                if (problem_rightAnswer[row][col] == 0 && problem_currentAnswer[row][col] != 0) {
                    return;
                }
            }
        }
        isFinish = true;
        Toast.makeText(PlayGameActivity.this, "游戏结束啦啦啦啦啦！", Toast.LENGTH_LONG).show();
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

        // 处理题目数据
        String[] problem_data_array = problem_data_string.split("#");
        problem_rightAnswer = new int[problem_size][problem_size];
        problem_currentAnswer = new int[problem_size][problem_size];
        for (int i = 0; i < problem_size; ++i) {
            for (int j = 0; j < problem_size; ++j) {
                problem_rightAnswer[i][j] = Character.getNumericValue(problem_data_array[i].charAt(j));
                problem_currentAnswer[i][j] = PANE_DEFAULT;
            }
        }

        for (int i = 0; i < problem_size; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < problem_size; ++j) {
                stringBuilder.append(problem_rightAnswer[i][j]);
            }
            Log.i("PlayGameActivity", "row" + i + " is " + stringBuilder.toString());
        }

        /*初始化布局对象*/
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout_playGame_button);

        /*获取Activity大小*/
        DisplayMetrics displayMetrics = new DisplayMetrics();
        PlayGameActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        /*获取布局大小*/
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) layout.getLayoutParams();
        //layoutParams.width =  width * 4 / 5;
        //layoutParams.height = height * 4 / 5;
        //layout.setLayoutParams(layoutParams);
        Log.i("PlayGameActivity", "Activity width is " + width);
        Log.i("PlayGameActivity", "Activity height is " + height);
        Log.i("PlayGameActivity", "RelativeLayout width is " + layoutParams.width);
        Log.i("PlayGameActivity", "RelativeLayout height is " + layoutParams.height);

        /*创建按钮对象*/
        int textView_size = 100;
        int button_size = (width - textView_size) / problem_size; // 每个按钮的大小
        buttons = new Button[problem_size][problem_size];

        for (int row = 0; row < problem_size; ++row) {
            for (int col = 0; col < problem_size; ++col) {
                // 设置按钮属性
                buttons[row][col] = new Button(PlayGameActivity.this);

                // 设置按钮样式
                RelativeLayout.LayoutParams buttonParam = new RelativeLayout.LayoutParams(button_size, button_size);
                buttonParam.leftMargin = textView_size + button_size * col; // 横坐标定位
                buttonParam.topMargin = textView_size + button_size * row; // 纵坐标定位
                layout.addView(buttons[row][col], buttonParam); // 将按钮放入layout组件
            }
        }

        /*创建提示数字信息*/
        int textSize = 14;
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
            RelativeLayout.LayoutParams rowHintParams = new RelativeLayout.LayoutParams(button_size, button_size);
            rowHintParams.leftMargin = 0;
            rowHintParams.topMargin = textView_size + button_size * i;
            layout.addView(rowHint, rowHintParams);

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
            RelativeLayout.LayoutParams colHintParams = new RelativeLayout.LayoutParams(button_size, button_size);
            colHintParams.leftMargin = textView_size + button_size * i;
            colHintParams.topMargin = 0;
            layout.addView(colHint, colHintParams);
        }

        /*渲染按钮颜色*/
        drawButtonsByAnswer();


        /*监听按钮事件*/
        buttonThread = new Thread(new ButtonThread());
        buttonThread.start();

        /*设置文字互动*/
        String errorCountString = Integer.toString(errorCount);
        textView_errorCount.setText(errorCountString);
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
                                        break;
                                    }
                                    if (problem_rightAnswer[rowNumber][colNumber] == PANE_NOT_EXISTED)
                                    {
                                        problem_currentAnswer[rowNumber][colNumber] = PANE_NOT_EXISTED;
                                    }
                                    else
                                    {
                                        problem_currentAnswer[rowNumber][colNumber] = PANE_ERROR;
                                        ++errorCount;
                                        String errorCountString = Integer.toString(errorCount);
                                        textView_errorCount.setText(errorCountString);
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
                                    }
                                    else if (problem_currentAnswer[rowNumber][colNumber] == PANE_MARKED)
                                    {
                                        problem_currentAnswer[rowNumber][colNumber] = PANE_DEFAULT;
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