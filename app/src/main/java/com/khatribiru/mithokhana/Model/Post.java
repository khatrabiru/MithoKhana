package com.khatribiru.mithokhana.Model;

import java.util.Date;
import java.util.List;

public class Post {
    private String PostId;
    private User PostedBy;
    private String Status;
    private String Image;
    private String TotalLoves;
    private String TotalComments;
    private Date CreatedDate;
    private List< Comment > Comments;

    public Post() {
    }

    public Post(String postId, User postedBy, String status, String image, String totalLoves, String totalComments, Date createdDate, List<Comment> comments) {
        PostId = postId;
        PostedBy = postedBy;
        Status = status;
        Image = image;
        TotalLoves = totalLoves;
        TotalComments = totalComments;
        CreatedDate = createdDate;
        Comments = comments;
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

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }

    public List<Comment> getComments() {
        return Comments;
    }

    public void setComments(List<Comment> comments) {
        Comments = comments;
    }

    public String getDateAndTime() {
        // Complete this later: TODO
        return "11 hours";
    }
    public String getFullName() {
        return this.PostedBy.getFullName();
    }
}
