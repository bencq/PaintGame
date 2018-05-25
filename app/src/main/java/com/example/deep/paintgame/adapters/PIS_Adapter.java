package com.example.deep.paintgame.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deep.paintgame.PlayGameActivity;
import com.example.deep.paintgame.R;
import com.example.deep.paintgame.javaBean.Problem;
import com.example.deep.paintgame.utils.SkxDrawableHelper;

import java.util.List;

/**
 * Created by Bencq on 2018/04/15.
 */

public class PIS_Adapter extends RecyclerView.Adapter<PIS_Adapter.ViewHolder>{



    public PIS_Adapter(List<Problem> problemList) {
        this.problemList = problemList;
    }

    private List<Problem> problemList;

    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.problem_item_start,parent,false);
        final ViewHolder viewholder = new ViewHolder(view);
        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent.getContext(),PlayGameActivity.class);
                int position = viewholder.getAdapterPosition();
                Problem problem = problemList.get(position);
                intent.putExtra("name",problem.getName());
                intent.putExtra("size",problem.getSize());
                intent.putExtra("source",PlayGameActivity.SOURCE_LOCAL);
                parent.getContext().startActivity(intent);
            }
        });
        return viewholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Problem problem = problemList.get(position);
        Drawable drawable_questionMark = ContextCompat.getDrawable(context,R.drawable.question_mark);
        holder.textView_PIS_name.setText(problem.getName());
        int size = problem.getSize();
        holder.textView_PIS_size.setText(String.valueOf(size));
        switch (size)
        {
            case 6:
                holder.textView_PIS_size.setTextColor(Color.RED);
                holder.imageView_PIS_image.setImageDrawable(SkxDrawableHelper.tintDrawable(drawable_questionMark,Color.RED));
                break;
            case 7:
                //orange
                holder.textView_PIS_size.setTextColor(Color.rgb(255,165,0));
                holder.imageView_PIS_image.setImageDrawable(SkxDrawableHelper.tintDrawable(drawable_questionMark,Color.rgb(255,165,0)));
                break;
            case 8:
                holder.textView_PIS_size.setTextColor(Color.YELLOW);
                holder.imageView_PIS_image.setImageDrawable(SkxDrawableHelper.tintDrawable(drawable_questionMark,Color.YELLOW));
                break;
            case 9:
                holder.textView_PIS_size.setTextColor(Color.GREEN);
                holder.imageView_PIS_image.setImageDrawable(SkxDrawableHelper.tintDrawable(drawable_questionMark,Color.GREEN));
                break;
            case 10:
                holder.textView_PIS_size.setTextColor(Color.CYAN);
                holder.imageView_PIS_image.setImageDrawable(SkxDrawableHelper.tintDrawable(drawable_questionMark,Color.CYAN));
                break;
            case 11:
                holder.textView_PIS_size.setTextColor(Color.BLUE);
                holder.imageView_PIS_image.setImageDrawable(SkxDrawableHelper.tintDrawable(drawable_questionMark,Color.BLUE));
                break;
            case 12:
                //purple
                holder.textView_PIS_size.setTextColor(Color.rgb(139,0,255));
                holder.imageView_PIS_image.setImageDrawable(SkxDrawableHelper.tintDrawable(drawable_questionMark,Color.rgb(139,0,255)));
                break;
        }





    }

    @Override
    public int getItemCount() {
        return problemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView_PIS_image;
        TextView textView_PIS_name;
        TextView textView_PIS_size;

        ViewHolder(View itemView) {
            super(itemView);
            imageView_PIS_image = itemView.findViewById(R.id.imageView_PIS_image);
            textView_PIS_name = itemView.findViewById(R.id.textView_PIS_name);
            textView_PIS_size = itemView.findViewById(R.id.textView_PIS_size);
        }
    }
}
