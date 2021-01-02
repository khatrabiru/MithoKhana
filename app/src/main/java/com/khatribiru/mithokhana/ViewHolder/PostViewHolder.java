package com.khatribiru.mithokhana.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khatribiru.mithokhana.Common.Common;
import com.khatribiru.mithokhana.Interface.ItemClickListener;
import com.khatribiru.mithokhana.Model.Loves;
import com.khatribiru.mithokhana.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CircleImageView imgProfile;
    public TextView name, date, status, totalLoves, totalComments;
    public ImageView image, iconComment, iconLove;



    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        imgProfile = itemView.findViewById(R.id.imgProfile );
        name = itemView.findViewById(R.id.name);
        date = itemView.findViewById(R.id.date);
        status = itemView.findViewById(R.id.status);
        image = itemView.findViewById(R.id.image);

        totalLoves = itemView.findViewById(R.id.totalLoves);
        iconLove = itemView.findViewById(R.id.iconLove);

        totalComments = itemView.findViewById(R.id.totalComments);
        iconComment = itemView.findViewById(R.id.iconComment);


        totalComments.setOnClickListener(this);
        iconComment.setOnClickListener(this);
        iconLove.setOnClickListener(this);
    }

    public void loveCLicked(String postId, PostViewHolder postViewHolder) {
        // Let's Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_loves = database.getReference("loves");

        table_loves.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(postId).child( Common.currentUser.getPhone() ).exists() ) {

                    // already loved this post, lets unlike
                    table_loves.child(postId).child( Common.currentUser.getPhone() ).removeValue();

                } else {
                    // New love
                    String id = String.valueOf(System.currentTimeMillis());
                    Object loves = new Loves(id, Common.currentUser.getPhone());
                    table_loves.child(postId).child( Common.currentUser.getPhone() ).setValue( loves  );
                }
                updateLoveCount(postId, postViewHolder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateLoveCount(String postId, PostViewHolder postViewHolder) {

        // Let's Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_loves = database.getReference("loves").child(postId);

        table_loves.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child( Common.currentUser.getPhone() ).exists() ) {

                    postViewHolder.iconLove.setImageResource(R.drawable.love);

                } else {
                    postViewHolder.iconLove.setImageResource(R.drawable.love_before);
                }
                int count = (int)snapshot.getChildrenCount();
                postViewHolder.totalLoves.setText( String.valueOf(count));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });    }

    public void updateCommentCount(String postId, PostViewHolder postViewHolder) {

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

                    postViewHolder.totalComments.setVisibility(View.VISIBLE);
                    postViewHolder.totalComments.setText( "1 Comment");
                } else {

                    postViewHolder.totalComments.setText( String.valueOf(count) +  " Comments");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
