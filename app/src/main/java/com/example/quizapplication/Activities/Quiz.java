package com.example.quizapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizapplication.ApplicationContext;
import com.example.quizapplication.Data.Option;
import com.example.quizapplication.Data.UriTypeConverter;
import com.example.quizapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Quiz extends AppCompatActivity {

    ArrayList<Option> choices = new ArrayList<>();
    private Option current = null;
    private Option previous = null;
    private int RoundsPlayed = 0;
    private int RoundsWon = 0;
    private Random rnd = new Random();
    private ApplicationContext appCon;
    private String played_text;
    private ProgressBar pb;
    private CountDownTimer timer;
    private String won_text;
    private AtomicInteger progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        // Set the default text, we also collect the history
        played_text = getResources().getString(R.string.played);
        won_text = getResources().getString(R.string.won);
        appCon = (ApplicationContext) getApplicationContext();
        choices = appCon.options;
        SharedPreferences pref = this.getSharedPreferences("history", MODE_PRIVATE);
        RoundsPlayed = pref.getInt("played", 0);
        RoundsWon = pref.getInt("won", 0);
        Button option1 = findViewById(R.id.option1);
        Button option2 = findViewById(R.id.option2);
        Button option3 = findViewById(R.id.option3);
        Button[] btns = {option1,option2,option3};
        pb = findViewById(R.id.timer);
        if (appCon.isHardMode()) { // show progressbar if hardmode is on
            pb.setVisibility(View.VISIBLE);
        } else {
            pb.setVisibility(View.INVISIBLE);
        }
        int total_time = 30000;
        pb.setMax(total_time/50);
        progress = new AtomicInteger(0);
        timer = new CountDownTimer(total_time, 50) {
            public void onTick(long millisUntilFinished) { // pretty colors :)
                progress.set(progress.get() +1);
                pb.setProgress(progress.get());
                float percentage = (float) progress.get() / (total_time/50);

                int blendedColor = blendColors(Color.RED, Color.GREEN, percentage);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    pb.setProgressTintList(ColorStateList.valueOf(blendedColor));
                }
            }
            public void onFinish() { // increment rounds played, save to history and start next round
                RoundsPlayed++;
                SaveHistory();
                playSound(false);
                pb.setProgress(300);
                progress.set(0);
                play();
            }
            public int blendColors(int color1, int color2, float ratio) { // method for pretty colors
                final float inverseRation = 1f - ratio;

                float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
                float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
                float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);

                return Color.rgb((int) r, (int) g, (int) b);
            }

        };
        resetButtonColors(btns); // set all button colors to default color
        // set on click listeners to each option button, each also cancel the timer if needed
        option1.setOnClickListener(v -> {
            RegisterAnswer(v, option1, btns, this);
            progress.set(0);
            timer.cancel();
        });
        option2.setOnClickListener(v -> {
            RegisterAnswer(v, option2, btns, this);
            progress.set(0);
            timer.cancel();
        });
        option3.setOnClickListener(v -> {
            RegisterAnswer(v, option3, btns, this);
            progress.set(0);
            timer.cancel();
        });
        Button reset = findViewById(R.id.resetBtn);
        reset.setOnClickListener(v -> { // reset history to initial values
            RoundsPlayed = 0;
            RoundsWon = 0;
            SaveHistory();
            TextView played = findViewById(R.id.playedCounter);
            TextView won = findViewById(R.id.wonCounter);
            played.setText(played_text + " "+RoundsPlayed);
            won.setText(won_text + " "+ RoundsWon);
        });
        play();

    }
    public void SaveHistory() { // get sharedpreferences and save to it
        SharedPreferences pref = this.getSharedPreferences("history", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("played", RoundsPlayed);
        editor.putInt("won", RoundsWon);
        editor.apply();
    }
    public void RegisterAnswer(View view, Button btn, Button[] btns, Context context) {
        String answer = btn.getText().toString();
        setButtonColors(btns); // set the button colors to give feedback
        for (Button b : btns) {
            b.setEnabled(false); // disable all option buttons while doing so, this is to prevent spam
        }
        if (answer.equals(current.getName())) { // increment if correct
            RoundsWon++;
            playSound(true);
        } else {
            playSound(false);
        }
        RoundsPlayed++;
        SaveHistory();
        new Handler().postDelayed(new Runnable() { // a timer to start next round and reset colors in 1 second.
            @Override
            public void run() {
                resetButtonColors(btns);
                play();
                for (Button b : btns) {
                    b.setEnabled(true);
                }
            }
        }, 1000);
    }
    private void play() {
        do { // make sure the chosen option is not the same as previous, chose at random
            Log.i("choices: ", choices.size() + " ");
            current = choices.get(rnd.nextInt(choices.size()));
        } while (current == previous);
        previous = current; // set this up for the next round
        ImageView imageView = findViewById(R.id.quiz_current);
        imageView.setImageURI(UriTypeConverter.toUri(current.getUri())); // set image
        TextView played = findViewById(R.id.playedCounter);
        TextView won = findViewById(R.id.wonCounter);
        played.setText(played_text + " "+ RoundsPlayed);
        won.setText(won_text + " "+ RoundsWon);
        setOptions(current); // set the different options for the buttons
        if (appCon.isHardMode()) { // start timer here if hardmode
            timer.start();
        }
    }
    private void resetButtonColors(Button[] btns) { // sets all buttons to same color
        for (int i = 0; i < btns.length; i++) {
            btns[i].setBackgroundColor(getResources().getColor(R.color.buttonColor));
            btns[i].setHeight(40);
        }
    }
    private void setButtonColors(Button[] btns) { // sets the correct option green, rest red
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


    private void setOptions(Option current) {
        ArrayList<String> optionsSet = new ArrayList<>();
        // add the correct answer to the list
        optionsSet.add(current.getName());

        while (optionsSet.size() < 3) { // Now we need 3 unique options including the correct one
            int randomIndex = rnd.nextInt(appCon.getList().size()); // random int 0 -> n
            Option r = choices.get(randomIndex); // get the chosen option
            if (!optionsSet.contains(r.getName())) { // check if it has already been picked
                optionsSet.add(r.getName());
            }
        }
        // Shuffle the list to randomize the position of the correct answer
        Collections.shuffle(optionsSet);

        Button option1 = findViewById(R.id.option1);
        Button option2 = findViewById(R.id.option2);
        Button option3 = findViewById(R.id.option3);

        // Assign texts to buttons
        Button[] buttons = {option1, option2, option3};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(optionsSet.get(i));
        }
    }
    private void playSound(boolean x) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, x ? R.raw.happy_beep : R.raw.unhappy_beep);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }
}