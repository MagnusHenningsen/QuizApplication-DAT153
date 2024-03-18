package com.example.quizapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.quizapplication.ApplicationContext;
import com.example.quizapplication.Data.Option;
import com.example.quizapplication.Data.OptionViewModel;
import com.example.quizapplication.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Option> choices = new ArrayList<Option>();
    private OptionViewModel optionViewModel;

    // private final int GALLERY_REQUEST_CODE = 1;
    // private final int QUIZ_REQUEST_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        optionViewModel = new ViewModelProvider(this).get(OptionViewModel.class);
        setContentView(R.layout.activity_main);
        Button galleryBtn = findViewById(R.id.buttonGallery);
        ApplicationContext appCon = (ApplicationContext) getApplicationContext();
        appCon.setViewModelHolder(optionViewModel);
        // on click listeners
        galleryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, Gallery.class);
            startActivity(intent);
        });
        appCon.optionViewModel.getAllOptions().observe(this, list -> {
            this.choices = new ArrayList<>(list);
            appCon.update(list);
        });
        Button quizBtn = findViewById(R.id.buttonQuiz);
        quizBtn.setOnClickListener(view -> {
            if (this.choices.size() >= 3) {
                Intent intent = new Intent(this, Quiz.class);
                startActivity(intent);
            } else {
                // Send user to gallery if there isn't enough items to play
                Toast.makeText(this, getResources().getString(R.string.not_enough_items), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, Gallery.class);
                startActivity(intent);
            }
        });
        Switch hardmode = findViewById(R.id.hardmodeSwitch);
        hardmode.setChecked(appCon.isHardMode());

        // Changing colors of check states
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked},
                new int[] {-android.R.attr.state_checked}
        };

        int[] thumbColors = new int[] {
                Color.rgb(100,200,100),
                Color.GRAY
        };

        int[] trackColors = new int[] {
                Color.GREEN,
                Color.LTGRAY
        };

        ColorStateList thumbStateList = new ColorStateList(states, thumbColors);
        ColorStateList trackStateList = new ColorStateList(states, trackColors);

        hardmode.setThumbTintList(thumbStateList);
        hardmode.setTrackTintList(trackStateList);
        // setting a listener to change the state of hardmode in the applicationContext
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appCon.setHardMode(isChecked);
            }
        };
        hardmode.setOnCheckedChangeListener(listener);
    }


}