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
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.khatribiru.mithokhana.Database.Database;
import com.khatribiru.mithokhana.Model.Menu;
import com.khatribiru.mithokhana.Model.Order;
import com.khatribiru.mithokhana.ViewHolder.NonVegMenuAdapter;
import com.khatribiru.mithokhana.ViewHolder.VegMenuAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    Geocoder geocoder;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.navigation);
        appBar = (MaterialToolbar) findViewById(R.id.topAppBar);
        fab = findViewById(R.id.fab);

        database = FirebaseDatabase.getInstance();
        menu = database.getReference("menu");

        // Weekly menu list
        vegMenuIds.add( Integer.toString(123) );
        vegMenuIds.add( Integer.toString(321) );

        nonVegMenuIds.add( Integer.toString(321) );
        nonVegMenuIds.add( Integer.toString(123) );

        geocoder = new Geocoder(this, Locale.getDefault());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Order> cart = new Database(Home.this).getCarts();

                if( cart.isEmpty() ) {
                    Toast.makeText(Home.this, "Please add foods to cart first", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
                return;
            }
        });

        if( Common.currentUser != null && Common.currentUser.getLocation() != null ) {
            double lat = Common.currentUser.getLocation().get(0);
            double lng = Common.currentUser.getLocation().get(1);
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

                String fullAddress = "";
                String address = addresses.get(0).getAddressLine(0);
                fullAddress += address;

                String city = addresses.get(0).getLocality();
                if(city != null) fullAddress += ", " + city;

                String state = addresses.get(0).getAdminArea();
                if(state != null) fullAddress += ", " + state;

                String country = addresses.get(0).getCountryName();
                if(country != null) fullAddress += ", " + country;

                String postalCode = addresses.get(0).getPostalCode();
                if(postalCode != null) fullAddress += ", " + postalCode;

                String knownName = addresses.get(0).getFeatureName();
                if(knownName != null) fullAddress += ", " + knownName;

                appBar.setTitle(fullAddress);

            } catch (IOException e) {
                e.printStackTrace();
            }
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
                        loadOrders();
                        return true;

                    case R.id.nav_social:
                        // do work
                        loadSocial();
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

    private void loadOrders() {
        if(Common.isConnectedToInternet(getBaseContext())) {

            Intent intent = new Intent(Home.this, OrderActivity.class);
            startActivity(intent);

        } else {

            Toast.makeText(Home.this,"Please check internet", Toast.LENGTH_SHORT).show();
            return;

        }
    }

    private void loadSocial() {

        if(Common.isConnectedToInternet(getBaseContext())) {

            Intent intent = new Intent(Home.this, SocialMedia.class);
            startActivity(intent);

        } else {

            Toast.makeText(Home.this,"Please check internet", Toast.LENGTH_SHORT).show();
            return;

        }
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
                        Menu newMenu = snapshot.child(menuId).getValue(Menu.class);
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
                        Menu newMenu = snapshot.child(menuId).getValue(Menu.class);
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