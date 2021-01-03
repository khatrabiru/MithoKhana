package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Model.Favourite;
import com.khatribiru.mithokhana.Model.Food;
import com.khatribiru.mithokhana.Model.Review;
import com.khatribiru.mithokhana.ViewHolder.ReviewViewHolder;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener {

    TextView food_name, food_price, food_description;
    ImageView food_image;

    CollapsingToolbarLayout collapsingToolbarLayout;
    RatingBar ratingBar;

    String foodId = "";
    FirebaseDatabase database;
    DatabaseReference foods;

    Food currentFood;
    FloatingActionButton myFav, btnRating;
    RecyclerView recyclerReview;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference reviews;
    FirebaseRecyclerAdapter<Review, ReviewViewHolder> adapter;

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
        btnRating = findViewById(R.id.btnRating);
        myFav = findViewById(R.id.btnMyFav);

        recyclerReview = findViewById(R.id.recyclerReview);
        recyclerReview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerReview.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("foods");

        if( getIntent() != null ) {
            foodId = getIntent().getStringExtra("foodId");
        }

        if( !foodId.isEmpty()  ) {

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

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement rating dialog
                showRatingDialog();
            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very bad", "Not good", "Quiet ok", "Very good", "Excellent"))
                .setDefaultRating(0)
                .setTitle("Rate this food")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Write your review here")
                .setDefaultRating(5)
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetail.this)
                .show();
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference table_reviews = db.getReference("reviews").child(foodId);

        // Get rating and update firebase
        table_reviews.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                int ratingSum = 0;
                if( snapshot.child("ratingSum").exists() ) ratingSum = Integer.parseInt(snapshot.child("ratingSum").getValue().toString());

                int ratingCount = 0;
                if( snapshot.child("ratingCount").exists() ) ratingCount = Integer.parseInt(snapshot.child("ratingCount").getValue().toString());


                if(snapshot.child("reviews").child(Common.currentUser.getPhone()).exists()) {

                    Review currentReview= snapshot.child("reviews").child(Common.currentUser.getPhone()).getValue(Review.class);
                    ratingSum -= Integer.parseInt(currentReview.getStar());

                } else {

                    ratingCount++;
                }

                ratingSum += i;

                Review currentReview = new Review( Common.currentUser.getPhone(), Common.currentUser.getFullName(), String.valueOf(i), s);
                table_reviews.child("ratingSum").setValue( String.valueOf(ratingSum) );
                table_reviews.child("ratingCount").setValue( String.valueOf(ratingCount) );
                table_reviews.child("reviews").child(Common.currentUser.getPhone()).setValue(currentReview);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onNeutralButtonClicked() {

    }

    private void updateRating() {

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference table_reviews = db.getReference("reviews").child(foodId);
        // Get rating and update firebase
        table_reviews.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                int ratingSum = 0;
                if( snapshot.child("ratingSum").exists() ) ratingSum = Integer.parseInt(snapshot.child("ratingSum").getValue().toString());

                int ratingCount = 0;
                if( snapshot.child("ratingCount").exists() ) ratingCount = Integer.parseInt(snapshot.child("ratingCount").getValue().toString());
                float currentRating = 0;
                if( ratingCount > 0 ) currentRating = (float) ratingSum / (float) ratingCount;
                ratingBar.setRating(currentRating);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

                currentFood = snapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(food_image);
                food_price.setText(" " + currentFood.getPrice()+ " Rs");
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());
                updateFavButton(false);
                updateRating();
                loadReviews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadReviews() {
        reviews = database.getReference("reviews").child(foodId).child("reviews");
        // Get rating and update firebase

        adapter = new FirebaseRecyclerAdapter<Review, ReviewViewHolder>(Review.class,
                R.layout.review_item,
                ReviewViewHolder.class,
                reviews){
            @Override
            protected void populateViewHolder(ReviewViewHolder reviewViewHolder, Review review, int i) {
                reviewViewHolder.name.setText(review.getName());
                reviewViewHolder.review.setText(review.getComment());
                reviewViewHolder.ratingBar.setRating( Integer.parseInt(review.getStar()) );
            }
        };
        recyclerReview.setAdapter(adapter);
    }
}