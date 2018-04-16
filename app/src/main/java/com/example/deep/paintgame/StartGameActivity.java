package com.example.deep.paintgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StartGameActivity extends AppCompatActivity {

    private static final String TAG = "StartGameActivity";
    List<Problem> problemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startgame);



        problemList = getProblemList();
        if(problemList == null)
        {
            Log.e(TAG, "onCreate: " + "发生未知错误，problemList == null");
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView_startGame);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(StartGameActivity.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        PIS_Adapter pis_adapter = new PIS_Adapter(problemList);
        recyclerView.setAdapter(pis_adapter);
    }


    public List<Problem> getProblemList()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(StartGameActivity.this);
        String problemNames = sharedPreferences.getString("problemNames",null);
        List<Problem> problemList = null;
        if(!TextUtils.isEmpty(problemNames))
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
            Toast.makeText(StartGameActivity.this,"获取题目失败!",Toast.LENGTH_SHORT).show();
            Log.d(TAG, "getProblemList: " + "获取题目失败!");
            finish();
        }
        return problemList;
    }
}
