package com.caragiz_studios.classifiedgallery.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface DbDao {
    @Insert
    public void addNew(Entity entity);

    @Query("SELECT distinct category FROM entity WHERE category = :categoryName")
    public String[] loadAllInCategory(String categoryName);

    @Query("SELECT distinct category FROM entity")
    String[] loadAllCategories();

}
