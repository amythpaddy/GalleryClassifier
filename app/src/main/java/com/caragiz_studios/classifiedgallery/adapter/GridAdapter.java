package com.caragiz_studios.classifiedgallery.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caragiz_studios.classifiedgallery.database.Entity;

public class GridAdapter extends BaseAdapter {
    Entity[] images;
    Context context;

    public GridAdapter(Context context, Entity[] images){
        this.context = context;
        this.images = images;
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ImageView imgView;
        if(view==null){
            imgView = new ImageView(context);
            imgView.setLayoutParams(new ViewGroup.LayoutParams(200,200));
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgView.setPadding(8,8,8,8);
            imgView.setElevation(5);
        }else
            imgView = (ImageView)view;

        Glide.with(context).load(images[position].path).into(imgView);
        return imgView;
    }
}
