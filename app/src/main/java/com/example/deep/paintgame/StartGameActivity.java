package com.example.deep.paintgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.deep.paintgame.adapters.PIS_Adapter;
import com.example.deep.paintgame.javaBean.Problem;
import com.example.deep.paintgame.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StartGameActivity extends AppCompatActivity {

    private static final String TAG = "StartGameActivity";

    public static final String URL_GET_PROBLEM = "http://www.scutcssu.com/paintgame/public/index.php/problem/info/getRandomProblem/";

    List<Problem> problemList;




    FloatingActionButton floatingActionButton_StartGame_getProblemFromWeb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startgame);

        floatingActionButton_StartGame_getProblemFromWeb = findViewById(R.id.floatingActionButton_StartGame_getProblemFromWeb);
        floatingActionButton_StartGame_getProblemFromWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    /*

                     返回示例:

                    {"errcode":0,"data":[{"id":1,"name":"test","size":6,"data":"111111#111111#111111#111111#111111#000000"}],"errmsg":""}
                     */

                Log.d(TAG, "onClick: ");
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        Log.d(TAG, "run: ");

                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(URL_GET_PROBLEM)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    Log.d(TAG, "onResponse: ");
                                    try
                                    {
                                        String bodyString = response.body().string();
                                        Log.d(TAG, "onResponse: " + "bodyString: " + bodyString);
                                        JSONObject jsonObject_all = new JSONObject(bodyString);
                                        int errCode = jsonObject_all.getInt("errcode");
                                        String errMsg = jsonObject_all.getString("errmsg");

                                        Log.d(TAG, "onClick: " + "errCode: " + errCode + " errMsg: " + errMsg);
                                        if (errCode != 0 || !errMsg.equals(""))
                                        {
                                            Log.d(TAG, "onResponse: " + "err");
                                            throw new IOException("network error");

                                        }
                                        JSONArray jsonArray_data = jsonObject_all.getJSONArray("data");
                                        JSONObject jsonObject_problem = jsonArray_data.getJSONObject(0);
                                        //int id = jsonObject_problem.getInt("id");
                                        String name = jsonObject_problem.getString("name");
                                        int size = jsonObject_problem.getInt("size");
                                        String data = jsonObject_problem.getString("data");

                                        Log.d(TAG, "onClick: " + "name: " + name);
                                        Log.d(TAG, "onClick: " + "size: " + size);
                                        Log.d(TAG, "onClick: " + "data: " + data);

                                        Intent intent = new Intent(StartGameActivity.this,PlayGameActivity.class);
                                        intent.putExtra("source",PlayGameActivity.SOURCE_NETWORK);
                                        intent.putExtra("name",name);
                                        intent.putExtra("data",data);
                                        intent.putExtra("size",size);

                                        StartGameActivity.this.startActivity(intent);
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }

                                }
                            });
                    }
                }.start();
            }
        });



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

    /**
     *
     * @return list of problems that saved
     */
    public List<Problem> getProblemList()
    {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(StartGameActivity.this);
        String problemNames = sharedPreferences.getString("problemNames",null);
        List<Problem> problemList = null;
        if(!TextUtils.isEmpty(problemNames))
        {
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
            Toast.makeText(StartGameActivity.this,"获取题目失败!",Toast.LENGTH_SHORT).show();
            Log.d(TAG, "getProblemList: " + "获取题目失败!");
            finish();
        }
        return problemList;
    }
}
