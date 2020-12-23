package com.khatribiru.mithokhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcelable;
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
import com.khatribiru.mithokhana.ViewHolder.PostViewHolder;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference commentList;

    FirebaseRecyclerAdapter <Comment, CommentViewHolder> adapter;

    CircleImageView imgProfile, imgProfile1;
    TextView name, date, status, postText;
    ImageView image;
    EditText addComment;

    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Firebase
        database = FirebaseDatabase.getInstance();
        commentList = database.getReference("comments");

        imgProfile = findViewById(R.id.imgProfile );
        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        status = findViewById(R.id.status);
        image = findViewById(R.id.image);
        postText = findViewById(R.id.post);
        addComment = findViewById(R.id.addComment);
        imgProfile1 = findViewById(R.id.imgProfile1);

        recyclerView = findViewById(R.id.recyclerComment);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if( getIntent() != null ) {
            post = (Post) getIntent().getSerializableExtra("post");
        }

        if( !post.getId().isEmpty() && post.getId() != null ) {

            if(Common.isConnectedToInternet(getBaseContext())) {
                loadPost();

            } else {

                Toast.makeText(CommentActivity.this,"Please check internet", Toast.LENGTH_SHORT).show();
                return;

            }
        }

        postText.setOnClickListener(new View.OnClickListener() {
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

                    commentList.child(post.getId()).child( id ).setValue( commentNew );
                    Toast.makeText(CommentActivity.this, "Comment posted", Toast.LENGTH_SHORT).show();
                    addComment.setText("");

                }catch (Exception e){
                    Toast.makeText(CommentActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadPost() {

        Picasso.with(getBaseContext()).load( post.getPosterImageLink() )
                .into(imgProfile);
        Picasso.with(getBaseContext()).load( Common.currentUser.getImage() )
                .into(imgProfile1);
        Picasso.with(getBaseContext()).load( post.getImage() )
                .into(image);

        name.setText( post.getPosterName() );
        date.setText( post.getCreatedDate() );
        status.setText( post.getStatus() );
        loadComments();
    }

    private void loadComments() {

            adapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(Comment.class,
                R.layout.comment_item,
                CommentViewHolder.class,
                commentList.child(post.getId())) {
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