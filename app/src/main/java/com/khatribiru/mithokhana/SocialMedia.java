package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khatribiru.mithokhana.Common.Common;


import com.khatribiru.mithokhana.Interface.ItemClickListener;
import com.khatribiru.mithokhana.Model.Post;
import com.khatribiru.mithokhana.Model.User;
import com.khatribiru.mithokhana.ViewHolder.PostViewHolder;
import com.squareup.picasso.Picasso;


public class SocialMedia extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference postList;
    FirebaseRecyclerAdapter <Post, PostViewHolder > adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        // Firebase
        database = FirebaseDatabase.getInstance();
        postList = database.getReference("posts");

        recyclerView = findViewById(R.id.recyclerPost);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(Common.isConnectedToInternet(getBaseContext())) {
            loadListPost();

        } else {

            Toast.makeText(SocialMedia.this,"Please check internet", Toast.LENGTH_SHORT).show();
            return;

        }
    }

    private void loadListPost() {

        adapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class,
                R.layout.post_item,
                PostViewHolder.class,
                postList) {
            @Override
            protected void populateViewHolder(PostViewHolder postViewHolder, Post post, int i) {

                Picasso.with(getBaseContext()).load( post.getPostedBy().getImage() )
                        .into(postViewHolder.imgProfile);
                Picasso.with(getBaseContext()).load( post.getImage() )
                        .into(postViewHolder.image);

                postViewHolder.name.setText(post.getPostedBy().getFullName() );
                postViewHolder.date.setText( post.getCreatedDate() );
                postViewHolder.status.setText( post.getStatus() );
                postViewHolder.totalLoves.setText( post.getTotalLoves() );
                postViewHolder.totalComments.setText( post.getTotalComments() + " Comments" );

                changeLoveIcon(adapter.getRef(i).getKey(), postViewHolder);

                postViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        String postId = adapter.getRef(position).getKey();

                        if( view.getId() == R.id.iconLove ) {

                            try {

                                loveClicked(postId, postViewHolder);
                                recreate();

                            }catch (Exception e){

                                Toast.makeText(SocialMedia.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            return;
                        }

                        Intent intent = new Intent(SocialMedia.this, CommentActivity.class);
                        intent.putExtra("postId", postId);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void changeLoveIcon(String postId, PostViewHolder postViewHolder) {

        // Let's Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_posts = database.getReference("posts");

        table_posts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postId).child("loves").child( Common.currentUser.getPhone() ).exists() ) {

                    //  loved this post
                    postViewHolder.iconLove.setImageResource(R.drawable.love);

                } else {
                    postViewHolder.iconLove.setImageResource(R.drawable.love_before);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return;
    }

    private void loveClicked(String postId, PostViewHolder postViewHolder) {

        // Let's Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_posts = database.getReference("posts");

        table_posts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postId).child("loves").child( Common.currentUser.getPhone() ).exists() ) {

                    // already loved this post, lets unlike
                    postViewHolder.iconLove.setImageResource(R.drawable.love_before);
                    table_posts.child(postId).child("loves").child( Common.currentUser.getPhone() ).removeValue();
                    String oldLovesCount = snapshot.child(postId).child("totalLoves").getValue().toString() ;
                    String newLovesCount = String.valueOf( Integer.parseInt( oldLovesCount ) - 1 );
                    table_posts.child(postId).child("totalLoves").setValue( newLovesCount );

                } else {
                    // New love
                    table_posts.child(postId).child("loves").child( Common.currentUser.getPhone() ).setValue( Common.currentUser.getPhone()  );
                    String oldLovesCount = "0";

                    if( snapshot.child(postId).child("totalLoves").exists() ) {
                        oldLovesCount =  snapshot.child(postId).child("totalLoves").getValue().toString();
                        if(oldLovesCount.isEmpty()) oldLovesCount = "0";
                    }

                    String newLovesCount = String.valueOf( Integer.parseInt( oldLovesCount ) + 1 );
                    table_posts.child(postId).child("totalLoves").setValue( newLovesCount );
                    postViewHolder.iconLove.setImageResource(R.drawable.love);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return;
    }
}