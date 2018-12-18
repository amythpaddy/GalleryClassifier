package com.caragiz_studios.classifiedgallery;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.caragiz_studios.classifiedgallery.database.DbDao;
import com.caragiz_studios.classifiedgallery.database.Entity;
import com.caragiz_studios.classifiedgallery.database.ImageDatabase;

public class ActivityShowImageList extends AppCompatActivity {

    private static final String DATABASE = "image_db";
    private ImageView imageView;
    private RelativeLayout fullImage;
    private GridView imageList;
    private ImageDatabase imageDb;
    private DbDao dao;
    private String[] imagePaths;
    String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_list);
        imageView = findViewById(R.id.image_view);
        imageList = findViewById(R.id.image_list);
        fullImage = findViewById(R.id.full_img);

        categoryName = getIntent().getExtras().getString("category_name");
    }

    public void hideImage(View view){
        fullImage.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageDb = Room.databaseBuilder(this , ImageDatabase.class,DATABASE)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                imagePaths = imageDb.dbDao().loadAllInCategory(categoryName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //todo add builder
                    }
                });
            }
        }).start();
    }
}
