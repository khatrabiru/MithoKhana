package com.khatribiru.mithokhana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class VerifyPhone extends AppCompatActivity {
    EditText verificationCode;
    Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        verificationCode = findViewById(R.id.verificationCode);
        verify = findViewById(R.id.btnVerify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First verify then add to firebase
                Intent intent = new Intent(VerifyPhone.this, Home.class);
                startActivity(intent);
            }
        });
    }
}