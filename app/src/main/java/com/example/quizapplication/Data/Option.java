package com.example.quizapplication.Data;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "options_table")
public class Option  {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "uri")
    private String uri;
    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    // Constructors
    public Option(@NonNull String uri, @NonNull String name) {
        this.uri = uri;
        this.name = name;
    }
    public Option(@NonNull Uri uri, @NonNull String name) {
        this.uri = UriTypeConverter.fromUri(uri);
        this.name = name;
    }
    // Getters and Setters
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
