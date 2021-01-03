package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Database.Database;
import com.khatribiru.mithokhana.Interface.ItemClickListener;
import com.khatribiru.mithokhana.Model.Favourite;
import com.khatribiru.mithokhana.Model.Food;
import com.khatribiru.mithokhana.Model.Menu;
import com.khatribiru.mithokhana.Model.Order;
import com.khatribiru.mithokhana.Model.Review;
import com.khatribiru.mithokhana.ViewHolder.FoodViewHolder;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class MenuDetail extends AppCompatActivity implements RatingDialogListener {

    TextView menu_description, menu_price, count, menu_name;
    ImageView menu_image, plus, minus;
    RatingBar ratingBar;
    Menu currentMenu;

    RecyclerView recycler_menu;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String menuId = "";

    FirebaseDatabase database;
    DatabaseReference menus;
    FirebaseRecyclerAdapter< Food, FoodViewHolder > adapter;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton btnAddToCart, btnRating, myFav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        database = FirebaseDatabase.getInstance();
        menus = database.getReference("menu");

        menu_image = findViewById(R.id.menu_image);
        menu_description = findViewById(R.id.menu_description);
        menu_price = findViewById(R.id.menu_price);
        menu_name = findViewById(R.id.menu_name);

        ratingBar = findViewById(R.id.ratingBar);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnRating = findViewById(R.id.btnRating);
        myFav = findViewById(R.id.myFav);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        count = findViewById(R.id.count);


        recycler_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        collapsingToolbarLayout = findViewById(R.id.collapsing);

        if( getIntent() != null ) {
            menuId = getIntent().getStringExtra("menuId");
        }

        if( !menuId.isEmpty() && menuId != null ) {

            if(Common.isConnectedToInternet(getBaseContext())) {

                loadMenuDetail();
                loadFoods();

            } else {

                Toast.makeText(MenuDetail.this,"Please check internet", Toast.LENGTH_SHORT).show();
                return;

            }
        }

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCount = Integer.parseInt( count.getText().toString() );
                currentCount += 1;
                // One Id can have maximum 9 orders at a time.
                if( currentCount > 9) currentCount = 9;
                count.setText(String.valueOf( currentCount ));
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCount = Integer.parseInt( count.getText().toString() );
                currentCount -= 1;
                if( currentCount < 1 ) currentCount = 1;
                count.setText(String.valueOf( currentCount ));
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String today = df.format(new Date());

                Order order = new Order(
                        currentMenu.getId(),
                        currentMenu.getImage(),
                        currentMenu.getName(),
                        count.getText().toString(),
                        currentMenu.getPrice(),
                        "Placed",
                        today
                );

                new Database(getBaseContext()).addToCart(order);
                Toast.makeText(MenuDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                return;
            }
        });

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
                .create(MenuDetail.this)
                .show();
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference table_reviews = db.getReference("reviews").child(menuId);

        // Get rating and update firebase
        table_reviews.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                int ratingSum = 0;
                if( snapshot.child("ratingSum").exists() ) ratingSum = Integer.parseInt(snapshot.child("ratingSum").getValue().toString());

                int ratingCount = 0;
                if( snapshot.child("ratingCount").exists() ) ratingCount = Integer.parseInt(snapshot.child("ratingCount").getValue().toString());


                if(snapshot.child(Common.currentUser.getPhone()).exists()) {

                    Review currentReview= snapshot.child(Common.currentUser.getPhone()).getValue(Review.class);
                    ratingSum -= Integer.parseInt(currentReview.getStar());

                } else {

                    ratingCount++;
                }

                ratingSum += i;

                Review currentReview = new Review( Common.currentUser.getPhone(), Common.currentUser.getFullName(), String.valueOf(i), s);
                table_reviews.child("ratingSum").setValue( String.valueOf(ratingSum) );
                table_reviews.child("ratingCount").setValue( String.valueOf(ratingCount) );
                table_reviews.child(Common.currentUser.getPhone()).setValue(currentReview);
                finish();
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
        final DatabaseReference table_reviews = db.getReference("reviews").child(menuId);
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
        final DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favourites").child(Common.currentUser.getPhone()).child(menuId);
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

                        Favourite fav = new Favourite( menuId, currentMenu.getImage(), currentMenu.getName(), currentMenu.getPrice(), true );
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

    private void loadFoods() {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                menus.child(menuId).child("foods")
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.name.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage())
                        .into(foodViewHolder.image);
                foodViewHolder.price.setText(food.getPrice() +" Rs.");
                foodViewHolder.description.setText(food.getDescription());

                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // Start new activity
                        Food newFood = adapter.getItem(position);
                        Intent foodDetail = new Intent(MenuDetail.this, FoodDetail.class);
                        foodDetail.putExtra("foodId", newFood.getId());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }
    private void loadMenuDetail() {
        menus.child(menuId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ratingSum = 0;
                if( snapshot.child("ratingSum").exists() ) ratingSum = Integer.parseInt(snapshot.child("ratingSum").getValue().toString());

                int ratingCount = 0;
                if( snapshot.child("ratingCount").exists() ) ratingCount = Integer.parseInt(snapshot.child("ratingCount").getValue().toString());
                float currentRating = 0;
                if( ratingCount > 0 ) currentRating = (float) ratingSum / (float) ratingCount;

                currentMenu = snapshot.getValue(Menu.class);

                Picasso.with(getBaseContext()).load(currentMenu.getImage())
                        .into(menu_image);
                collapsingToolbarLayout.setTitle(currentMenu.getName());
                menu_price.setText(currentMenu.getPrice() + " Rs.");
                menu_name.setText(currentMenu.getName());
                ratingBar.setRating(currentRating);
                menu_description.setText(currentMenu.getDescription());
                updateFavButton(false);
                updateRating();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}