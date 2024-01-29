package com.example.quizapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class newImage extends AppCompatActivity {
    private Choice choice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image);
        this.choice = null;
        Button submit = findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            EditText nameText = findViewById(R.id.nameInput);
            choice.setName(nameText.getText().toString());
            if (choice != null) {
                if (choice.getName().isEmpty()) {
                    Toast.makeText(this, "Name is required!", Toast.LENGTH_LONG);
                } else {
                    returnIntent.putExtra("uri", choice.getUri()); // Attach your choice object
                    returnIntent.putExtra("name", choice.getName());
                    setResult(Activity.RESULT_OK, returnIntent);
                }
            } else {
                setResult(Activity.RESULT_CANCELED, returnIntent); // Indicate failure
            }
            finish(); // Close the activity and return to the parent
        });
    }

    public void getImage(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*"); // Use image/* for images of any type.
        startActivityForResult(intent, 42);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                // Use the URI to access the document, for example, to display the image
                displayImage(uri);
                choice = new Choice(uri, "test");
            }
        }
    }
    private void displayImage(Uri uri) {
        ImageView iv = findViewById(R.id.imageView2);
        iv.setImageURI(uri);
    }
}