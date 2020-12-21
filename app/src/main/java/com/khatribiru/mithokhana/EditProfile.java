package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    TextView txtToolbar, logout;
    EditText firstName, lastName, phone, password;
    ImageView backArrow, editProfileImage;
    Button btnSave;
    CircleImageView profileImage;
    User user, changedUser;

    Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    private String imageUrl = "";
    StorageReference storageReference;
    private boolean isSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        logout = findViewById(R.id.logout);
        txtToolbar = findViewById(R.id.txtToolbar);

        profileImage = findViewById(R.id.profileImage);
        editProfileImage = findViewById(R.id.editProfileImage);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);

        backArrow = findViewById(R.id.backArrow);
        btnSave = findViewById(R.id.btnSave);


        user = Common.currentUser;

        if( !Common.currentUser.getImage().isEmpty() ) {

            Picasso.with(getBaseContext()).load( Common.currentUser.getImage() )
                    .into( profileImage );
        }
        logout.setVisibility(View.GONE);
        firstName.setText( user.getFirstName() );
        lastName.setText( user.getLastName() );
        phone.setText( user.getPhone() );
        password.setText(user.getPassword());
        txtToolbar.setText("Edit Profile");

        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( imageUrl.isEmpty() ) {
                    imageUrl = user.getImage();
                }
                changedUser = new User(
                        user.getId(),
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        phone.getText().toString(),
                        password.getText().toString(),
                        imageUrl,
                        user.getLocation(),
                        user.getFavouriteFoods(),
                        user.getFavouriteMenus()
                );

                if( !validateName() || !validatePhone() || !validatePassword() ) {
                    return;
                }

                if( doWeNeedToSave()  ) {

                    // If phone number changed
                    // Because phone number is primary key
                    if( !user.getPhone().equals(  changedUser.getPhone() ) ) {
                        // Haven't tested much, need to test: TODO
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Common.flagForSignUp = false;
                            }
                        }, 500);
                        Intent intent = new Intent(EditProfile.this, VerifyPhone.class);
                        startActivity(intent);

                    } else {
                        // Update firebase
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference currentUser = database.getReference("user");
                        currentUser.child(user.getPhone()).setValue( changedUser );
                        Toast.makeText(EditProfile.this, "Update profile!!", Toast.LENGTH_SHORT).show();
                        // set New Current User in Common
                        Common.currentUser = changedUser;
                        Intent intent = new Intent(getApplicationContext(), MyProfile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity( intent );
                    }

                } else {
                    Toast.makeText(EditProfile.this, "None of the field changed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean doWeNeedToSave() {

        if( !user.getPassword().equals( changedUser.getPassword() ) ) return true;

        if( !user.getFirstName().equals( changedUser.getFirstName() ) ) return true;
        if( !user.getLastName().equals( changedUser.getLastName() ) ) return true;
        if( !user.getPhone().equals( changedUser.getPhone() ) ) return true;
        if( !user.getImage().equals( changedUser.getImage() ) ) return true;

        return false;
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        if(saveUri != null) {
            // Lets upload a image
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading.......");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Uploaded!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    imageUrl = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(EditProfile.this, "::: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress  = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded " +  progress+ "%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            uploadImage();
        }
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

        if( pwd.length() < 5 ) {

            password.setError("Password should have at least 5 characters");
            return false;

        } else if( pwd.length() > 15 ) {

            password.setError("Password should have at max 15 characters");
            return false;

        } else {

            password.setError(null);
            return true;

        }
    }
}