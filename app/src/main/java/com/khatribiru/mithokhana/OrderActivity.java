package com.khatribiru.mithokhana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Model.Order;
import com.khatribiru.mithokhana.ViewHolder.OrderViewHolder;
import com.squareup.picasso.Picasso;

public class OrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference orders;
    FirebaseRecyclerAdapter<Order, OrderViewHolder > adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        database = FirebaseDatabase.getInstance();
        orders = database.getReference("orders");

        recyclerView = findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();
    }

    private void loadOrders() {

        adapter = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(Order.class,
                R.layout.order_item,
                OrderViewHolder.class,
                orders.child(Common.currentUser.getPhone()).orderByChild("date")) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Order order, int i) {

                Picasso.with(getBaseContext()).load(order.getMenuImage())
                        .into(orderViewHolder.image);
                orderViewHolder.name.setText( order.getMenuName() );
                orderViewHolder.price.setText( order.getPrice() + " X " );
                orderViewHolder.quantity.setText( order.getQuantity() + " = ");

                int sum = Integer.parseInt( order.getPrice() ) * Integer.parseInt( order.getQuantity() );
                orderViewHolder.total.setText( String.valueOf(sum) );

                orderViewHolder.status.setText( order.getStatus() );
                orderViewHolder.date.setText( order.getDate() );
            }
        };

        recyclerView.setAdapter(adapter);
    }

}