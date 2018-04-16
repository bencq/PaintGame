package com.example.deep.paintgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ManageProblemActivity extends AppCompatActivity {

    private static final String TAG = "ManageProblemActivity";
    List<Problem> problemList;

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
                Intent intent = new Intent(ManageProblemActivity.this,AddProblemActivity.class);
                ManageProblemActivity.this.startActivity(intent);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageproblem);

        problemList = getProblemList();
        if(problemList == null)
        {
            Log.e(TAG, "onCreate: " + "发生未知错误，problemList == null");
            return;
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView_manageProblem);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ManageProblemActivity.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        PIM_Adapter pim_adapter = new PIM_Adapter(problemList);
        recyclerView.setAdapter(pim_adapter);
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
                String names[] = problemNames.split("#");
                int problemCount = names.length;
                problemList = new ArrayList<>(problemCount);
                for(int i = 0; i < problemCount; ++i)
                {
                    SharedPreferences sharedPreferences_problem = getSharedPreferences("problem_" + names[i],MODE_PRIVATE);
                    int size = sharedPreferences_problem.getInt("size",0);
                    String data = sharedPreferences_problem.getString("data",null);
                    Problem problem = new Problem(names[i],size,data,R.drawable.questionmark_small);
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
}
