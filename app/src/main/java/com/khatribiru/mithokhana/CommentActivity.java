package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference postList;
    FirebaseRecyclerAdapter <Comment, CommentViewHolder> adapter;

    CircleImageView imgProfile, imgProfile1;
    TextView name, date, status, post;
    ImageView image;
    EditText addComment;

    String postId = "";
    Post currentPost;
    int totalComments = 0;

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
        image = findViewById(R.id.image);
        post = findViewById(R.id.post);
        addComment = findViewById(R.id.addComment);
        imgProfile1 = findViewById(R.id.imgProfile1);

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

            } else {

                Toast.makeText(CommentActivity.this,"Please check internet", Toast.LENGTH_SHORT).show();
                return;

            }

        }

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postComment = addComment.getText().toString();
                if( postComment.isEmpty() ) {

                    Toast.makeText(CommentActivity.this, "Please enter a comment first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String id = String.valueOf(System.currentTimeMillis());
                Comment commentNew = new Comment( id, id, Common.currentUser.getId(), postComment, Common.currentUser.getFullName(), Common.currentUser.getImage() );

                try {

                    postList.child(postId).child("comments").child( id ).setValue( commentNew );
                    postList.child(postId).child("totalComments").setValue( String.valueOf( totalComments + 1 ) );
                    Toast.makeText(CommentActivity.this, "Comment posted", Toast.LENGTH_SHORT).show();
                    addComment.setText("");
                    loadPost();

                }catch (Exception e){
                    Toast.makeText(CommentActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadPost() {
        postList.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                currentPost = snapshot.getValue(Post.class);

                Picasso.with(getBaseContext()).load( currentPost.getPostedBy().getImage() )
                        .into(imgProfile);
                Picasso.with(getBaseContext()).load( currentPost.getPostedBy().getImage() )
                        .into(imgProfile1);
                Picasso.with(getBaseContext()).load( currentPost.getImage() )
                        .into(image);

                name.setText(currentPost.getFullName() );
                date.setText( currentPost.getCreatedDate() );
                status.setText( currentPost.getStatus() );
                totalComments = currentPost.getTotalCommentsInteger();
                loadComments();
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
                Picasso.with(getBaseContext()).load( comment.getCommenterImageLink() )
                        .into(commentViewHolder.imgProfile);
                commentViewHolder.name.setText( comment.getCommenterName() );
                commentViewHolder.comment.setText( comment.getComment() );
            }
        };
        recyclerView.setAdapter(adapter);
    }
}