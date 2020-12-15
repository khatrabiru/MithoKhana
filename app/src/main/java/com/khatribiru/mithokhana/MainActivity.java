package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Model.User;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {

    TextView signUp, forgotPassword;
    EditText phone, password;
    Button btnSignIn;
    CheckBox chkBoxRemember;

    DatabaseReference table_user;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        Paper.book().destroy();

        // Check if user details remembered
        String user = Paper.book().read(Common.USER_KEY);
        String pass = Paper.book().read(Common.PWD_KEY);

        if( user != null && pass != null ) {
            login(user, pass);
        }

        signUp = findViewById(R.id.tvSignUp);
        forgotPassword = findViewById(R.id.txtForgotPassword);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.btnSignIn);
        chkBoxRemember = findViewById(R.id.chkBoxRemember);

        // Let's Init Firebase
        database = FirebaseDatabase.getInstance();
        table_user= database.getReference("user");


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        signUp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( Common.isConnectedToInternet(getBaseContext()) ) {

                    ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
                    mDialog.setMessage("Please wait...");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if ( snapshot.child(phone.getText().toString()).exists() ) {

                                // Save user
                                if( chkBoxRemember.isChecked() ) {
                                    Paper.book().write(Common.USER_KEY, phone.getText().toString());
                                    Paper.book().write(Common.PWD_KEY, password.getText().toString());
                                }

                                mDialog.dismiss();
                                // Get user Information
                                User user = snapshot.child(phone.getText().toString()).getValue(User.class);
                                user.setPhone(phone.getText().toString());

                                if ( user.getPassword().equals( password.getText().toString() ) ) {

                                    Intent homeIntent = new Intent(MainActivity.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();

                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Sign In Failed, Check Password", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(MainActivity.this, "User doesn't exists, Check phone number", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }  else {

                    Toast.makeText(MainActivity.this,"Please check internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public void login(String phone, String password) {
        // Let's Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(phone).exists()) {

                    // Get user Information
                    User user = snapshot.child(phone).getValue(User.class);
                    user.setPhone(phone);

                    if (user.getPassword().equals(password)) {

                        Intent homeIntent = new Intent(MainActivity.this, Home.class);
                        Common.currentUser = user;
                        startActivity(homeIntent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}