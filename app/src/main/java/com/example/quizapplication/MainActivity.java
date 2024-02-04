package com.example.quizapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;

import com.example.quizapplication.DataTypes.Choice;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<Choice> choices = new ArrayList<Choice>();
    private final int GALLERY_REQUEST_CODE = 1;
    // private final int QUIZ_REQUEST_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button galleryBtn = findViewById(R.id.buttonGallery);
        initiateGallery();
        galleryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, Gallery.class);
            intent.putParcelableArrayListExtra("choices", choices);
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        });
        Button quizBtn = findViewById(R.id.buttonQuiz);
        quizBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, Quiz.class);
            intent.putParcelableArrayListExtra("choices", choices);
            startActivity(intent);
        });
    }
    private void initiateGallery() {
        this.choices = new ArrayList<Choice>();
        addImageResourceToList(R.drawable.dog1, "Rico");
        addImageResourceToList(R.drawable.dog2, "Kiki");
        addImageResourceToList(R.drawable.dog3, "Baxter");
        setIndex();
    }
    private void setIndex() {
        for (int i = 0; i < choices.size(); i++) {
            choices.get(i).setIndex(i);
        }
    }
    private void addImageResourceToList(int imageResourceId, String name) {
        Uri imageUri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE) // "android.resource"
                .authority(getResources().getResourcePackageName(imageResourceId))
                .appendPath(getResources().getResourceTypeName(imageResourceId))
                .appendPath(getResources().getResourceEntryName(imageResourceId))
                .build();
        choices.add(new Choice(imageUri, name));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Choice> updatedChoices = data.getParcelableArrayListExtra("UpdatedChoices");
            Log.i("updated choices", "Amount: " +updatedChoices.size());
            if (updatedChoices != null) {
                Log.i("updated choices", "Updating...");
                choices = updatedChoices; // Update your main list with the modified one
                setIndex();
            }
        }
    }

}