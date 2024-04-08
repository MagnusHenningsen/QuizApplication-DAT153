package com.example.quizapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.example.quizapplication.Activities.Gallery;
import com.example.quizapplication.Activities.MainActivity;
import com.example.quizapplication.Activities.newImage;
import com.example.quizapplication.Data.Option;
import com.example.quizapplication.Data.OptionViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GalleryTests {

    @Rule
    public IntentsTestRule<MainActivity> main = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void testActivityResultWithImage() {
        // Start with MainActivity
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.buttonGallery)).perform(click());

        // Now in Gallery activity, set up your ViewModel and mock Intent as before
        ApplicationContext appcon = (ApplicationContext) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        appcon.setViewModelHolder(new ViewModelProvider(main.getActivity()).get(OptionViewModel.class));

        Intent resultData = new Intent();
        Uri mockImageUri = Uri.parse("content://com.example.app/mock_image");
        resultData.setData(mockImageUri);
        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData));

        // Simulate clicking the "Add" button to add an image
        onView(withId(R.id.buttonAdd)).perform(click());

        // Enter a mock name into the EditText
        String mockName = "Mock Name";
        onView(withId(R.id.nameInput)).perform(typeText(mockName), closeSoftKeyboard());
        // Stub the intent for image selection
        intending(hasAction(Intent.ACTION_OPEN_DOCUMENT)).respondWith(
                new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent().setData(mockImageUri))
        );

        // Perform the click that should trigger the image selection intent
        onView(withId(R.id.buttonAdd)).perform(click());
        // Click the "Submit" button
        onView(withId(R.id.buttonSubmit)).perform(click());
        // Assert your expected outcomes here
    }
}



