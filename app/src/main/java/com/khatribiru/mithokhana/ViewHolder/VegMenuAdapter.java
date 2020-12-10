package com.khatribiru.mithokhana.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.khatribiru.mithokhana.Model.Menu;
import com.khatribiru.mithokhana.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VegMenuAdapter extends PagerAdapter {

    private List< Menu > menus;
    private LayoutInflater layoutInflater;
    private Context context;

    public VegMenuAdapter(List<Menu> menus, Context context) {
        this.menus = menus;
        this.context = context;
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
        layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.menu_item, container, false);


        TextView name, price;
        RatingBar ratingBar;
        ImageView image;

        name = itemView.findViewById(R.id.name);
        name.setText(menus.get(position).getName());
        name.setTextSize(22);


        price = itemView.findViewById(R.id.price);
        price.setText("NRS: " + menus.get(position).getPrice());
        price.setTextSize(12);

        ratingBar = itemView.findViewById(R.id.ratingBar);
        ratingBar.setRating( menus.get(position).getRating() );

        image = itemView.findViewById(R.id.image);
        Picasso.with(context).load(menus.get(position).getImage()).into(image);

        container.addView(itemView, 0);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (View) object );
    }
}
