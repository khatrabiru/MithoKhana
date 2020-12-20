package com.khatribiru.mithokhana.Model;

import java.util.Date;

public class Comment {
    private String CommentId;
    private Date CommentDate;
    private User CommentedBy;
    private String Comment;

    public Comment() {
    }

    public Comment(String commentId, Date commentDate, User commentedBy, String comment) {
        CommentId = commentId;
        CommentDate = commentDate;
        CommentedBy = commentedBy;
        Comment = comment;
    }

    public String getCommentId() {
        return CommentId;
    }

    public void setCommentId(String commentId) {
        CommentId = commentId;
    }

    public Date getCommentDate() {
        return CommentDate;
    }

    public void setCommentDate(Date commentDate) {
        CommentDate = commentDate;
    }

    public User getCommentedBy() {
        return CommentedBy;
    }

    public void setCommentedBy(User commentedBy) {
        CommentedBy = commentedBy;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
