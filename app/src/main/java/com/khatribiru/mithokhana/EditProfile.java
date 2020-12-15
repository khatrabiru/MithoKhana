package com.khatribiru.mithokhana;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    TextView txtToolbar, logout;
    MaterialEditText firstName, lastName, phone, password, repeatPassword;
    ImageView backArrow;
    Button btnSave;
    CircleImageView profileImage;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        logout = findViewById(R.id.logout);
        txtToolbar = findViewById(R.id.txtToolbar);

        profileImage = findViewById(R.id.profileImage);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        repeatPassword = findViewById(R.id.repeatPassword);

        backArrow = findViewById(R.id.backArrow);
        btnSave = findViewById(R.id.btnSave);


        user = Common.currentUser;

        // Also change picture
        logout.setVisibility(View.GONE);
        firstName.setText( user.getFirstName() );
        lastName.setText( user.getLastName() );
        phone.setText( user.getPhone() );
        password.setText(user.getPassword());
        repeatPassword.setText(user.getPassword());
        txtToolbar.setText("Edit Profilee");
    }
}