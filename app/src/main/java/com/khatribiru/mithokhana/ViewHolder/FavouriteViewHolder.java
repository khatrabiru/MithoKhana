package com.khatribiru.mithokhana.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khatribiru.mithokhana.Interface.ItemClickListener;
import com.khatribiru.mithokhana.R;

public class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView image;
    public TextView name, price;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FavouriteViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.image);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
