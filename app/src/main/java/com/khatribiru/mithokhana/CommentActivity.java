package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Model.Comment;
import com.khatribiru.mithokhana.Model.Post;
import com.khatribiru.mithokhana.ViewHolder.CommentViewHolder;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference postList;
    FirebaseRecyclerAdapter <Comment, CommentViewHolder> adapter;

    CircleImageView imgProfile;
    TextView name, date, status, totalLoves, totalComments;
    ImageView image;

    String postId = "";
    Post currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Firebase
        database = FirebaseDatabase.getInstance();
        postList = database.getReference("posts");

        imgProfile = findViewById(R.id.imgProfile );
        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        status = findViewById(R.id.status);
        totalLoves = findViewById(R.id.totalLoves);
        totalComments = findViewById(R.id.totalComments);
        image = findViewById(R.id.image);

        recyclerView = findViewById(R.id.recyclerComment);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if( getIntent() != null ) {
            postId = getIntent().getStringExtra("postId");
        }

        if( !postId.isEmpty() && postId != null ) {

            if(Common.isConnectedToInternet(getBaseContext())) {
                loadPost();
                loadComments();

            } else {

                Toast.makeText(CommentActivity.this,"Please check internet", Toast.LENGTH_SHORT).show();
                return;

            }

        }

    }

    private void loadPost() {
        postList.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                currentPost = snapshot.getValue(Post.class);

                Picasso.with(getBaseContext()).load( currentPost.getPostedBy().getImage() )
                        .into(imgProfile);
                Picasso.with(getBaseContext()).load( currentPost.getImage() )
                        .into(image);

                name.setText(currentPost.getFullName() );
                date.setText( currentPost.getDateAndTime() );
                status.setText( currentPost.getStatus() );
                totalLoves.setText( currentPost.getTotalLoves() );
                totalComments.setText( currentPost.getTotalComments() + " Comments" );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadComments() {
        adapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(Comment.class,
                R.layout.comment_item,
                CommentViewHolder.class,
                postList.child(postId).child("comments")) {
            @Override
            protected void populateViewHolder(CommentViewHolder commentViewHolder, Comment comment, int i) {
                Picasso.with(getBaseContext()).load( comment.getCommentedBy().getImage() )
                        .into(commentViewHolder.imgProfile);
                commentViewHolder.name.setText( comment.getCommentedBy().getFullName() );
                commentViewHolder.comment.setText( comment.getComment() );
            }
        };
        recyclerView.setAdapter(adapter);
    }
}