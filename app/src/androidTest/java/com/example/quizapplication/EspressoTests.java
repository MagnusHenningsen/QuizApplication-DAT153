package com.example.quizapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.example.quizapplication.Activities.MainActivity;
import com.example.quizapplication.Data.Option;


@RunWith(AndroidJUnit4.class)
public class EspressoTests {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        ActivityScenario.launch(MainActivity.class);
    }
    @Test
    public void testScoreUpdate() {
        // Click on the button to navigate to Quiz activity
        onView(withId(R.id.buttonQuiz)).perform(click());

        ApplicationContext appcon = (ApplicationContext) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        Option current = appcon.getCurrent();

        // Retrieve SharedPreferences before interaction
        SharedPreferences prefBefore = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences("history", Context.MODE_PRIVATE);
        int roundsPlayedBefore = prefBefore.getInt("played", 0);
        int roundsWonBefore = prefBefore.getInt("won", 0);

        // Click on the button with the correct answer
        onView(withText(current.getName())).perform(click());

        // Retrieve SharedPreferences after interaction
        SharedPreferences prefAfter = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences("history", Context.MODE_PRIVATE);
        int roundsPlayedAfter = prefAfter.getInt("played", 0);
        int roundsWonAfter = prefAfter.getInt("won", 0);

        // Assert that the rounds played and won are incremented by 1
        assertEquals("Rounds played should be incremented by 1", roundsPlayedBefore + 1, roundsPlayedAfter);
        assertEquals("Rounds won should be incremented by 1", roundsWonBefore + 1, roundsWonAfter);

    }







}
