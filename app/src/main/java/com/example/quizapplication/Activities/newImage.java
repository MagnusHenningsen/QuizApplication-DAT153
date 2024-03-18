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

import com.example.quizapplication.ApplicationContext;
import com.example.quizapplication.Data.Option;
import com.example.quizapplication.Data.UriTypeConverter;
import com.example.quizapplication.R;

public class newImage extends AppCompatActivity {
    private Option choice;
    private ApplicationContext appCon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image);
        this.appCon = (ApplicationContext) getApplicationContext();
        this.choice = null;
        Button submit = findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(view -> {
            EditText nameText = findViewById(R.id.nameInput);
            // if the choice is null we give a toast message to explain the situation
            if (choice != null) {
                choice.setName(nameText.getText().toString());
                // if the name is null or "illegal" we give an appropriate message
                if (choice.getName() == null || choice.getName().isEmpty() || choice.getName().equals("")) {
                    Toast.makeText(this, getResources().getString(R.string.name_required), Toast.LENGTH_LONG).show();

                } else if (choice.getName().length() < 3) {
                    Toast.makeText(this, getResources().getString(R.string.name_length), Toast.LENGTH_LONG).show();
                } else {
                    // add to global list
                    if (appCon.addToList(choice)) {
                        setResult(Activity.RESULT_OK, null);
                        finish();
                    } else {
                        // duplicate warning and denied addition
                        Toast.makeText(this, getResources().getString(R.string.duplicate), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.submit_nothing), Toast.LENGTH_LONG).show();
            }

        });
    }

    public void getImage(View view) {
        // pass our intent to open a document
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        // we only want those of the category openable
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*"); // type
        startActivityForResult(intent, 42, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that the user selected.
            Uri uri = null;
            if (resultData != null) {
                try {
                    uri = resultData.getData();
                    // display the given image
                    displayImage(uri);

                    choice = new Option(UriTypeConverter.fromUri(uri), null);
                    // needed lasting uri permission, otherwise we got errors when deleting for some reason
                    // might not be needed anymore
                    getContentResolver().takePersistableUriPermission(uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                }
        }
    }

    private void displayImage(Uri uri) {
        ImageView iv = findViewById(R.id.imageView2);
        iv.setImageURI(uri);
    }
}