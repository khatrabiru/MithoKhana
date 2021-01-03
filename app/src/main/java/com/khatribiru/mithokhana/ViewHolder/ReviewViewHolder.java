package com.khatribiru.mithokhana.ViewHolder;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khatribiru.mithokhana.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    public TextView name, review;
    public RatingBar ratingBar;

    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        review = itemView.findViewById(R.id.review);
        ratingBar = itemView.findViewById(R.id.ratingBar);

    }
}


