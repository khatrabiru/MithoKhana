package com.khatribiru.mithokhana;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {

    TextView txtToolbar, logout, profileName, firstName, lastName, phone;
    ImageView backArrow;
    CircleImageView profileImage;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        profileName = findViewById(R.id.profileName);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phone = findViewById(R.id.phone);
        profileImage = findViewById(R.id.profileImage);
        backArrow = findViewById(R.id.backArrow);
        logout = findViewById(R.id.logout);
        txtToolbar = findViewById(R.id.txtToolbar);

        user = Common.currentUser;
        profileName.setText( user.getFirstName() + " " + user.getLastName() );
        firstName.setText( user.getFirstName() );
        lastName.setText( user.getLastName() );
        phone.setText( user.getPhone() );

        txtToolbar.setText("My Profile");
        // Set Image: todo


    }
}