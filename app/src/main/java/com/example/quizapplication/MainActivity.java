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

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button galleryBtn = findViewById(R.id.buttonGallery);
        galleryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, Gallery.class);
            startActivity(intent);
        });
        Button quizBtn = findViewById(R.id.buttonQuiz);
        quizBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, Quiz.class);
            startActivity(intent);
        });
    }
}