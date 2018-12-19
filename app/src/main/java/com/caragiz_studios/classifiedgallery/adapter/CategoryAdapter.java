package com.caragiz_studios.classifiedgallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caragiz_studios.classifiedgallery.ActivityShowCategory;
import com.caragiz_studios.classifiedgallery.ActivityShowImageList;
import com.caragiz_studios.classifiedgallery.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    String[] categories;
    Context context;

    public CategoryAdapter(String[] categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.category_card, viewGroup, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, final int i) {
        categoryViewHolder.categoryName.setText(categories[i]);
        AnimationDrawable animation;
        if(i%6 ==0) {
            animation = (AnimationDrawable) context.getResources().getDrawable(R.drawable.background_animation_1);
        }
        else if(i%6 ==1) {
            animation = (AnimationDrawable) context.getResources().getDrawable(R.drawable.background_animation_2);
        }
        else if(i%6 ==2) {
            animation = (AnimationDrawable) context.getResources().getDrawable(R.drawable.background_animation_3);
        }
        else if(i%6 ==3) {
            animation = (AnimationDrawable) context.getResources().getDrawable(R.drawable.background_animation_4);
        }
        else if(i%6 ==4) {
            animation = (AnimationDrawable) context.getResources().getDrawable(R.drawable.background_animation_5);
        }
        else {
            animation = (AnimationDrawable) context.getResources().getDrawable(R.drawable.background_animation_6);
        }
        categoryViewHolder.categoryCard.setBackground(animation);

        animation.setEnterFadeDuration(4000);
        animation.setExitFadeDuration(2000);
        animation.start();

        if (i % 2 == 0)
            categoryViewHolder.categoryCard.setGravity(Gravity.LEFT);

        categoryViewHolder.categoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, categories[i], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context , ActivityShowImageList.class);
                intent.putExtra("category_name",categories[i]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        LinearLayout categoryCard;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            categoryCard = itemView.findViewById(R.id.category_card);
        }
    }
}
