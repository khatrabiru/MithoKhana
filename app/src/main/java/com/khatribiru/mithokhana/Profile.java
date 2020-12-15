package com.khatribiru.mithokhana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.khatribiru.mithokhana.Common.Common;

public class Profile extends AppCompatActivity {

    TextView myProfile, txtToolbar, logout, profileName;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myProfile = findViewById(R.id.myProfile);

        profileName = findViewById(R.id.profileName);
        profileName.setText(Common.currentUser.getFirstName() + " " + Common.currentUser.getLastName());

        backArrow = findViewById(R.id.backArrow);
        backArrow.setVisibility(View.GONE);

        logout = findViewById(R.id.logout);
        logout.setVisibility(View.GONE);

        txtToolbar = findViewById(R.id.txtToolbar);
        txtToolbar.setText("Account");

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MyProfile.class);
                startActivity(intent);
            }
        });
    }
}