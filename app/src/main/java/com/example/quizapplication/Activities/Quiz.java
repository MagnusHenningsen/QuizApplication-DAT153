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

    ArrayList<Option> choices;
    private ArrayList<Option> optionsSet = new ArrayList<>();
    private Option previous = null;
    private int RoundsPlayed = 0;
    private int RoundsWon = 0;
    private final Random rnd = new Random();
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
                float percentage = (float) progress.get() / ((float) total_time /50);

                int blendedColor = blendColors(Color.RED, Color.GREEN, percentage);
                pb.setProgressTintList(ColorStateList.valueOf(blendedColor));

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
            RegisterAnswer(v, option1, btns);
            progress.set(0);
            timer.cancel();
        });
        option2.setOnClickListener(v -> {
            RegisterAnswer(v, option2, btns);
            progress.set(0);
            timer.cancel();
        });
        option3.setOnClickListener(v -> {
            RegisterAnswer(v, option3, btns);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        appCon.saveState = true;
        Option[] temp = new Option[3];
        int i = 0;
        while (i <= 3) {
            temp[i] = optionsSet.get(i);
        }
    }
    public void SaveHistory() { // get sharedpreferences and save to it
        SharedPreferences pref = this.getSharedPreferences("history", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("played", RoundsPlayed);
        editor.putInt("won", RoundsWon);
        editor.apply();
    }
    public void RegisterAnswer(View view, Button btn, Button[] btns) {
        String answer = btn.getText().toString();
        setButtonColors(btns); // set the button colors to give feedback
        for (Button b : btns) {
            b.setEnabled(false); // disable all option buttons while doing so, this is to prevent spam
        }
        if (answer.equals(appCon.getCurrent().getName())) { // increment if correct
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
        if (!appCon.saveState) {
            do { // make sure the chosen option is not the same as previous, chosen at random
                Log.i("choices: ", choices.size() + " ");
                appCon.setCurrent(choices.get(rnd.nextInt(choices.size())));
            } while (appCon.getCurrent() == previous);
        }
        previous = appCon.getCurrent(); // set this up for the next round
        ImageView imageView = findViewById(R.id.quiz_current);
        imageView.setImageURI(UriTypeConverter.toUri(appCon.getCurrent().getUri())); // set image
        TextView played = findViewById(R.id.playedCounter);
        TextView won = findViewById(R.id.wonCounter);
        played.setText(played_text + " "+ RoundsPlayed);
        won.setText(won_text + " "+ RoundsWon);
        setOptions(appCon.getCurrent()); // set the different options for the buttons
        if (appCon.isHardMode()) { // start timer here if hardmode
            timer.start();
        }
    }
    private void resetButtonColors(Button[] btns) { // sets all buttons to same color
        for (Button btn : btns) {
            btn.setBackgroundColor(getResources().getColor(R.color.buttonColor));
            btn.setHeight(40);
        }
    }
    private void setButtonColors(Button[] btns) { // sets the correct option green, rest red
        for (Button btn : btns) {
            if (btn.getText().toString().equals(appCon.getCurrent().getName())) {
                btn.setBackgroundColor(getResources().getColor(R.color.green));
                btn.setHeight(40);
            } else {
                btn.setBackgroundColor(getResources().getColor(R.color.red));
                btn.setHeight(40);
            }
        }
    }


    private void setOptions(Option current) {
        optionsSet = new ArrayList<>();
        // add the correct answer to the list
        optionsSet.add(current);

        while (optionsSet.size() < 3) { // Now we need 3 unique options including the correct one
            int randomIndex = rnd.nextInt(appCon.getList().size()); // random int 0 -> n
            Option r = choices.get(randomIndex); // get the chosen option
            if (!optionsSet.contains(r)) { // check if it has already been picked
                optionsSet.add(r);
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
            if (!appCon.saveState) {
                buttons[i].setText(optionsSet.get(i).getName());
            } else {
                buttons[i].setText(appCon.SavedOptions[i].getName());
            }
        }
        appCon.saveState = false;
        appCon.SavedOptions = new Option[3];
    }
    private void playSound(boolean x) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, x ? R.raw.happy_beep : R.raw.unhappy_beep);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        mediaPlayer.start();
    }
}