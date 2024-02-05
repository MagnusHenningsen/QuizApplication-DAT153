package com.example.quizapplication.DataTypes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Option implements Parcelable {

    private Integer index;
    private Uri uri;
    private String name;

    // Constructors
    public Option(Uri uri, String name) {
        this.uri = uri;
        this.name = name;
    }

    public Option(Integer index, Uri uri, String name) {
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
    protected Option(Parcel in) {
        index = (in.readByte() == 0) ? null : in.readInt();
        uri = in.readParcelable(Uri.class.getClassLoader());
        name = in.readString();
    }

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
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
