package com.khatribiru.mithokhana.Model;

import java.io.Serializable;

public class Post implements Serializable {
    private String id;
    private String Status;
    private String Image;
    private String CreatedDate;
    private String PosterUserId;
    private String PosterName;
    private String PosterImageLink;

    public Post() {
    }

    public Post(String id, String status, String image, String createdDate, String posterUserId, String posterName, String posterImageLink) {
        this.id = id;
        Status = status;
        Image = image;
        CreatedDate = createdDate;
        PosterUserId = posterUserId;
        PosterName = posterName;
        PosterImageLink = posterImageLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getPosterUserId() {
        return PosterUserId;
    }

    public void setPosterUserId(String posterUserId) {
        PosterUserId = posterUserId;
    }

    public String getPosterName() {
        return PosterName;
    }

    public void setPosterName(String posterName) {
        PosterName = posterName;
    }

    public String getPosterImageLink() {
        return PosterImageLink;
    }

    public void setPosterImageLink(String posterImageLink) {
        PosterImageLink = posterImageLink;
    }
}
