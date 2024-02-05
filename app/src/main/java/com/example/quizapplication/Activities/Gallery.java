package com.example.quizapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.quizapplication.Adapters.MyAdapter;
import com.example.quizapplication.ApplicationContext;
import com.example.quizapplication.DataTypes.Choice;
import com.example.quizapplication.R;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class Gallery extends AppCompatActivity {

    private ArrayList<Choice> choices = new ArrayList<Choice>();
    private ApplicationContext appCon;
    private MyAdapter adapter;
    private Consumer<Choice> removeItem = new Consumer<Choice>() {
        @Override
        public void accept(Choice c) {
            /*
            choices.remove(c);
            updateChoices();
             */
            appCon.removeFromList(c);
            choices = appCon.getList();

            // adapter.notifyDataSetChanged();
            // adapter.notifyItemRemoved(c.getIndex());
        }
    };
    /*
    private void updateChoices() {
        Intent returnIntent = new Intent();
        returnIntent.putParcelableArrayListExtra("UpdatedChoices", choices);
        setResult(RESULT_OK, returnIntent);
    }
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        appCon = (ApplicationContext) getApplicationContext();
        choices = appCon.getList();
        RecyclerView recyclerView = findViewById(R.id.imagesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns

        adapter = new MyAdapter(choices, removeItem);
        Button sortBtn = this.findViewById(R.id.sortBtn);
        sortBtn.setOnClickListener(v -> {
            adapter.notifyDataSetChanged();
            adapter.Sort();
        });
        recyclerView.setAdapter(adapter);
        Button addBtn = this.findViewById(R.id.buttonAdd);
        addBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, newImage.class);
            startActivityForResult(intent, 101);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 101 && resultCode == RESULT_OK) {
                // Create the new choice object
                /*
                String resultString = data.getStringExtra("name");
                Uri resultUri = data.getParcelableExtra("uri");

                // Persistable permission
                if (resultUri != null) {
                    getContentResolver().takePersistableUriPermission(resultUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                Choice choice = new Choice(resultUri, resultString);

                choices.add(choice);
                updateChoices();
                 */
               // adapter.notifyDataSetChanged();
            }
            RecyclerView recyclerView = findViewById(R.id.imagesRecyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns
            this.choices = appCon.getList();
            adapter = new MyAdapter(choices, removeItem);

            recyclerView.setAdapter(adapter); // update images

        }
    }

}