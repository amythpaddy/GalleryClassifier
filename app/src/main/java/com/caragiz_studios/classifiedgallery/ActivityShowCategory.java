package com.caragiz_studios.classifiedgallery;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caragiz_studios.classifiedgallery.adapter.CategoryAdapter;
import com.caragiz_studios.classifiedgallery.database.Entity;
import com.caragiz_studios.classifiedgallery.database.ImageDatabase;
import com.caragiz_studios.classifiedgallery.helpers.FullScreenActivity;

public class ActivityShowCategory extends FullScreenActivity {

    ImageDatabase imageDb;
    RecyclerView categoryList;
    private static final String DATABASE = "image_db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_category);
        categoryList = findViewById(R.id.category_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageDb = Room.databaseBuilder(getApplicationContext(), ImageDatabase.class, DATABASE)
                .fallbackToDestructiveMigration()
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String[] categories = imageDb.dbDao().loadAllCategories();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            categoryList.setLayoutManager(new LinearLayoutManager(ActivityShowCategory.this));
                            categoryList.setAdapter(new CategoryAdapter(categories));
                        }
                    });
            }
        }).start();
    }
}
