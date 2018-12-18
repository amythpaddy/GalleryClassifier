package com.caragiz_studios.classifiedgallery.database;

import android.arch.persistence.room.*;
import android.support.annotation.NonNull;

@android.arch.persistence.room.Entity
public class Entity {
    @PrimaryKey
    @NonNull
    public String path;
    public String category;
}
