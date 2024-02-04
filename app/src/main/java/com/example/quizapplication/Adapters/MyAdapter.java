package com.example.quizapplication.Adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapplication.DataTypes.Choice;
import com.example.quizapplication.R;

import java.util.ArrayList;
import java.util.List;
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Choice> choices; // List of drawable resource IDs

    public MyAdapter(ArrayList<Choice> imageResourceIds) {
        this.choices = imageResourceIds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Choice choice = choices.get(position);
        holder.imageView.setImageURI(choice.getUri());
        holder.textView.setText(choice.getName());
        holder.frame.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Are you sure you want to remove this choice?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                choices.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, choices.size());
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
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
