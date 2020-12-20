package com.khatribiru.mithokhana.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khatribiru.mithokhana.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    public TextView name, comment;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name );
        comment = itemView.findViewById(R.id.comment);
    }
}