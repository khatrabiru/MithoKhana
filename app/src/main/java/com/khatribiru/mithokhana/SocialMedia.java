package com.khatribiru.mithokhana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Model.Comment;
import com.khatribiru.mithokhana.Model.Post;
import com.khatribiru.mithokhana.ViewHolder.CommentViewHolder;
import com.khatribiru.mithokhana.ViewHolder.PostViewHolder;
import com.squareup.picasso.Picasso;


public class SocialMedia extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference postList;
    FirebaseRecyclerAdapter <Post, PostViewHolder > adapter;
    FirebaseRecyclerAdapter <Comment, CommentViewHolder> commentAdapter;

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

                postViewHolder.name.setText(post.getFullName() );
                postViewHolder.date.setText( post.getDateAndTime() );
                postViewHolder.status.setText( post.getStatus() );
                postViewHolder.totalLoves.setText( post.getTotalLoves() );
                postViewHolder.totalComments.setText( post.getTotalComments() + " Comments" );

                postViewHolder.totalComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start new activity
                        Intent foodDetail = new Intent(SocialMedia.this, CommentActivity.class);
                        foodDetail.putExtra("postId", adapter.getRef(i).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}