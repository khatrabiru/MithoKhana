package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Model.Favourite;
import com.khatribiru.mithokhana.Model.Food;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    TextView food_name, food_price, food_description;
    ImageView food_image;

    CollapsingToolbarLayout collapsingToolbarLayout;
    RatingBar ratingBar;

    String foodId = "";
    FirebaseDatabase database;
    DatabaseReference foods;

    Food currentFood;
    FloatingActionButton myFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        food_description = findViewById(R.id.food_description);
        food_image = findViewById(R.id.food_image);
        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        ratingBar = findViewById(R.id.ratingBar);
        myFav = findViewById(R.id.btnMyFav);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("foods");

        if( getIntent() != null ) {
            foodId = getIntent().getStringExtra("foodId");
        }

        if( !foodId.isEmpty() && foodId != null ) {

            if(Common.isConnectedToInternet(getBaseContext())) {

                loadFoodDetail();

            } else {

                Toast.makeText(FoodDetail.this,"Please check internet", Toast.LENGTH_SHORT).show();
                return;

            }
        }

        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFavButton(true);
            }
        });
    }

    private void updateFavButton(boolean flag) {
        final DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favourites").child(Common.currentUser.getPhone()).child(foodId);
        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if( snapshot.exists() ) {

                    if(flag) {

                        favRef.removeValue();
                        myFav.setImageResource(R.drawable.love_before);

                    } else{
                        myFav.setImageResource(R.drawable.love);
                    }

                } else {
                    if(flag) {

                        Favourite fav = new Favourite( foodId, currentFood.getImage(), currentFood.getName(), currentFood.getPrice(), false );
                        favRef.setValue( fav );
                        myFav.setImageResource(R.drawable.love);

                    } else {

                        myFav.setImageResource(R.drawable.love_before);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadFoodDetail() {
        foods.child(foodId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int ratingSum = 0;
                if( snapshot.child("ratingSum").exists() ) ratingSum = Integer.parseInt(snapshot.child("ratingSum").getValue().toString());

                int ratingCount = 0;
                if( snapshot.child("ratingCount").exists() ) ratingCount = Integer.parseInt(snapshot.child("ratingCount").getValue().toString());
                float currentRating = 0;
                if( ratingCount > 0 ) currentRating = (float) ratingSum / (float) ratingCount;

                currentFood = snapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(" " + currentFood.getPrice()+ " Rs");
                food_name.setText(currentFood.getName());
                ratingBar.setRating(currentRating);
                food_description.setText(currentFood.getDescription());
                updateFavButton(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}