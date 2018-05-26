package com.example.deep.paintgame.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deep.paintgame.DesignProblemActivity;
import com.example.deep.paintgame.ManageProblemActivity;
import com.example.deep.paintgame.R;
import com.example.deep.paintgame.javaBean.Problem;
import com.example.deep.paintgame.utils.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Bencq on 2018/04/15.
 */

public class PIM_Adapter extends RecyclerView.Adapter<PIM_Adapter.ViewHolder> {

    private static final String TAG = "PIM_Adapter";

    public static final String URL_UPLOAD_PROBLEM = "http://www.scutcssu.com/paintgame/public/index.php/problem/info/createProblem";

    public PIM_Adapter(List<Problem> problemList) {
        this.problemList = problemList;
        this.pim_adapter = this;
    }

    private List<Problem> problemList;
    private PIM_Adapter pim_adapter;

    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.problem_item_manage, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);


        viewHolder.imageButton_PIM_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final int position = viewHolder.getAdapterPosition();
                final Problem problem = problemList.get(position);
                final String name = problem.getName();
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("确认要删除 " + name + " ?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //处理RecyclerView
                        problemList.remove(position);
                        pim_adapter.notifyItemRemoved(position);

                        //处理SharedPreferences
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        String problemNames = sharedPreferences.getString("problemNames", null);
                        if (problemNames != null) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int j = 0; j < problemList.size(); ++j) {
                                Problem problem_append = problemList.get(j);
                                stringBuilder.append(problem_append.getName()).append("*");
                            }
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("problemNames", stringBuilder.toString());
                            editor.apply();

                            //删除具体题目
                            SharedPreferences sharedPreferences_problem = context.getSharedPreferences("problem_" + name, Context.MODE_PRIVATE);
                            sharedPreferences_problem.edit().clear().apply();

                            //删除图片
                            File file_image = Paths.getImageFile(context, problem.getName());
                            Log.d(TAG, "onClick: " + "file_image.exists(): " + file_image.exists());
                            if (file_image.exists()) {
                                boolean isDeleteSuccessful = file_image.delete();
                                Log.d(TAG, "onClick: " + "isDeleteSuccessful: " + isDeleteSuccessful);
                            } else {
                                Toast.makeText(context, "删除图片失败!发生未知错误!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "删除失败!发生未知错误!", Toast.LENGTH_SHORT).show();
                        }
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

        viewHolder.imageButton_PIM_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final int position = viewHolder.getAdapterPosition();
                Problem problem = problemList.get(position);
                String name = problem.getName();

                Intent intent = new Intent(view.getContext(), DesignProblemActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("mode", DesignProblemActivity.MODE_EDIT);
                intent.putExtra("position", position);
                ((ManageProblemActivity) (context)).startActivityForResult(intent, DesignProblemActivity.MODE_EDIT);

            }
        });

        viewHolder.imageButton_PIM_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final int position = viewHolder.getAdapterPosition();
                final Problem problem = problemList.get(position);


                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("确认要上传 " + problem.getName() + " ?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Thread getNetworkProblem = new Thread() {
                            @Override
                            public void run() {

                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                                String identity = sharedPreferences.getString("identity","");
                                if (identity.equals(""))
                                {
                                    identity = "No id";
                                }


                                FormBody formBody = new FormBody.Builder()
                                        .add("name",problem.getName())
                                        .add("size", String.valueOf(problem.getSize()))
                                        .add("data",problem.getData())
                                        .add("identity",identity)
                                        .build();

                                OkHttpClient okHttpClient = new OkHttpClient();


                                Request request = new Request.Builder()
                                        .url(URL_UPLOAD_PROBLEM)
                                        .put(formBody)
                                        .build();


                                okHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                        Log.d(TAG, "onFailure: ");
                                        Toast.makeText(context,"发送请求失败!",Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }


                                    @Override
                                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                                        Looper.prepare();

                                        Log.d(TAG, "onResponse: ");
                                        String bodyString = response.body().string();
                                        try
                                        {
                                            JSONObject jsonObject_all = new JSONObject(bodyString);
                                            int errCode = jsonObject_all.getInt("errcode");
                                            String errMsg = jsonObject_all.getString("errmsg");
                                            Log.d(TAG, "onResponse: " + "errCode: " + errCode);
                                            Log.d(TAG, "onResponse: " + "errMsg: " + errMsg);
                                            if(jsonObject_all.has("data"))
                                            {
                                                Object object = jsonObject_all.get("data");

                                                Log.d(TAG, "onResponse: " + "respString: " + object);
                                            }
                                            if(errCode != 0 || !errMsg.equals(""))
                                            {
                                                Log.d(TAG, "onResponse: " + "题目上传失败!" + "\n返回信息: " + errMsg);
                                                Toast.makeText(context,"题目上传失败!" + "\n返回信息: " + errMsg,Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Log.d(TAG, "onResponse: " + "题目 " + problem.getName() + " 上传成功!");
                                                Toast.makeText(context,"题目 " + problem.getName() + " 上传成功!",Toast.LENGTH_SHORT).show();
                                            }

                                            Looper.loop();
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        };

                        getNetworkProblem.start();
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


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Problem problem = problemList.get(position);

        try
        {
            Bitmap bitmap = BitmapFactory.decodeStream(context.openFileInput(Paths.getImageFileName(problem.getName())));
            holder.imageView_PIM_image.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        holder.textView_PIM_name.setText(problem.getName());
        int size = problem.getSize();
        holder.textView_PIM_size.setText(String.valueOf(size));
        switch (size)
        {
            case 6:
                holder.textView_PIM_size.setTextColor(Color.RED);
                break;
            case 7:
                //orange
                holder.textView_PIM_size.setTextColor(Color.rgb(255,165,0));
                break;
            case 8:
                holder.textView_PIM_size.setTextColor(Color.YELLOW);
                break;
            case 9:
                holder.textView_PIM_size.setTextColor(Color.GREEN);
                break;
            case 10:
                holder.textView_PIM_size.setTextColor(Color.CYAN);
                break;
            case 11:
                holder.textView_PIM_size.setTextColor(Color.BLUE);
                break;
            case 12:
                //purple
                holder.textView_PIM_size.setTextColor(Color.rgb(139,0,255));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return problemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView_PIM_image;

        public ImageButton imageButton_PIM_edit;
        ImageButton imageButton_PIM_upload;
        ImageButton imageButton_PIM_delete;
        TextView textView_PIM_name;
        TextView textView_PIM_size;

        ViewHolder(View itemView) {
            super(itemView);
            imageView_PIM_image = itemView.findViewById(R.id.imageView_PIM_image);
            imageButton_PIM_edit = itemView.findViewById(R.id.imageButton_PIM_edit);
            imageButton_PIM_upload = itemView.findViewById(R.id.imageButton_PIM_upload);
            imageButton_PIM_delete = itemView.findViewById(R.id.imageButton_PIM_delete);
            textView_PIM_name = itemView.findViewById(R.id.textView_PIM_name);
            textView_PIM_size = itemView.findViewById(R.id.textView_PIM_size);
        }
    }
}
