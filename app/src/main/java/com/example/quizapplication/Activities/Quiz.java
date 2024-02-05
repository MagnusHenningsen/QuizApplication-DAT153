package com.example.quizapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quizapplication.ApplicationContext;
import com.example.quizapplication.DataTypes.Choice;
import com.example.quizapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Quiz extends AppCompatActivity {

    ArrayList<Choice> choices = new ArrayList<>();
    private Choice current = null;
    private Choice previous = null;
    private int RoundsPlayed = 0;
    private int RoundsWon = 0;
    private Random rnd = new Random();
    private ApplicationContext appCon;
    private static final String played_text = "Games played: ";
    private static final String won_text = "Games won: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        //choices = getIntent().getParcelableArrayListExtra("choices");

        appCon = (ApplicationContext) getApplicationContext();
        choices = appCon.getList();
        SharedPreferences pref = this.getSharedPreferences("history", MODE_PRIVATE);
        RoundsPlayed = pref.getInt("played", 0);
        RoundsWon = pref.getInt("won", 0);
        Button option1 = findViewById(R.id.option1);
        Button option2 = findViewById(R.id.option2);
        Button option3 = findViewById(R.id.option3);
        Button[] btns = {option1,option2,option3};
        resetButtonColors(btns);
        option1.setOnClickListener(v -> {
            RegisterAnswer(v, option1, btns, this);
        });
        option2.setOnClickListener(v -> {
            RegisterAnswer(v, option2, btns, this);
        });
        option3.setOnClickListener(v -> {
            RegisterAnswer(v, option3, btns, this);
        });
        Button reset = findViewById(R.id.resetBtn);
        reset.setOnClickListener(v -> {
            RoundsPlayed = 0;
            RoundsWon = 0;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("played", RoundsPlayed);
            editor.putInt("won", RoundsWon);
            editor.apply();
            TextView played = findViewById(R.id.playedCounter);
            TextView won = findViewById(R.id.wonCounter);
            played.setText(played_text + RoundsPlayed);
            won.setText(won_text + RoundsWon);
        });
        play();

    }

    public void RegisterAnswer(View view, Button btn, Button[] btns, Context context) {
        String answer = btn.getText().toString();
        setButtonColors(btns);
        if (answer.equals(current.getName())) {
            RoundsWon++;

        }
        RoundsPlayed++;
        SharedPreferences pref = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("played", RoundsPlayed);
        editor.putInt("won", RoundsWon);
        editor.apply();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetButtonColors(btns);
                play();
            }
        }, 1000);
    }
    private void play() {
        do {
            current = choices.get(rnd.nextInt(choices.size()));
        } while (current == previous);
        previous = current;
        ImageView imageView = findViewById(R.id.quiz_current);
        imageView.setImageURI(current.getUri());
        TextView played = findViewById(R.id.playedCounter);
        TextView won = findViewById(R.id.wonCounter);
        played.setText(played_text + RoundsPlayed);
        won.setText(won_text + RoundsWon);
        setOptions(current);
    }
    private void resetButtonColors(Button[] btns) {
        for (int i = 0; i < btns.length; i++) {
            btns[i].setBackgroundColor(getResources().getColor(R.color.buttonColor));
            btns[i].setHeight(40);
        }
    }
    private void setButtonColors(Button[] btns) {
        for (int i = 0; i < btns.length; i++) {
            if (btns[i].getText().toString().equals(current.getName())) {
                btns[i].setBackgroundColor(getResources().getColor(R.color.green));
                btns[i].setHeight(40);
            } else {
                btns[i].setBackgroundColor(getResources().getColor(R.color.red));
                btns[i].setHeight(40);
            }
        }
    }


    private void setOptions(Choice current) {
        ArrayList<String> optionsSet = new ArrayList<>();
        int attempts = 0;

        // Add the index of the correct answer to ensure it's not selected as an incorrect option
        optionsSet.add(current.getName());

        while (optionsSet.size() < 3) { // Now we need 3 unique options including the correct one
            int randomIndex = rnd.nextInt(appCon.getList().size());
            Choice r = choices.get(randomIndex);
            if (!optionsSet.contains(r.getName())) {
                optionsSet.add(r.getName());
                attempts++;
                Log.i("attempt", attempts + "");
            }
        }

        Collections.shuffle(optionsSet); // Shuffle the list to randomize the position of the correct answer

        Button option1 = findViewById(R.id.option1);
        Button option2 = findViewById(R.id.option2);
        Button option3 = findViewById(R.id.option3);

        // Assign texts to buttons
        Button[] buttons = {option1, option2, option3};
        for (int i = 0; i < buttons.length; i++) {
            // Set the text for each button. The correct answer is already included in optionsSet.
            buttons[i].setText(optionsSet.get(i));
        }
    }
}