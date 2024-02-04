package com.example.quizapplication.DataTypes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Choice implements Parcelable {

    private Integer index;
    private Uri uri;
    private String name;

    // Constructors
    public Choice(Uri uri, String name) {
        this.uri = uri;
        this.name = name;
    }

    public Choice(Integer index, Uri uri, String name) {
        this.index = index;
        this.uri = uri;
        this.name = name;
    }

    // Getters and Setters
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

    // Parcelable Implementation
    protected Choice(Parcel in) {
        index = (in.readByte() == 0) ? null : in.readInt();
        uri = in.readParcelable(Uri.class.getClassLoader());
        name = in.readString();
    }

    public static final Creator<Choice> CREATOR = new Creator<Choice>() {
        @Override
        public Choice createFromParcel(Parcel in) {
            return new Choice(in);
        }

        @Override
        public Choice[] newArray(int size) {
            return new Choice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        if (index == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(index);
        }
        parcel.writeParcelable(uri, 0); // Explicitly using 0 for flags
        parcel.writeString(name);
    }
}
