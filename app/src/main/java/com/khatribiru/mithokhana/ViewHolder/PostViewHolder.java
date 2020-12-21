package com.khatribiru.mithokhana.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khatribiru.mithokhana.Interface.ItemClickListener;
import com.khatribiru.mithokhana.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CircleImageView imgProfile;
    public TextView name, date, status, totalLoves, totalComments;
    public ImageView image, iconComment;


    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        imgProfile = itemView.findViewById(R.id.imgProfile );
        name = itemView.findViewById(R.id.name);
        date = itemView.findViewById(R.id.date);
        status = itemView.findViewById(R.id.status);
        totalLoves = itemView.findViewById(R.id.totalLoves);
        totalComments = itemView.findViewById(R.id.totalComments);
        image = itemView.findViewById(R.id.image);
        iconComment = itemView.findViewById(R.id.iconComment);

        totalComments.setOnClickListener(this);
        iconComment.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
