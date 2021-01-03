package com.khatribiru.mithokhana.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khatribiru.mithokhana.MainActivity;
import com.khatribiru.mithokhana.MenuDetail;
import com.khatribiru.mithokhana.Model.Menu;
import com.khatribiru.mithokhana.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VegMenuAdapter extends PagerAdapter {


    private final List< Menu > menus;
    private final Context context;
    private final List< String > menuIds;


    public VegMenuAdapter(List<Menu> menus, Context context,List< String > menuIds ) {
        this.menus = menus;
        this.context = context;
        this.menuIds = menuIds;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.menu_item, container, false);


        TextView name, price;
        RatingBar ratingBar;
        ImageView image;

        name = itemView.findViewById(R.id.name);
        name.setText(menus.get(position).getName());
        name.setTextSize(22);


        price = itemView.findViewById(R.id.price);
        price.setText(menus.get(position).getPrice() + " Rs.");
        price.setTextSize(12);

        ratingBar = itemView.findViewById(R.id.ratingBar);
        updateRating(ratingBar, menuIds.get(position));

        image = itemView.findViewById(R.id.image);
        Picasso.with(context).load(menus.get(position).getImage()).into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuDetail.class);
                intent.putExtra("menuId", menuIds.get(position));
                context.startActivity(intent);
            }
        });

        container.addView(itemView, 0);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (View) object );
    }

    private void updateRating(RatingBar ratingBar, String menuId) {

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

}
