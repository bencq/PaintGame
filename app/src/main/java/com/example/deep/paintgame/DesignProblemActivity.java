package com.example.deep.paintgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deep.paintgame.utils.Paths;
import com.example.deep.paintgame.utils.Utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class DesignProblemActivity extends AppCompatActivity {
    //方块状态
    public static final int PANE_NOT_EXISTED = 0;
    public static final int PANE_DEFAULT = 1;
    public static final int PANE_MARKED = 2;
    public static final int PANE_ERROR = 3;
    public static final int PANE_RIGHT = 4;

    public static final int COLOR_LTGRAY = 1;
    public static final int COLOR_GREEN = 2;
    public static final int COLOR_RED = 3;
    public static final int COLOR_BLUE = 4;
    public static final int COLOR_BLACK = 5;
    public static final int COLOR_YELLOW = 6;

    public static final int IS_FINISHED_TRUE = 1;
    public static final int IS_FINISHED_FALSE = 0;

    public static final int MODE_ADD = 0;
    public static final int MODE_EDIT = 1;

    private int mode;
    private int problem_size; // 当前题目的尺寸大小
    private String problem_name; // 当前题目的名字
    private int problem_position;
    private Button button_DP_confirm; //确认按钮
    private Button[][] buttons; // 按钮对象
    private int[][] problem_currentAnswer; // 当前玩家的答案
    private int currentColor; // 当前的颜色，注意：0为不存在的方块，1为白色即默认方块颜色，2为绿色即标记颜色，3为红色即错误颜色
    private Thread buttonThread; // 按钮事件线程对象


    RelativeLayout relativeLayout_DP_button;


    RadioButton radioButton_DPA_white;
    RadioButton radioButton_DPA_ltgray;
    RadioButton radioButton_DPA_green;
    RadioButton radioButton_DPA_red;
    RadioButton radioButton_DPA_blue;
    RadioButton radioButton_DPA_black;
    RadioButton radioButton_DPA_yellow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_problem);


        //初始化组件
        button_DP_confirm = findViewById(R.id.button_DP_confirm);

        radioButton_DPA_white = findViewById(R.id.radioButton_DPA_white);
        radioButton_DPA_ltgray = findViewById(R.id.radioButton_DPA_ltgray);
        radioButton_DPA_green = findViewById(R.id.radioButton_DPA_green);
        radioButton_DPA_red = findViewById(R.id.radioButton_DPA_red);
        radioButton_DPA_blue = findViewById(R.id.radioButton_DPA_blue);
        radioButton_DPA_black = findViewById(R.id.radioButton_DPA_black);
        radioButton_DPA_yellow = findViewById(R.id.radioButton_DPA_yellow);



        radioButton_DPA_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = PANE_NOT_EXISTED;
            }
        });
        radioButton_DPA_ltgray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = COLOR_LTGRAY;
            }
        });
        radioButton_DPA_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = COLOR_GREEN;
            }
        });
        radioButton_DPA_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = COLOR_RED;
            }
        });
        radioButton_DPA_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = COLOR_BLUE;
            }
        });
        radioButton_DPA_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = COLOR_BLACK;
            }
        });
        radioButton_DPA_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = COLOR_YELLOW;
            }
        });





        //获取Intent传递信息
        Intent intent = getIntent();
        problem_name = intent.getStringExtra("name");
        problem_size = intent.getIntExtra("size", 0);
        problem_position = intent.getIntExtra("position", -1);
        mode = intent.getIntExtra("mode",MODE_ADD);

        //开始游戏
        createGame();

        //按钮事件
        button_DP_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mode == MODE_ADD)
                {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DesignProblemActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String problemNames = sharedPreferences.getString("problemNames",null);
                    if(problemNames == null)
                    {
                        Toast.makeText(DesignProblemActivity.this,"发生未知错误!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        editor.putString("problemNames",problemNames + problem_name + "#");
                        editor.apply();


                        SharedPreferences sharedPreferences_problem = getSharedPreferences("problem_" + problem_name, MODE_PRIVATE);
                        SharedPreferences.Editor editor_problem = sharedPreferences_problem.edit();
                        editor_problem.putInt("size",problem_size);
                        String data = getDataString();
                        editor_problem.putString("data",data);
                        editor_problem.apply();
                    }
                }
                else if(mode == MODE_EDIT)
                {
                    SharedPreferences sharedPreferences_problem = getSharedPreferences("problem_" + problem_name, MODE_PRIVATE);
                    SharedPreferences.Editor editor_problem = sharedPreferences_problem.edit();
                    editor_problem.putInt("size",problem_size);
                    String data = getDataString();
                    editor_problem.putString("data",data);
                    editor_problem.apply();
                }
                else
                {
                    Toast.makeText(DesignProblemActivity.this,"发生未知错误!",Toast.LENGTH_SHORT).show();
                    finish();
                }


                //处理图片
                try
                {
                    FileOutputStream fileOutputStream = view.getContext().openFileOutput(Paths.getImageFileName(problem_name),MODE_PRIVATE);
                    Bitmap bitmap = Utils.loadBitmapFromView(relativeLayout_DP_button);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    view.destroyDrawingCache();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }


                Toast.makeText(DesignProblemActivity.this,"保存成功!",Toast.LENGTH_SHORT).show();
                //提醒刷新图片
                Intent intent_back = new Intent();
                intent_back.putExtra("position", problem_position);
                setResult(RESULT_OK,intent_back);
                finish();
            }
        });
    }

    private String getDataString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < problem_size; ++i)
        {
            for (int j = 0; j < problem_size; ++j)
            {
                stringBuilder.append(problem_currentAnswer[i][j]);
            }
            stringBuilder.append("#");
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onDestroy() {
        buttons = null;
        problem_currentAnswer = null;
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
        targetAnswer = problem_currentAnswer[row][col];
        switch (targetAnswer) {
            case PANE_NOT_EXISTED:
                drawable.setColor(Color.WHITE);
                break;
            case COLOR_LTGRAY:
                drawable.setColor(Color.LTGRAY);
                break;
            case COLOR_GREEN:
                drawable.setColor(Color.GREEN);
                break;
            case COLOR_RED:
                drawable.setColor(Color.RED);
                break;
            case COLOR_BLUE:
                drawable.setColor(Color.BLUE);
                break;
            case COLOR_BLACK:
                drawable.setColor(Color.BLACK);
                break;
            case COLOR_YELLOW:
                drawable.setColor(Color.YELLOW);
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
                targetAnswer = problem_currentAnswer[row][col];
                switch (targetAnswer) {
                    case PANE_NOT_EXISTED:
                        drawable.setColor(Color.WHITE);
                        break;
                    case COLOR_LTGRAY:
                        drawable.setColor(Color.LTGRAY);
                        break;
                    case COLOR_GREEN:
                        drawable.setColor(Color.GREEN);
                        break;
                    case COLOR_RED:
                        drawable.setColor(Color.RED);
                        break;
                    case COLOR_BLUE:
                        drawable.setColor(Color.BLUE);
                        break;
                    case COLOR_BLACK:
                        drawable.setColor(Color.BLACK);
                        break;
                    case COLOR_YELLOW:
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
     * 初始化游戏
     */
    public void createGame() {

        /*初始化游戏属性*/
        String problem_data_string = null;

        if(mode == MODE_ADD)
        {
            StringBuilder stringBuilder_row = new StringBuilder();
            for (int i = 0; i < problem_size; ++i) {
                stringBuilder_row.append(PANE_NOT_EXISTED);
            }
            stringBuilder_row.append("#");
            String string_row = stringBuilder_row.toString();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < problem_size; ++i) {
                stringBuilder.append(string_row);
            }
            problem_data_string = stringBuilder.toString();
        }
        else if(mode == MODE_EDIT)
        {
            // 获取题目数据
            SharedPreferences sharedPreferences = getSharedPreferences("problem_" + problem_name, MODE_PRIVATE);
            problem_size = sharedPreferences.getInt("size", 0);
            problem_data_string = sharedPreferences.getString("data", null);
            if (problem_size == 0 || TextUtils.isEmpty(problem_data_string)) {
                Toast.makeText(DesignProblemActivity.this, "读取题目数据失败！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else
        {
            Toast.makeText(DesignProblemActivity.this,"发生未知错误!",Toast.LENGTH_SHORT).show();
            finish();
        }

        // 处理题目数据
        String[] problem_data_array = problem_data_string.split("#");
        problem_currentAnswer = new int[problem_size][problem_size];
        for (int i = 0; i < problem_size; ++i) {
            for (int j = 0; j < problem_size; ++j) {
                problem_currentAnswer[i][j] = Character.getNumericValue(problem_data_array[i].charAt(j));
            }
        }

        /*初始化布局对象*/
        relativeLayout_DP_button = findViewById(R.id.relativeLayout_DP_button);

        /*获取Activity大小*/
        DisplayMetrics displayMetrics = new DisplayMetrics();
        DesignProblemActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        /*动态改变布局对象属性*/
        ViewGroup.LayoutParams layoutParams = relativeLayout_DP_button.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        relativeLayout_DP_button.setLayoutParams(layoutParams);

        /*创建按钮对象*/
        int textView_size = 100;
        int button_size = (width - textView_size) / problem_size; // 每个按钮的大小
        buttons = new Button[problem_size][problem_size];

        for (int row = 0; row < problem_size; ++row) {
            for (int col = 0; col < problem_size; ++col) {
                // 设置按钮属性
                buttons[row][col] = new Button(DesignProblemActivity.this);

                // 设置按钮样式
                RelativeLayout.LayoutParams buttonParam = new RelativeLayout.LayoutParams(button_size, button_size);
                buttonParam.leftMargin = textView_size + button_size * col; // 横坐标定位
                buttonParam.topMargin = textView_size + button_size * row; // 纵坐标定位
                relativeLayout_DP_button.addView(buttons[row][col], buttonParam); // 将按钮放入layout组件
            }
        }


        //debug
        // 更新文字提示信息
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
                if (problem_currentAnswer[i][j] != PANE_NOT_EXISTED) {
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

            TextView rowHint = new TextView(DesignProblemActivity.this);
            String rowHintString = getHintString(rowPaneCount,rowPartCount);
            rowHint.setText(rowHintString);
            rowHint.setTextSize(textSize);
            rowHint.setGravity(Gravity.CENTER);
            RelativeLayout.LayoutParams rowHintParams = new RelativeLayout.LayoutParams(textView_size, button_size);
            rowHintParams.rightMargin = 0;
            rowHintParams.topMargin = textView_size + button_size * i;
            relativeLayout_DP_button.addView(rowHint, rowHintParams);

            // 设置每一列的数字信息
            int colPaneCount = 0; // 该列的方格数量
            int colPartCount = 0; // 该列的方格部分数量
            boolean isInColPart = false;
            for (int j = 0; j < problem_size; ++j) {
                if (problem_currentAnswer[j][i] != PANE_NOT_EXISTED) {
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
            String colHintString =  getHintString(colPaneCount,colPartCount);
            colHint.setText(colHintString);
            colHint.setTextSize(textSize);
            colHint.setGravity(Gravity.CENTER);
            RelativeLayout.LayoutParams colHintParams = new RelativeLayout.LayoutParams(button_size, textView_size);
            colHintParams.leftMargin = textView_size + button_size * i;
            colHintParams.bottomMargin = 0;
            relativeLayout_DP_button.addView(colHint, colHintParams);
        }



        /*渲染按钮颜色*/
        drawButtonsByAnswer();


        /*监听按钮事件*/
        buttonThread = new Thread(new ButtonThread());
        buttonThread.start();

        /*设置文字互动*/
    }

    private String getHintString(int paneCount, int partCount) {
        return paneCount + "/" + partCount;
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
                            if(problem_currentAnswer[rowNumber][colNumber] == currentColor)
                            {
                                problem_currentAnswer[rowNumber][colNumber] = PANE_NOT_EXISTED;
                            }
                            else
                            {
                                problem_currentAnswer[rowNumber][colNumber] = currentColor;
                            }

                            drawSingleButtonByAnswer(rowNumber, colNumber);
                        }
                    });
                }
            }

        }
    }
}