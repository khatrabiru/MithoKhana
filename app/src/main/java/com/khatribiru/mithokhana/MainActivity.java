package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Model.Menu;
import com.khatribiru.mithokhana.ViewHolder.NonVegMenuAdapter;
import com.khatribiru.mithokhana.ViewHolder.VegMenuAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FirebaseDatabase database;
    DatabaseReference menu;

    ViewPager nonVegViewPager, vegViewPager;
    VegMenuAdapter vegMenuAdapter;
    NonVegMenuAdapter nonVegMenuAdapter;


    List< Menu > menus = new ArrayList<>();
    List< String > vegMenuIds = new ArrayList<>();
    List< String > nonVegMenuIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigation);

        database = FirebaseDatabase.getInstance();
        menu = database.getReference("menu");

        // Weekly menu list
        for(int i = 1; i <= 7; i++ ) {
            vegMenuIds.add( Integer.toString(i) );
            nonVegMenuIds.add( Integer.toString(1) );
        }

        loadMenu();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        loadMenu();
                        return true;

                    case R.id.nav_orders:
                        // do work
                        return true;

                    case R.id.nav_social:
                        // do work
                        return true;

                    case R.id.nav_account:
                        // load profile
                        loadProfile();
                        return true;
                    default:
                        return false;
                }

            }
        });
    }

    private void loadProfile() {
        if(Common.isConnectedToInternet(getBaseContext())) {

            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);

        } else {

            Toast.makeText(MainActivity.this,"Please check internet", Toast.LENGTH_SHORT).show();
            return;

        }
    }

    private void loadMenu() {
        if(Common.isConnectedToInternet(getBaseContext())) {

            menu.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    menus = new ArrayList<>();

                    for(String menuId: vegMenuIds) {

                        Menu newMenu = new Menu(
                                snapshot.child(menuId).child("name").getValue().toString(),
                                snapshot.child(menuId).child("type").getValue().toString(),
                                snapshot.child(menuId).child("price").getValue().toString(),
                                snapshot.child(menuId).child("image").getValue().toString(),
                                snapshot.child(menuId).child("description").getValue().toString(),
                                snapshot.child(menuId).child("ratingSum").getValue().toString(),
                                snapshot.child(menuId).child("ratingCount").getValue().toString()
                        );
                        menus.add(newMenu);

                    }

                    vegMenuAdapter = new VegMenuAdapter(menus, MainActivity.this, vegMenuIds);
                    vegViewPager = findViewById(R.id.vegViewPager);
                    vegViewPager.setAdapter(vegMenuAdapter);
                    vegViewPager.setPadding(0, 0, 90, 0);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            menu.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    menus = new ArrayList<>();

                    for(String menuId: nonVegMenuIds) {

                        Menu newMenu = new Menu(
                                snapshot.child(menuId).child("name").getValue().toString(),
                                snapshot.child(menuId).child("type").getValue().toString(),
                                snapshot.child(menuId).child("price").getValue().toString(),
                                snapshot.child(menuId).child("image").getValue().toString(),
                                snapshot.child(menuId).child("description").getValue().toString(),
                                snapshot.child(menuId).child("ratingSum").getValue().toString(),
                                snapshot.child(menuId).child("ratingCount").getValue().toString()
                        );
                        menus.add(newMenu);

                    }

                    nonVegMenuAdapter = new NonVegMenuAdapter(menus, MainActivity.this, nonVegMenuIds);
                    nonVegViewPager = findViewById(R.id.nonVegViewPager);
                    nonVegViewPager.setAdapter(nonVegMenuAdapter);
                    nonVegViewPager.setPadding(0, 0, 90, 0);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {

            Toast.makeText(MainActivity.this,"Please check internet", Toast.LENGTH_SHORT).show();
            return;

        }
    }
}