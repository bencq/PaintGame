package com.example.deep.paintgame.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deep.paintgame.DesignProblemActivity;
import com.example.deep.paintgame.ManageProblemActivity;
import com.example.deep.paintgame.R;
import com.example.deep.paintgame.javaBean.Problem;
import com.example.deep.paintgame.utils.Paths;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by Bencq on 2018/04/15.
 */

public class PIM_Adapter extends RecyclerView.Adapter<PIM_Adapter.ViewHolder>{

    private static final String TAG = "PIM_Adapter";

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

        View view = LayoutInflater.from(context).inflate(R.layout.problem_item_manage,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);


        viewHolder.button_PIM_delete.setOnClickListener(new View.OnClickListener() {
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
                        String problemNames = sharedPreferences.getString("problemNames",null);
                        if(problemNames != null)
                        {
                            StringBuilder stringBuilder = new StringBuilder();
                            for(int j = 0; j < problemList.size(); ++j)
                            {
                                Problem problem_append = problemList.get(j);
                                stringBuilder.append(problem_append.getName()).append("*");
                            }
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("problemNames",stringBuilder.toString());
                            editor.apply();

                            //删除具体题目
                            SharedPreferences sharedPreferences_problem = context.getSharedPreferences("problem_" + name, Context.MODE_PRIVATE);
                            sharedPreferences_problem.edit().clear().apply();

                            //删除图片
                            File file_image = Paths.getImageFile(context,problem.getName());
                            Log.d(TAG, "onClick: " + "file_image.exists(): " + file_image.exists());
                            if(file_image.exists())
                            {
                                boolean isDeleteSuccessful = file_image.delete();
                                Log.d(TAG, "onClick: " + "isDeleteSuccessful: " + isDeleteSuccessful);
                            }
                            else
                            {
                                Toast.makeText(context,"删除图片失败!发生未知错误!",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"删除失败!发生未知错误!",Toast.LENGTH_SHORT).show();
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

        viewHolder.button_PIM_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final int position = viewHolder.getAdapterPosition();
                Problem problem = problemList.get(position);
                String name = problem.getName();

                Intent intent = new Intent(view.getContext(), DesignProblemActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("mode",DesignProblemActivity.MODE_EDIT);
                intent.putExtra("position",position);
                ((ManageProblemActivity)(context)).startActivityForResult(intent,DesignProblemActivity.MODE_EDIT);

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
        Button button_PIM_delete;
        public Button button_PIM_edit;
        TextView textView_PIM_name;
        TextView textView_PIM_size;

        ViewHolder(View itemView) {
            super(itemView);
            imageView_PIM_image = itemView.findViewById(R.id.imageView_PIM_image);
            button_PIM_delete = itemView.findViewById(R.id.button_PIM_delete);
            button_PIM_edit = itemView.findViewById(R.id.button_PIM_edit);
            textView_PIM_name = itemView.findViewById(R.id.textView_PIM_name);
            textView_PIM_size = itemView.findViewById(R.id.textView_PIM_size);
        }
    }
}
