package com.example.quizapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.quizapplication.Adapters.GalleryAdapter;
import com.example.quizapplication.ApplicationContext;
import com.example.quizapplication.DataTypes.Option;
import com.example.quizapplication.R;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Gallery extends AppCompatActivity {

    private ArrayList<Option> choices = new ArrayList<Option>();
    private ApplicationContext appCon;
    private GalleryAdapter adapter;
    private Consumer<Option> removeItem = new Consumer<Option>() {
        @Override
        public void accept(Option c) {
            // callback method we sent into the adapter, will remove what was removed in the adapter in our global list aswell
            appCon.removeFromList(c);
            choices = appCon.getList();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        appCon = (ApplicationContext) getApplicationContext();
        // collect list
        choices = appCon.getList();
        // set the view
        RecyclerView recyclerView = findViewById(R.id.imagesRecyclerView);
        // span of 3 columns
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        // pass in the list and a function to remove items
        adapter = new GalleryAdapter(choices, removeItem);
        Button sortBtn = this.findViewById(R.id.sortBtn);
        sortBtn.setOnClickListener(v -> {
            adapter.Sort();
            adapter.notifyDataSetChanged();
        });
        // attach adapter to rec. view
        recyclerView.setAdapter(adapter);
        Button addBtn = this.findViewById(R.id.buttonAdd);
        // start activity to collect new choice
        // code 101 is just a random number, lets us control the callback of the activity
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
            // if the activity was a success we update the recylcerview
            if (requestCode == 101 && resultCode == RESULT_OK) {
                RecyclerView recyclerView = findViewById(R.id.imagesRecyclerView);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns
                this.choices = appCon.getList();
                adapter = new GalleryAdapter(choices, removeItem);

                recyclerView.setAdapter(adapter); // update images
            }


        }
    }

}