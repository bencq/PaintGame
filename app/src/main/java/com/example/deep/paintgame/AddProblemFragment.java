package com.example.deep.paintgame;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Bencq on 2018/04/17.
 */

public class AddProblemFragment extends Fragment{

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case DesignProblemActivity.MODE_ADD:
                if(resultCode == RESULT_OK)
                {
                    activity.problemList.add(new Problem(str_name,size,null,R.drawable.questionmark_small));
                    activity.pim_adapter.notifyItemChanged(activity.problemList.size() - 1);
                }
                break;

            case DesignProblemActivity.MODE_EDIT:
                //debug
                // 更新图片
                break;

            default:
                break;
        }
    }

    private static final String TAG = "AddProblemFragment";
    public static final int MAX_SIZE = 12; //最大尺寸
    public static final int MIN_SIZE = 6; //最小尺寸

    private Button button_MPF_confirm; //确认按钮
    private Button button_MPF_cancel; //取消按钮
    private Button button_MPF_downSize; //尺寸减一按钮
    private Button button_MPF_upSize; //尺寸加一按钮
    private EditText editText_MPF_name; //名字输入框
    private EditText editText_MPF_size; //尺寸输入框 目前不可输入 以后可能改为可输入的

    private ManageProblemActivity activity;

    private String str_name;
    private int size;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_problem, container, false);

        //初始化组件
        button_MPF_confirm = view.findViewById(R.id.button_MPF_confirm);
        button_MPF_cancel = view.findViewById(R.id.button_MPF_cancel);
        button_MPF_downSize = view.findViewById(R.id.button_MPF_downSize);
        button_MPF_upSize = view.findViewById(R.id.button_MPF_upSize);
        editText_MPF_name = view.findViewById(R.id.editText_MPF_name);
        editText_MPF_size = view.findViewById(R.id.editText_MPF_size);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //获取Activity
        activity = (ManageProblemActivity) getActivity();

        //设置onClick事件
        button_MPF_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_name = editText_MPF_name.getText().toString();
                if (!TextUtils.isEmpty(str_name)) {

                    //判断重名
                    SharedPreferences sharedPreferences_problem = activity.getSharedPreferences("problem_" + str_name, Context.MODE_PRIVATE);
                    String data = sharedPreferences_problem.getString("data", null);
                    if (data != null) {
                        showDuplicatedNameAlertDialog(str_name);
                    } else {
                        Intent intent = new Intent(activity, DesignProblemActivity.class);
                        String str_size = editText_MPF_size.getText().toString();
                        size = Integer.parseInt(str_size);
                        intent.putExtra("size", size);
                        intent.putExtra("name", str_name);
                        intent.putExtra("mode", DesignProblemActivity.MODE_ADD);
                        startActivityForResult(intent,DesignProblemActivity.MODE_ADD);
                    }
                } else {
                    Toast.makeText(activity, "请换一个名字!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_MPF_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.drawerLayout.closeDrawers();
            }
        });

        button_MPF_upSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_size = editText_MPF_size.getText().toString();
                int size = Integer.parseInt(str_size);
                if (size < MAX_SIZE) {
                    ++size;
                    str_size = Integer.toString(size);
                    editText_MPF_size.setText(str_size);
                }
            }
        });

        button_MPF_downSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_size = editText_MPF_size.getText().toString();
                int size = Integer.parseInt(str_size);
                if (size > MIN_SIZE) {
                    --size;
                    str_size = Integer.toString(size);
                    editText_MPF_size.setText(str_size);
                }
            }
        });

    }

    /*重名时弹出的警告对话框*/
    void showDuplicatedNameAlertDialog(final String str_name)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("题目已存在");
        alertDialog.setMessage("是否要继续打开 " + str_name + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(activity, DesignProblemActivity.class);
                intent.putExtra("name", str_name);
                intent.putExtra("mode",DesignProblemActivity.MODE_EDIT);
                startActivityForResult(intent,DesignProblemActivity.MODE_EDIT);
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
