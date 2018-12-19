package com.caragiz_studios.classifiedgallery;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.caragiz_studios.classifiedgallery.adapter.GridAdapter;
import com.caragiz_studios.classifiedgallery.database.DbDao;
import com.caragiz_studios.classifiedgallery.database.Entity;
import com.caragiz_studios.classifiedgallery.database.ImageDatabase;
import com.caragiz_studios.classifiedgallery.helpers.FullScreenActivity;

public class ActivityShowImageList extends FullScreenActivity {

    private static final String DATABASE = "image_db";
    private ImageView imageView;
    private RelativeLayout fullImage;
    private GridView imageList;
    private ImageDatabase imageDb;
    private DbDao dao;
    private Entity[] images;
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_list);
        imageView = findViewById(R.id.image_view);
        imageList = findViewById(R.id.image_list);
        fullImage = findViewById(R.id.full_img);

        categoryName = getIntent().getExtras().getString("category_name");
        imageView.setOnTouchListener(new ImageMatrixTouchHandler(imageView.getContext()));
        imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fullImage.setVisibility(View.VISIBLE);
                Glide.with(ActivityShowImageList.this)
                        .load(images[position].path)
                        .into(imageView);
            }
        });
        imageList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ActivityShowImageList.this, ""+images[position].confidence, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void hideImage(View view) {
        fullImage.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageDb = Room.databaseBuilder(this, ImageDatabase.class, DATABASE)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                images = imageDb.dbDao().loadAllInCategory(categoryName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageList.setAdapter(new GridAdapter(ActivityShowImageList.this,
                                images));
                    }
                });
            }
        }).start();
    }
}
