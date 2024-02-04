package com.example.quizapplication.Activities;

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

import com.example.quizapplication.DataTypes.Choice;
import com.example.quizapplication.R;

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

            if (choice != null) {
                choice.setName(nameText.getText().toString());
                if (choice.getName() == null || choice.getName().isEmpty() || choice.getName().equals("")) {
                    Toast.makeText(this, "Name is required!", Toast.LENGTH_LONG).show();

                } else if (choice.getName().length() < 3) {
                    Toast.makeText(this, "Name must be atleast 3 characters!", Toast.LENGTH_LONG).show();
                } else {
                    returnIntent.putExtra("uri", choice.getUri());
                    returnIntent.putExtra("name", choice.getName());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            } else {
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }

        });
    }

    public void getImage(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*"); // Use image/* for images of any type.
        startActivityForResult(intent, 42, null);
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
                choice = new Choice(uri, null);
            }
        }
    }

    private void displayImage(Uri uri) {
        ImageView iv = findViewById(R.id.imageView2);
        iv.setImageURI(uri);
    }
}