package com.example.quizapplication;

import android.net.Uri;

public class Choice {


    private Integer index;
    private Uri uri;



    private String name;
    public Choice(Uri uri, String name) {
        this.uri = uri;
        this.name = name;
    }
    public Choice(Integer index, Uri uri, String name) {
        this.uri = uri;
        this.index = index;
        this.name = name;
    }
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
