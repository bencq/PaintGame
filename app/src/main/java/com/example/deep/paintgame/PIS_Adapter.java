package com.example.deep.paintgame;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bencq on 2018/04/15.
 */

public class PIS_Adapter extends RecyclerView.Adapter<PIS_Adapter.ViewHolder>{

    public PIS_Adapter(List<Problem> problemList) {
        this.problemList = problemList;
    }

    private List<Problem> problemList;

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
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
                parent.getContext().startActivity(intent);
            }
        });
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Problem problem = problemList.get(position);
        holder.imageView_PIS_image.setImageResource(problem.getImageId());
        holder.textView_PIS_name.setText(problem.getName());
        holder.textView_PIS_size.setText(String.valueOf(problem.getSize()));
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
