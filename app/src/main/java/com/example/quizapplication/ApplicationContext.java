package com.example.quizapplication;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;

import com.example.quizapplication.DataTypes.Option;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApplicationContext extends Application {

    private ArrayList<Option> list;


    private AtomicBoolean HardMode;
    @Override
    public void onCreate() {
        super.onCreate();
        // instantiate the global list
        this.list = new ArrayList<>();
        HardMode = new AtomicBoolean(false);
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
    public void setList(ArrayList<Option> list) {
        this.list = list;
    }
    public ArrayList<Option> getList() {
        return this.list;
    }
    public boolean removeFromList(Option c) {
        return list.remove(c);
    }
    public boolean addToList(Option c) {
        AtomicBoolean nameIsDupe = new AtomicBoolean(false);
        AtomicBoolean uriIsDupe = new AtomicBoolean(false);
        // check for duplications in the list
        // only using atomicboolean due to java demanding it if you want to use forEach, and we are stubborn
        list.stream().forEach(x -> {
            if (x.getName().toUpperCase().equals(c.getName().toUpperCase())) {
                nameIsDupe.set(true);
                //Log.i("Name dupe", "Name is duplicate");
            }
            if (x.getUri().equals(c.getUri())) {
                uriIsDupe.set(true);
                //Log.i("Uri dupe", "URI is duplicate");
            }
        });
        if (!nameIsDupe.get() && !uriIsDupe.get()) {
            //Log.i("AppCOn", "Adding to list");
            return this.list.add(c);
        }
        return false;
    }
    private void addImageResourceToList(int imageResourceId, String name) {
        // collect preset image and return the new option
        Uri imageUri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE) // "android.resource"
                .authority(getResources().getResourcePackageName(imageResourceId))
                .appendPath(getResources().getResourceTypeName(imageResourceId))
                .appendPath(getResources().getResourceEntryName(imageResourceId))
                .build();
        list.add(new Option(imageUri, name));
    }
    // get hardcore
    public boolean isHardMode() {
        return HardMode.get();
    }
    // set hardcore
    public void setHardMode(boolean hardMode) {
        HardMode.set(hardMode);
    }

}
