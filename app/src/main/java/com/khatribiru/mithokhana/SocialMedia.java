package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khatribiru.mithokhana.Common.Common;


import com.khatribiru.mithokhana.Interface.ItemClickListener;
import com.khatribiru.mithokhana.Model.Comment;
import com.khatribiru.mithokhana.Model.Loves;
import com.khatribiru.mithokhana.Model.Post;
import com.khatribiru.mithokhana.Model.User;
import com.khatribiru.mithokhana.ViewHolder.PostViewHolder;
import com.squareup.picasso.Picasso;


public class SocialMedia extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference postList;

    FirebaseRecyclerAdapter <Post, PostViewHolder > adapterPost;
    PostViewHolder pstViewHolder;
    String ref = "";

    FloatingActionButton fab;

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

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialMedia.this, PostActivity.class);
                startActivity(intent);
            }
        });


        if(Common.isConnectedToInternet(getBaseContext())) {
            loadListPost();

        } else {

            Toast.makeText(SocialMedia.this,"Please check internet", Toast.LENGTH_SHORT).show();
            return;

        }
    }

    private void loadListPost() {

        adapterPost = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class,
                R.layout.post_item,
                PostViewHolder.class,
                postList.orderByChild("id")) {
            @Override
            protected void populateViewHolder(PostViewHolder postViewHolder, Post post, int i) {

                Picasso.with(getBaseContext()).load( post.getPosterImageLink() )
                        .into(postViewHolder.imgProfile);
                Picasso.with(getBaseContext()).load( post.getImage() )
                        .into(postViewHolder.image);

                postViewHolder.name.setText(post.getPosterName() );
                postViewHolder.date.setText( post.getTimeDifference() );
                postViewHolder.status.setText( post.getStatus() );

                postViewHolder.updateLoveCount(adapterPost.getRef(i).getKey(), postViewHolder);
                postViewHolder.updateCommentCount(adapterPost.getRef(i).getKey(), postViewHolder);

                postViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        String postId = adapterPost.getRef(position).getKey();

                        if( view.getId() == R.id.iconLove ) {

                            postViewHolder.loveCLicked(postId, postViewHolder);

                        } else {
                            pstViewHolder = postViewHolder;
                            ref = postId;
                            Intent intent = new Intent(SocialMedia.this, CommentActivity.class);
                            intent.putExtra("post", post);
                            onPause();
                            startActivity(intent);
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(adapterPost);

    }

    private void updateCommentCount(String postId, PostViewHolder postViewHolder) {

        // Let's Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_comments = database.getReference("comments").child(postId);

        table_comments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int count = (int)snapshot.getChildrenCount();


                if(count == 0) {
                    postViewHolder.totalComments.setVisibility(View.GONE);
                } else if( count == 1 ) {

                    postViewHolder.totalComments.setText( "1 Comment");
                } else {

                    postViewHolder.totalComments.setText( String.valueOf(count) +  " Comments");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if( !ref.isEmpty() ){
            pstViewHolder.updateCommentCount(ref, pstViewHolder);
        }
    }
}