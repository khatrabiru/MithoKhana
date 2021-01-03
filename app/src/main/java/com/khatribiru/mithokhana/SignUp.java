package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
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
import com.khatribiru.mithokhana.Model.Food;
import com.khatribiru.mithokhana.Model.Menu;
import com.khatribiru.mithokhana.Model.User;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    EditText firstName, lastName, phone, password, repeatPassword;
    Button btnSignUp;
    TextView signIn;

    DatabaseReference table_user;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signIn = findViewById(R.id.tvSignIn);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        repeatPassword = findViewById(R.id.repeatPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Let's Init Firebase
        database = FirebaseDatabase.getInstance();
        table_user= database.getReference("user");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( !validateName() || !validatePhone() || !validatePassword() ) {
                    return;
                }
                if(Common.isConnectedToInternet(getBaseContext())) {

                    ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please wait...");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if ( snapshot.child(phone.getText().toString()).exists() ) {
                                mDialog.dismiss();
                                phone.setError("Phone number already exists");
                                return;
                            } else {
                                mDialog.dismiss();

                                User newUser = new User(
                                        phone.getText().toString(),
                                        firstName.getText().toString(),
                                        lastName.getText().toString(),
                                        phone.getText().toString(),
                                        password.getText().toString(),
                                        "",
                                        null);
                                Common.currentUser = newUser;

                                // Verify Phone Number
                                Intent intent = new Intent(SignUp.this, VerifyPhone.class);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {

                    Toast.makeText(SignUp.this,"Please check internet", Toast.LENGTH_SHORT).show();
                    return;

                }
            }
        });
    }

    private boolean validateName() {

        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();
        if( fName.isEmpty() ) {

            firstName.setError("First name can't be empty");
            return false;

        } else if( lName.isEmpty() ) {

            lastName.setError("Last name can't be empty");
            return false;

        } else {

            firstName.setError(null);
            lastName.setError(null);
            return true;

        }
    }

    private boolean validatePhone() {
        String pNumber = phone.getText().toString();
        if( pNumber.isEmpty() ) {

            phone.setError("Phone number can't be empty");
            return false;

        } else if( pNumber.length() != 10 ) {

            phone.setError("Phone number should have exactly 10 digits");
            return false;

        } else {

            phone.setError(null);
            return true;

        }
    }

    private boolean validatePassword(){
        String pwd = password.getText().toString();
        String rPwd = repeatPassword.getText().toString();

        if( pwd.length() < 5 ) {

            password.setError("Password should have at least 5 characters");
            return false;

        } else if( pwd.length() > 15 ) {

            password.setError("Password should have at max 15 characters");
            return false;

        } if( rPwd.equals(pwd) ) {

            password.setError(null);
            repeatPassword.setError(null);
            return true;

        } else {

            password.setError(null);
            repeatPassword.setError("Password should be exactly the same");
            return false;

        }
    }

}