package com.khatribiru.mithokhana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.khatribiru.mithokhana.Common.Common;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    TextView myProfile, txtToolbar, logout, profileName, myFav;
    ImageView backArrow;
    CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myProfile = findViewById(R.id.myProfile);
        myFav = findViewById(R.id.myFav);

        profileName = findViewById(R.id.profileName);
        profileName.setText(Common.currentUser.getFirstName() + " " + Common.currentUser.getLastName());

        backArrow = findViewById(R.id.backArrow);

        logout = findViewById(R.id.logout);
        logout.setVisibility(View.GONE);

        txtToolbar = findViewById(R.id.txtToolbar);
        txtToolbar.setText("Account");

        profileImage = findViewById(R.id.profileImage);

        if( !Common.currentUser.getImage().isEmpty() ) {

            Picasso.with(getBaseContext()).load( Common.currentUser.getImage() )
                    .into( profileImage );
        }

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MyProfile.class);
                startActivity(intent);
            }
        });

        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MyFav.class);
                startActivity(intent);
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}