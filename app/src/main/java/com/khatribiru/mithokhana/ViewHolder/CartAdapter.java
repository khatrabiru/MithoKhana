package com.khatribiru.mithokhana.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khatribiru.mithokhana.Interface.ItemClickListener;
import com.khatribiru.mithokhana.Model.Order;
import com.khatribiru.mithokhana.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView name, price, quantity, total, count;
    public ImageView image, minus, plus;

    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        quantity = itemView.findViewById(R.id.quantity);
        total = itemView.findViewById(R.id.total);
        count = itemView.findViewById(R.id.count);

        image = itemView.findViewById(R.id.image);
        plus = itemView.findViewById(R.id.plus);
        minus = itemView.findViewById(R.id.minus);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context context;


    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_order_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Order order = listData.get(position);

        Picasso.with(context).load( order.getMenuImage() )
                .into(holder.image);
        holder.name.setText( order.getMenuName() );
        holder.price.setText( order.getPrice() + " X " );
        holder.quantity.setText( order.getQuantity() );
        holder.total.setText( " = "+ order.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}