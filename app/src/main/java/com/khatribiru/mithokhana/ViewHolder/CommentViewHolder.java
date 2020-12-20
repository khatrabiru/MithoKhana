package com.khatribiru.mithokhana.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khatribiru.mithokhana.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    public TextView name, comment;
    public CircleImageView imgProfile;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        imgProfile = itemView.findViewById(R.id.imgProfile );
        name = itemView.findViewById(R.id.name );
        comment = itemView.findViewById(R.id.comment);
    }
}