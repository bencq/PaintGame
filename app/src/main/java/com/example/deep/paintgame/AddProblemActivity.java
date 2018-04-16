package com.example.deep.paintgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProblemActivity extends AppCompatActivity {

    //debug 用fragment代替此activity

    public static final int MAX_SIZE = 12;
    public static final int MIN_SIZE = 6;
    private static final String TAG = "AddProblemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);

        //初始化组件
        Button button_MP_confirm = findViewById(R.id.button_MP_confirm);
        Button button_MP_downSize = findViewById(R.id.button_MP_downSize);
        Button button_MP_upSize = findViewById(R.id.button_MP_upSize);
        final EditText editText_MP_name = findViewById(R.id.editText_MP_name);
        final EditText editText_MP_size = findViewById(R.id.editText_MP_size);



        //设置onClick事件
        button_MP_upSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_size = editText_MP_size.getText().toString();
                int size = Integer.parseInt(str_size);
                if(size < MAX_SIZE)
                {
                    ++size;
                    str_size = Integer.toString(size);
                    editText_MP_size.setText(str_size);
                }
            }
        });

        button_MP_downSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_size = editText_MP_size.getText().toString();
                int size = Integer.parseInt(str_size);
                if(size > MIN_SIZE)
                {
                    --size;
                    str_size = Integer.toString(size);
                    editText_MP_size.setText(str_size);
                }
            }
        });

        button_MP_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_name = editText_MP_name.getText().toString();
                if(!TextUtils.isEmpty(str_name))
                {

                    //判断重名
                    SharedPreferences sharedPreferences_problem = getSharedPreferences("problem_" + str_name, MODE_PRIVATE);
                    String data = sharedPreferences_problem.getString("data",null);
                    if(data != null)
                    {
                        showDuplicatedNameAlertDialog(str_name);
                    }
                    else
                    {
                        Intent intent = new Intent(AddProblemActivity.this, DesignProblemActivity.class);
                        String str_size = editText_MP_size.getText().toString();
                        int size = Integer.parseInt(str_size);
                        intent.putExtra("size",size);
                        intent.putExtra("name", str_name);
                        intent.putExtra("mode",DesignProblemActivity.MODE_ADD);
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(AddProblemActivity.this,"请换一个名字!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void showDuplicatedNameAlertDialog(final String str_name)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(AddProblemActivity.this).create();
        alertDialog.setTitle("题目已存在");
        alertDialog.setMessage("是否要继续打开 " + str_name + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(AddProblemActivity.this, DesignProblemActivity.class);
                intent.putExtra("name", str_name);
                intent.putExtra("mode",DesignProblemActivity.MODE_EDIT);
                startActivity(intent);
                finish();
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
        alertDialog.show();
    }
}
