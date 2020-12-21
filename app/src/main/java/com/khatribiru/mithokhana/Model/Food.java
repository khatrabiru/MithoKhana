package com.khatribiru.mithokhana.Model;

import java.util.List;

public class Food {
    private String id;
    private String Name;                 // Name of the Food
    private String Type;                 // Veg or Non-veg
    private String Price;                // Price in NRS
    private String Image;                // Image link
    private String Description;          // Description of Food
    private String RatingSum;            // Sum of ratings
    private String RatingCount;          // Number of individual reviews
    private List< Review > Reviews;
    private List< User > Favourites;

    public Food() {
    }


    public Food(String id, String name, String type, String price, String image, String description, String ratingSum, String ratingCount, List<Review> reviews, List<User> favourites) {
        this.id = id;
        Name = name;
        Type = type;
        Price = price;
        Image = image;
        Description = description;
        RatingSum = ratingSum;
        RatingCount = ratingCount;
        Reviews = reviews;
        Favourites = favourites;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getRatingSum() {
        return RatingSum;
    }

    public void setRatingSum(String ratingSum) {
        RatingSum = ratingSum;
    }

    public String getRatingCount() {
        return RatingCount;
    }

    public void setRatingCount(String ratingCount) {
        RatingCount = ratingCount;
    }

    public List<Review> getReviews() {
        return Reviews;
    }

    public void setReviews(List<Review> reviews) {
        Reviews = reviews;
    }

    public List<User> getFavourites() {
        return Favourites;
    }

    public void setFavourites(List<User> favourites) {
        Favourites = favourites;
    }

    public float getRating() {
        if( this.getRatingCount() == null || Integer.parseInt(this.getRatingCount()) == 0 ) {
            return  0;
        }
        return Float.parseFloat(this.getRatingSum()) / Float.parseFloat( this.getRatingCount() );
    }
}
