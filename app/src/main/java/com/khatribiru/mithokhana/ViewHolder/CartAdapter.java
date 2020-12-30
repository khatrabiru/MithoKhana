package com.khatribiru.mithokhana.ViewHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.khatribiru.mithokhana.Cart;
import com.khatribiru.mithokhana.Database.Database;
import com.khatribiru.mithokhana.Interface.ItemClickListener;
import com.khatribiru.mithokhana.MenuDetail;
import com.khatribiru.mithokhana.Model.Order;
import com.khatribiru.mithokhana.OrderActivity;
import com.khatribiru.mithokhana.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView name, price, quantity, total, count;
    public ImageView image, minus, plus;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

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

        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
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

    public void showAlertDialog(Order order){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Do you want to delete?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete item from the cart
                new Database(context).removeItemFromCart( order );
                Intent cart = new Intent(context, Cart.class);
                context.startActivity(cart);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Order order = listData.get(position);

        Picasso.with(context).load( order.getMenuImage() )
                .into(holder.image);
        holder.name.setText( order.getMenuName() );
        holder.price.setText( order.getPrice() + " X " );
        holder.quantity.setText( order.getQuantity() );
        holder.count.setText( order.getQuantity() );
        holder.total.setText( " = "+ order.getTotalPrice());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                Order order1 = listData.get(position);

                if( view.getId() == R.id.plus ) {

                    int cnt = Integer.parseInt(order1.getQuantity() );

                    if( cnt < 9 ) {
                        new Database(context).updateItemFromCart( order1.getMenuId(), String.valueOf(cnt+1) );
                        Intent cart = new Intent(context, Cart.class);
                        context.startActivity(cart);
                    }

                } else if( view.getId() == R.id.minus ) {

                    int cnt = Integer.parseInt(order1.getQuantity() );

                    if( cnt > 1) {

                        new Database(context).updateItemFromCart( order1.getMenuId(), String.valueOf(cnt-1) );
                        Intent cart = new Intent(context, Cart.class);
                        context.startActivity(cart);

                    } else if( cnt == 1 ) {
                        showAlertDialog(order1);
                    }

                } else {

                    Intent menuDetail = new Intent(context, MenuDetail.class);
                    menuDetail.putExtra("menuId", order1.getMenuId());
                    context.startActivity(menuDetail);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}