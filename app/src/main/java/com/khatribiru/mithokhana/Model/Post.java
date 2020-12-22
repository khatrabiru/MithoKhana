package com.khatribiru.mithokhana.Model;

import java.util.HashMap;
import java.util.List;

public class Post {
    private String id;
    private String PostId;
    private User PostedBy;
    private String Status;
    private String Image;
    private String TotalLoves;
    private String TotalComments;
    private String CreatedDate;
    private HashMap<String, Comment> Comments;
    private HashMap<String, String> Loves;

    public Post() {
    }

    public Post(String id, String postId, User postedBy, String status, String image, String totalLoves, String totalComments, String createdDate, HashMap<String, Comment>  comments, HashMap<String, String> loves) {
        this.id = id;
        PostId = postId;
        PostedBy = postedBy;
        Status = status;
        Image = image;
        TotalLoves = totalLoves;
        TotalComments = totalComments;
        CreatedDate = createdDate;
        Comments = comments;
        Loves = loves;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public User getPostedBy() {
        return PostedBy;
    }

    public void setPostedBy(User postedBy) {
        PostedBy = postedBy;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTotalLoves() {
        return TotalLoves;
    }

    public void setTotalLoves(String totalLoves) {
        TotalLoves = totalLoves;
    }

    public String getTotalComments() {
        return TotalComments;
    }

    public void setTotalComments(String totalComments) {
        TotalComments = totalComments;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public HashMap<String, Comment>  getComments() {
        return Comments;
    }

    public void setComments(HashMap<String, Comment>  comments) {
        Comments = comments;
    }

    public HashMap<String, String> getLoves() {
        return Loves;
    }

    public void setLoves(HashMap<String, String> loves) {
        Loves = loves;
    }

    public String getFullName() {
        return this.PostedBy.getFullName();
    }

    public int getTotalCommentsInteger() {
        if( this.getTotalComments().isEmpty()) return 0;
        return Integer.parseInt( this.getTotalComments() );
    }
}
