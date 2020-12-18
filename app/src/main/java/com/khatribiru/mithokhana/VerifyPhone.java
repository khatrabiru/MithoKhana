package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khatribiru.mithokhana.Common.Common;

import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {
    EditText verificationCode;
    Button verify;

    String verificationCodeBySystem;
    FirebaseAuth mAuth;
    DatabaseReference table_user;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        verificationCode = findViewById(R.id.verificationCode);
        verify = findViewById(R.id.btnVerify);

        mAuth = FirebaseAuth.getInstance();


        sendVerificationToUser();


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First verify then add to firebase

                String code = verificationCode.getText().toString();
                if( code == null || code.length() != 6 ) {
                    Toast.makeText(VerifyPhone.this, "Wrong code by User", Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyCode(code);
            }
        });
    }

    private void sendVerificationToUser() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder( mAuth )
                        .setPhoneNumber("+44" + Common.currentUser.getPhone())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)                   // Timeout and unit
                        .setActivity(this)                                          // Activity (for callback binding)
                        .setCallbacks(mCallbacks)                                   // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if( code != null ) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhone.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredential(phoneAuthCredential);
    }

    private void signInTheUserByCredential(PhoneAuthCredential phoneAuthCredential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(VerifyPhone.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            // Let's Init Firebase
                            database = FirebaseDatabase.getInstance();
                            table_user= database.getReference("user");

                            ProgressDialog mDialog = new ProgressDialog(VerifyPhone.this);
                            mDialog.setMessage("Please wait...");
                            mDialog.show();

                            table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    mDialog.dismiss();

                                    table_user.child(Common.currentUser.getPhone()).setValue(Common.currentUser);


                                    if( Common.flagForSignUp == true ) {
                                        Intent intent = new Intent(getApplicationContext(), Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(VerifyPhone.this, "Update profile!!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MyProfile.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity( intent );
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {

                            Toast.makeText(VerifyPhone.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}