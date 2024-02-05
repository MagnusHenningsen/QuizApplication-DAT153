package com.example.quizapplication;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import com.example.quizapplication.DataTypes.Choice;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApplicationContext extends Application {

    private ArrayList<Choice> list;

    @Override
    public void onCreate() {
        super.onCreate();
        this.list = new ArrayList<>();
        addImageResourceToList(R.drawable.dog1, "Rico");
        addImageResourceToList(R.drawable.dog2, "Kiki");
        addImageResourceToList(R.drawable.dog3, "Baxter");
        setIndex();
    }
    private void setIndex() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIndex(i);
        }
    }
    public void setList(ArrayList<Choice> list) {
        this.list = list;
    }
    public ArrayList<Choice> getList() {
        return this.list;
    }
    public boolean removeFromList(Choice c) {
        return list.remove(c);
    }
    public boolean addToList(Choice c) {
        AtomicBoolean nameIsDupe = new AtomicBoolean(false);
        AtomicBoolean uriIsDupe = new AtomicBoolean(false);
        list.stream().forEach(x -> {
            if (x.getName().toUpperCase().equals(c.getName().toUpperCase())) {
                nameIsDupe.set(true);
                Log.i("Name dupe", "Name is duplicate");
            }
            if (x.getUri().equals(c.getUri())) {
                uriIsDupe.set(true);
                Log.i("Uri dupe", "URI is duplicate");
            }
        });
        if (!nameIsDupe.get() && !uriIsDupe.get()) {
            Log.i("AppCOn", "Adding to list");
            return this.list.add(c);
        }
        return false;
    }
    private void addImageResourceToList(int imageResourceId, String name) {
        Uri imageUri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE) // "android.resource"
                .authority(getResources().getResourcePackageName(imageResourceId))
                .appendPath(getResources().getResourceTypeName(imageResourceId))
                .appendPath(getResources().getResourceEntryName(imageResourceId))
                .build();
        list.add(new Choice(imageUri, name));
    }
}
