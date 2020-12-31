package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Model.Post;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.UUID;

public class PostActivity extends AppCompatActivity {

    ImageView close, image;
    MaterialEditText status;
    TextView post;

    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        close = findViewById(R.id.close);
        image = findViewById(R.id.image);
        status = findViewById(R.id.status); 
        post = findViewById(R.id.post);

        chooseImage();


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // add post to firebase
                uploadImage();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
                            Toast.makeText(PostActivity.this, "Uploaded!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String imageUrl = uri.toString();
                                    long curTime = System.currentTimeMillis();
                                    String id = String.valueOf( curTime );
                                    // save to firebase

                                    Post post = new Post(curTime * -1,
                                            status.getText().toString(),
                                            imageUrl,
                                            id,
                                            Common.currentUser.getId(),
                                            Common.currentUser.getFullName(),
                                            Common.currentUser.getImage());

                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    final DatabaseReference table_post = database.getReference("posts");

                                    try {

                                        table_post.child(id).setValue(post);
                                        Toast.makeText(PostActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PostActivity.this, SocialMedia.class);
                                        startActivity(intent);
                                        finish();

                                    }catch (Exception e){
                                        Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(PostActivity.this, "::: "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            image.setImageURI(saveUri);
        }
    }
}