package com.example.deep.paintgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deep.paintgame.adapters.PIM_Adapter;
import com.example.deep.paintgame.javaBean.Problem;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Bencq on 2018/04/17.
 */

public class AddProblemFragment extends Fragment{


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

    public String str_name;
    public int size;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case DesignProblemActivity.MODE_ADD:
                if(resultCode == RESULT_OK)
                {
                    Log.d(TAG, "onActivityResult: " + "add notify pic suc");
                    activity.problemList.add(new Problem(str_name,size,null,R.drawable.question_mark));
                    activity.pim_adapter.notifyItemChanged(activity.problemList.size() - 1);
                }
                break;


            case DesignProblemActivity.MODE_EDIT:
                //debug
                // 更新图片
                if(resultCode == RESULT_OK)
                {
                    int position = data.getIntExtra("position",-1);
                    Log.d(TAG, "onActivityResult: " + "position: " + position);
                    if(position == -1)
                    {
                        Toast.makeText(activity,"未知错误导致更新图片失败!",Toast.LENGTH_SHORT).show(); //debug
                    }
                    else
                    {
                        activity.pim_adapter.notifyItemChanged(position);
                    }
                }
                break;

            default:
                Log.d(TAG, "onActivityResult: " + "requestCode: " + requestCode);
                break;
        }
    }


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

        view.setClickable(true);
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

                    int position = -1;

                    for (int i = 0; i < activity.problemList.size(); ++i)
                    {
                        //判断重名
                        if(activity.problemList.get(i).getName().equals(str_name))
                        {
                            position = i;
                            break;
                        }
                    }
                    if (position != -1) {
                        showDuplicatedNameAlertDialog(position);
                    } else {
                        Intent intent = new Intent(activity, DesignProblemActivity.class);
                        String str_size = editText_MPF_size.getText().toString();
                        size = Integer.parseInt(str_size);
                        intent.putExtra("size", size);
                        intent.putExtra("name", str_name);
                        intent.putExtra("mode", DesignProblemActivity.MODE_ADD);
                        activity.startActivityForResult(intent,DesignProblemActivity.MODE_ADD);

                        activity.drawerLayout.closeDrawers();
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
    void showDuplicatedNameAlertDialog(final int position)
    {
        Problem problem = activity.problemList.get(position);
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("题目已存在");
        alertDialog.setMessage("是否要继续打开 " + problem.getName() + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //调用按钮的onClick函数防止BUG
                PIM_Adapter.ViewHolder viewHolder = (PIM_Adapter.ViewHolder) activity.recyclerView_MP.findViewHolderForAdapterPosition(position);
                viewHolder.imageButton_PIM_edit.callOnClick();
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
