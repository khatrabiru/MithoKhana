package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Model.Menu;
import com.khatribiru.mithokhana.ViewHolder.NonVegMenuAdapter;
import com.khatribiru.mithokhana.ViewHolder.VegMenuAdapter;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FirebaseDatabase database;
    DatabaseReference menu;

    ViewPager nonVegViewPager, vegViewPager;
    VegMenuAdapter vegMenuAdapter;
    NonVegMenuAdapter nonVegMenuAdapter;


    List<Menu> menus = new ArrayList<>();
    List< String > vegMenuIds = new ArrayList<>();
    List< String > nonVegMenuIds = new ArrayList<>();

    MaterialToolbar appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.navigation);
        appBar = (MaterialToolbar) findViewById(R.id.topAppBar);

        database = FirebaseDatabase.getInstance();
        menu = database.getReference("menu");

        // Weekly menu list
        for(int i = 1; i <= 7; i++ ) {
            vegMenuIds.add( Integer.toString(i) );
            nonVegMenuIds.add( Integer.toString(1) );
        }

        appBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission( Home.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)  {
                    Intent intent  = new Intent(Home.this, ChangeMyLocation.class);
                    startActivity(intent);
                    finish();
                } else {

                    // Ask for Permission
                    Dexter.withContext( Home.this )
                            .withPermission( Manifest.permission.ACCESS_FINE_LOCATION )
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                    Intent intent  = new Intent(Home.this, ChangeMyLocation.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                    if (permissionDeniedResponse.isPermanentlyDenied()) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                                        builder.setTitle("Permission Denied")
                                                .setMessage("Permission to access device locations is permanently denied. You need to go to settings to allow the permission")
                                                .setNegativeButton("Cancel", null)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent();
                                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                        intent.setData(Uri.fromParts( "package", getPackageName(), null ) );
                                                    }
                                                })
                                                .show();
                                    } else {

                                        Toast.makeText(Home.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                }
                            }).check();

                }
            }
        });

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

            Intent intent = new Intent(Home.this, Profile.class);
            startActivity(intent);

        } else {

            Toast.makeText(Home.this,"Please check internet", Toast.LENGTH_SHORT).show();
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

                    vegMenuAdapter = new VegMenuAdapter(menus, Home.this, vegMenuIds);
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

                    nonVegMenuAdapter = new NonVegMenuAdapter(menus, Home.this, nonVegMenuIds);
                    nonVegViewPager = findViewById(R.id.nonVegViewPager);
                    nonVegViewPager.setAdapter(nonVegMenuAdapter);
                    nonVegViewPager.setPadding(0, 0, 90, 0);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {

            Toast.makeText(Home.this,"Please check internet", Toast.LENGTH_SHORT).show();
            return;

        }

    }
}