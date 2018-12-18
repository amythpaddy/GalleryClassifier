package com.caragiz_studios.classifiedgallery.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Entity.class}, version = 1, exportSchema = false)
public abstract class ImageDatabase extends RoomDatabase{
    public abstract DbDao dbDao();
}
