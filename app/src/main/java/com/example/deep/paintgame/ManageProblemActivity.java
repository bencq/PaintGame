package com.example.deep.paintgame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.deep.paintgame.adapters.PIM_Adapter;
import com.example.deep.paintgame.animation.Animation;
import com.example.deep.paintgame.javaBean.Problem;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import com.example.deep.paintgame.utils.Paths;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ManageProblemActivity extends AppCompatActivity {

    private static final String TAG = "ManageProblemActivity";
    public DrawerLayout drawerLayout;
    public PIM_Adapter pim_adapter;
    public List<Problem> problemList;
    public RecyclerView recyclerView_MP;
    public AddProblemFragment addProblemFragment;


    FloatingActionButton floatingActionButton_MP_addProblem;
    ImageView imageView_tool;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {

            case DesignProblemActivity.MODE_ADD:
                if(resultCode == RESULT_OK)
                {
                    Log.d(TAG, "onActivityResult: " + "add notify pic suc");
                    problemList.add(new Problem(addProblemFragment.str_name,addProblemFragment.size,null,R.drawable.question_mark));
                    pim_adapter.notifyItemChanged(problemList.size() - 1);
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
                        Toast.makeText(ManageProblemActivity.this,"未知错误导致更新图片失败!",Toast.LENGTH_SHORT).show(); //debug
                    }
                    else
                    {
                        pim_adapter.notifyItemChanged(position);
                    }
                }
                break;

            default:
                Log.d(TAG, "onActivityResult: " + "requestCode: " + requestCode);
                break;
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuItem_addProblem:
                drawerLayout.openDrawer(Gravity.END);
                break;

            default:
                break;
        }
        return true;
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageproblem);

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(100);
        textPaint.setColor(Color.WHITE);
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);
        Target viewTarget = new ViewTarget(R.id.floatingActionButton_MP_addProblem, this);     //R.id.rb4指定的要突出显示的控件的id
        ShowcaseView sv = new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setStyle(R.style.CustomText)  //设置自定义样式
                .setTarget(viewTarget)
                .setContentText("新建题目")        //提示的内容
                .setContentTextPaint(textPaint)
                .hideOnTouchOutside()   //点击空白位置也会被隐藏
                .singleShot(5)       //用于单次显示. 当下次发现这个int值所标示的已经显示过了,就不再显示.int值自己随便写,注意不要重复就好
                .build();
        sv.setButtonPosition(lps);

        addProblemFragment = (AddProblemFragment)(getSupportFragmentManager().findFragmentById(R.id.fragment_MP_addProblem));
        drawerLayout = findViewById(R.id.drawerLayout_MP);



        floatingActionButton_MP_addProblem = findViewById(R.id.floatingActionButton_MP_addProblem);
        floatingActionButton_MP_addProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.END);
            }
        });

        problemList = getProblemList();
        if(problemList == null)
        {
            Log.e(TAG, "onCreate: " + "发生未知错误，problemList == null");
            return;
        }

        if(problemList.size() < MainActivity.problemNames.length)
        {
            addRecoverButton();
        }
        else
        {
            for(int i = 0;i < MainActivity.problemNames.length; ++i)
            {
                String name = problemList.get(i).getName();
                if(!MainActivity.problemNames[i].equals(name))
                {
                    addRecoverButton();
                    break;
                }
            }
        }


        recyclerView_MP = findViewById(R.id.recyclerView_MP);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ManageProblemActivity.this,2);
        recyclerView_MP.setLayoutManager(gridLayoutManager);
        pim_adapter = new PIM_Adapter(problemList);
        recyclerView_MP.setAdapter(pim_adapter);
    }

    private void addRecoverButton() {
        imageView_tool = findViewById(R.id.imageView_tool);
        imageView_tool.startAnimation(Animation.animation_rotate);
        imageView_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(ManageProblemActivity.this).create();
                alertDialog.setTitle("恢复默认题目");
                alertDialog.setMessage("要继续吗？");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.initProblems(ManageProblemActivity.this);
                        Toast.makeText(ManageProblemActivity.this,"恢复成功!",Toast.LENGTH_SHORT).show();
                        imageView_tool.setOnClickListener(null);
                        imageView_tool.clearAnimation();
                    }
                });

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
            }
        });
    }


    public List<Problem> getProblemList()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ManageProblemActivity.this);
        String problemNames = sharedPreferences.getString("problemNames",null);
        List<Problem> problemList = null;
        if(problemNames != null)
        {
            if(!problemNames.equals(""))
            {
                Log.d(TAG, "getProblemList: " + "problemNames: " + problemNames);
                String names[] = problemNames.split("\\*");
                int problemCount = names.length;
                problemList = new ArrayList<>(problemCount);
                for(int i = 0; i < problemCount; ++i)
                {
                    SharedPreferences sharedPreferences_problem = getSharedPreferences("problem_" + names[i],MODE_PRIVATE);
                    int size = sharedPreferences_problem.getInt("size",0);
                    String data = sharedPreferences_problem.getString("data",null);
                    Problem problem = new Problem(names[i],size,data,R.drawable.question_mark);
                    problemList.add(problem);
                }
            }
            else
            {
                problemList = new ArrayList<>(0);
                Toast.makeText(ManageProblemActivity.this,"题目库为空",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(ManageProblemActivity.this,"获取题目失败!",Toast.LENGTH_SHORT).show();
            Log.d(TAG, "getProblemList: " + "获取题目失败!");
            finish();
        }
        return problemList;
    }

    public void showToast(CharSequence text, int duration)
    {
        Toast.makeText(ManageProblemActivity.this,text,duration).show();
    }
}
