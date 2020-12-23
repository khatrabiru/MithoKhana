package com.khatribiru.mithokhana.Model;

public class Comment {
    private String id;
    private String CommentDate;
    private String CommenterUserId;
    private String Comment;
    private String CommenterName;
    private String CommenterImageLink;

    public Comment() {
    }

    public Comment(String id, String commentDate, String commenterUserId, String comment, String commenterName, String commenterImageLink) {
        this.id = id;
        CommentDate = commentDate;
        CommenterUserId = commenterUserId;
        Comment = comment;
        CommenterName = commenterName;
        CommenterImageLink = commenterImageLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentDate() {
        return CommentDate;
    }

    public void setCommentDate(String commentDate) {
        CommentDate = commentDate;
    }

    public String getCommenterUserId() {
        return CommenterUserId;
    }

    public void setCommenterUserId(String commenterUserId) {
        CommenterUserId = commenterUserId;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getCommenterName() {
        return CommenterName;
    }

    public void setCommenterName(String commenterName) {
        CommenterName = commenterName;
    }

    public String getCommenterImageLink() {
        return CommenterImageLink;
    }

    public void setCommenterImageLink(String commenterImageLink) {
        CommenterImageLink = commenterImageLink;
    }
}
