package com.caragiz_studios.classifiedgallery;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.caragiz_studios.classifiedgallery.helpers.ImageClassifier;
import com.caragiz_studios.classifiedgallery.helpers.Model_images;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    public static List<Model_images> al_menu = new ArrayList<>();
    boolean boolean_folder = false;
    ImageView showImg;
    TextView imagePrediction;
    ImageClassifier classifier;
    int i = 0;
    int j = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showImg = findViewById(R.id.show_img);
        imagePrediction = findViewById(R.id.image_prediction);
        showImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predictImage();
            }
        });
        initializeClassifier();
        getAllImages();

    }

    private void predictImage() {
        j++;
        if(j == al_menu.get(i).getAl_imagepath().size()){
            j=0;
            i++;
        }
        if(i == al_menu.size()){
            i=0;
        }
        Glide.with(this).load(al_menu.get(i).getAl_imagepath().get(j))
                .into(showImg);
        Log.e("FOLDER", al_menu.get(i).getStr_folder());
        Log.e("FILE", al_menu.get(i).getAl_imagepath().get(j));
        classifyImage(BitmapFactory.decodeFile(al_menu.get(i).getAl_imagepath().get(j)));
    }


    public void getAllImages() {
        int pos = 0;
        Uri uri;
        Cursor cursor;
        int column_index, column_index_name;
        String absolute_path;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projections = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getContentResolver().query(uri, projections, null, null, orderBy + " DESC");
        column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolute_path = cursor.getString(column_index);
            Log.e("Column", absolute_path);
            Log.e("Folder", cursor.getString(column_index_name));

            for (int i = 0; i < al_menu.size(); i++) {
                if (al_menu.get(i).getStr_folder().equals(cursor.getString(column_index_name))) {
                    boolean_folder = true;
                    pos = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }
            if (boolean_folder) {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(al_menu.get(pos).getAl_imagepath());
                al_path.add(absolute_path);
                al_menu.get(pos).setAl_imagepath(al_path);
            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolute_path);
                Model_images obj_model = new Model_images();
                obj_model.setStr_folder(cursor.getString(column_index_name));
                obj_model.setAl_imagepath(al_path);
                al_menu.add(obj_model);
            }
        }
        predictImage();
    }

    private void initializeClassifier() {
        try {
            classifier = new ImageClassifier(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void classifyImage(Bitmap bmp) {
        if (classifier == null)
            Toast.makeText(this, "Classifier not initialized", Toast.LENGTH_SHORT).show();

        bmp = Bitmap.createScaledBitmap(bmp, 224, 224, true);
        String result = classifier.classifyFrame(bmp);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        bmp.recycle();
        Log.i("Result", result);
    }
}
