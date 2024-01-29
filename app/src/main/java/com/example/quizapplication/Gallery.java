package com.example.quizapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class Gallery extends AppCompatActivity {

    private List<Uri> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        this.images = new ArrayList<>();
        addImageResourceToList(R.drawable.dog1);
        addImageResourceToList(R.drawable.dog2);
        addImageResourceToList(R.drawable.dog3);
        RecyclerView recyclerView = findViewById(R.id.imagesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns


        recyclerView.setAdapter(new MyAdapter(images)); // 'images' is your data source
        Button addBtn = this.findViewById(R.id.buttonAdd);
        addBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, newImage.class);
            startActivityForResult(intent, 101);
        });
    }
    private void addImageResourceToList(int imageResourceId) {
        Uri imageUri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE) // "android.resource"
                .authority(getResources().getResourcePackageName(imageResourceId))
                .appendPath(getResources().getResourceTypeName(imageResourceId))
                .appendPath(getResources().getResourceEntryName(imageResourceId))
                .build();
        images.add(imageUri);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            String resultString = data.getStringExtra("name");

            // Retrieve the Uri data
            Uri resultUri = data.getParcelableExtra("uri");
            Choice choice = new Choice(resultUri, resultString);
            images.add(choice.getUri());
            RecyclerView recyclerView = findViewById(R.id.imagesRecyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns


            recyclerView.setAdapter(new MyAdapter(images)); // 'images' is your data source

        }
    }
}