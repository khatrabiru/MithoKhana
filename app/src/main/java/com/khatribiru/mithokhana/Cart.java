package com.khatribiru.mithokhana;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esewa.android.sdk.payment.ESewaPayment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Database.Database;
import com.khatribiru.mithokhana.Model.Order;
import com.khatribiru.mithokhana.ViewHolder.CartAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView txtTotalPrice;
    FButton btnPlace;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    int total = 0;
    private static final int REQUEST_CODE_PAYMENT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);


        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( total == 0 ) {

                    Toast.makeText(Cart.this, "Please add foods to the cart first", Toast.LENGTH_SHORT).show();

                } else {

                    Intent intent = new Intent(Cart.this, EsewaPayment.class);
                    intent.putExtra("amount", String.valueOf(total));
                    startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                }
                finish();
            }
        });
        loadListFood();
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);

        // calculate total price
        total = 0;
        for(Order order:cart) {
            total += Integer.parseInt(order.getQuantity()) * Integer.parseInt(order.getPrice());
        }

        if(total == 0) {

            Intent intent = new Intent(Cart.this, Home.class);
            startActivity(intent);
            finish();
        }

        txtTotalPrice.setText("Total: " + total + " Rs.");
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(Cart.this, Home.class);
        startActivity(intent);
    }
}