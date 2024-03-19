package com.example.quizapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;


import com.example.quizapplication.Activities.Gallery;
import com.example.quizapplication.Activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EspressoTests {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        // Initialize Espresso Intents before each test
        Intents.init();
    }

    @After
    public void tearDown() throws Exception {
        // Release Espresso Intents after each test
        Intents.release();
    }

    @Test
    public void testButtonClickActivityTransition() {
        // Perform a click on the button
        onView(withId(R.id.buttonGallery)).perform(click());

        // Verify that an Intent was sent to start the expected Activity
        intended(hasComponent(Gallery.class.getName()));
    }
}
