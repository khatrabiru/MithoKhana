package com.khatribiru.mithokhana.Model;

import java.io.Serializable;

public class Post implements Serializable {
    private long id;
    private String Status;
    private String Image;
    private String CreatedDate;
    private String PosterUserId;
    private String PosterName;
    private String PosterImageLink;

    public Post() {
    }

    public Post(long id, String status, String image, String createdDate, String posterUserId, String posterName, String posterImageLink) {
        this.id = id;
        Status = status;
        Image = image;
        CreatedDate = createdDate;
        PosterUserId = posterUserId;
        PosterName = posterName;
        PosterImageLink = posterImageLink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String friendlyTimeDiff(long timeDifferenceMilliseconds) {
        long diffSeconds = timeDifferenceMilliseconds / 1000;
        long diffMinutes = timeDifferenceMilliseconds / (60 * 1000);
        long diffHours = timeDifferenceMilliseconds / (60 * 60 * 1000);
        long diffDays = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24);
        long diffWeeks = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24 * 7);
        long diffMonths = (long) (timeDifferenceMilliseconds / (60 * 60 * 1000 * 24 * 30.41666666));
        long diffYears = timeDifferenceMilliseconds / ((long)60 * 60 * 1000 * 24 * 365);

        if (diffMinutes < 1) {
            return diffSeconds + " seconds";
        } else if (diffHours < 1) {
            return diffMinutes + " min";
        } else if (diffDays < 1) {
            return diffHours + " hrs";
        } else if (diffWeeks < 1) {
            return diffDays + " days";
        } else if (diffMonths < 1) {
            return diffWeeks + " weeks";
        } else if (diffYears < 1) {
            return diffMonths + " months";
        } else {
            return diffYears + " years";
        }
    }

    public String getTimeDifference() {

        if( this.getCreatedDate() == null || this.getCreatedDate().isEmpty() ){
            return "11 hrs";
        }

        long diff = System.currentTimeMillis();
        diff -= Long.parseLong(this.getCreatedDate());
        return this.friendlyTimeDiff(diff);
    }
}
