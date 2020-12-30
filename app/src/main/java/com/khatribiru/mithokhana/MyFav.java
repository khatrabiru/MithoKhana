package com.khatribiru.mithokhana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Interface.ItemClickListener;
import com.khatribiru.mithokhana.Model.Favourite;
import com.khatribiru.mithokhana.ViewHolder.FavouriteViewHolder;
import com.squareup.picasso.Picasso;

public class MyFav extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference myFavList;

    FirebaseRecyclerAdapter<Favourite, FavouriteViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fav);

        // Firebase
        database = FirebaseDatabase.getInstance();
        myFavList = database.getReference("favourites").child(Common.currentUser.getPhone());

        recyclerView = findViewById(R.id.listFav);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadMyFav();
    }

    private void loadMyFav() {

        adapter = new FirebaseRecyclerAdapter<Favourite, FavouriteViewHolder>(Favourite.class,
                R.layout.my_fav_item,
                FavouriteViewHolder.class,
                myFavList) {
            @Override
            protected void populateViewHolder(FavouriteViewHolder favouriteViewHolder, Favourite favourite, int i) {
                Picasso.with(getBaseContext()).load( favourite.getImage() )
                        .into(favouriteViewHolder.image);
                favouriteViewHolder.price.setText("Price: " + favourite.getPrice() + " Rs.");
                favouriteViewHolder.name.setText(favourite.getName());

                favouriteViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Favourite fav = adapter.getItem(position);

                        if( favourite.isMenu() ) {

                            Intent menuDetail = new Intent(MyFav.this, MenuDetail.class);
                            menuDetail.putExtra("menuId", fav.getId());
                            startActivity(menuDetail);

                        }
                        else {

                            Intent foodDetail = new Intent(MyFav.this, FoodDetail.class);
                            foodDetail.putExtra("foodId", fav.getId());
                            startActivity(foodDetail);

                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

    }
}