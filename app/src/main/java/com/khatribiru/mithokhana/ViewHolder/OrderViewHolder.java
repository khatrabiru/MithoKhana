package com.khatribiru.mithokhana.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khatribiru.mithokhana.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public TextView name, price, quantity, total, status, date;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        quantity = itemView.findViewById(R.id.quantity);
        total = itemView.findViewById(R.id.total);
        status = itemView.findViewById(R.id.status);
        date = itemView.findViewById(R.id.date);
        image = itemView.findViewById(R.id.image);
    }
}
