package com.example.deep.paintgame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by Bencq on 2018/04/15.
 */

public class PIM_Adapter extends RecyclerView.Adapter<PIM_Adapter.ViewHolder>{

    public PIM_Adapter(List<Problem> problemList) {
        this.problemList = problemList;
        this.pim_adapter = this;
    }

    private List<Problem> problemList;

    private PIM_Adapter pim_adapter;

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.problem_item_manage,parent,false);
        final ViewHolder viewholder = new ViewHolder(view);


        viewholder.button_PIM_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final int position = viewholder.getAdapterPosition();
                Problem problem = problemList.get(position);
                final String name = problem.getName();
                AlertDialog alertDialog = new AlertDialog.Builder(parent.getContext()).create();
                alertDialog.setTitle("确认要删除 " + name + " ?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //处理RecyclerView
                        problemList.remove(position);
                        pim_adapter.notifyItemRemoved(position);

                        //处理SharedPreferences
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
                        String problemNames = sharedPreferences.getString("problemNames",null);
                        if(problemNames != null)
                        {
                            StringBuilder stringBuilder = new StringBuilder();
                            for(int j = 0; j < problemList.size(); ++j)
                            {
                                Problem problem_append = problemList.get(j);
                                stringBuilder.append(problem_append.getName()).append("#");
                            }
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("problemNames",stringBuilder.toString());
                            editor.apply();

                            //删除具体题目
                            SharedPreferences sharedPreferences_problem = parent.getContext().getSharedPreferences("problem_" + name, Context.MODE_PRIVATE);
                            sharedPreferences_problem.edit().clear().apply();
                        }
                        else
                        {
                            Toast.makeText(parent.getContext(),"删除失败!发生未知错误!",Toast.LENGTH_SHORT).show();
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

        viewholder.button_PIM_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //debug
                //准备添加回调startActivityForResult 来更新图片
                final int position = viewholder.getAdapterPosition();
                Problem problem = problemList.get(position);
                String name = problem.getName();

                Intent intent = new Intent(view.getContext(), DesignProblemActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("mode",DesignProblemActivity.MODE_EDIT);
                view.getContext().startActivity(intent);
            }
        });
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Problem problem = problemList.get(position);
        holder.imageView_PIM_image.setImageResource(problem.getImageId());
        holder.textView_PIM_name.setText(problem.getName());
        holder.textView_PIM_size.setText(String.valueOf(problem.getSize()));
    }

    @Override
    public int getItemCount() {
        return problemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView_PIM_image;
        Button button_PIM_delete;
        Button button_PIM_edit;
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
