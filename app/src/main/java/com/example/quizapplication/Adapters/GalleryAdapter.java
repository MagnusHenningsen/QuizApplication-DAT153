package com.example.quizapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.quizapplication.Data.UriTypeConverter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapplication.Data.Option;
import com.example.quizapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/** @noinspection ComparatorCombinators*/
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<Option> choices; // List of drawable resource IDs
    private final Consumer<Option> func;

    private boolean Ascending = true;
    public GalleryAdapter(ArrayList<Option> imageResourceIds, Consumer<Option> func) {
        this.choices = new ArrayList<>(imageResourceIds);
        this.choices.sort((x, y) -> x.getName().compareTo(y.getName()));

        this.func = func;
    }

    public void updateList(List<Option> newList) {
        choices.clear(); // Clear current list
        choices.addAll(newList); // Add all from new list
        this.choices.sort((x, y) -> x.getName().compareTo(y.getName())); // Sort if needed
        notifyDataSetChanged(); // Notify the adapter to refresh
    }

    @SuppressLint("NotifyDataSetChanged")
    public void Sort() {
        Collections.reverse(choices); // the list should always be sorted one way or another, so we can just reverse
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Option choice = choices.get(position); // get the item in question
        holder.imageView.setImageURI(UriTypeConverter.toUri(choice.getUri())); // set the image
        holder.textView.setText(choice.getName()); // set the text
        // onclicklistener for the frame, meaning the entire "item"
        holder.frame.setOnClickListener(v -> {
            // create an alert incase of accidental click
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Are you sure you want to remove this choice?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                choices.remove(choice); // remove from list
                notifyDataSetChanged();
                this.func.accept(choice); // callback function to remove it from global list aswell
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss()); // do nothing if no
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return choices != null ? choices.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        FrameLayout frame;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            frame = itemView.findViewById(R.id.frame);
        }
    }
}
