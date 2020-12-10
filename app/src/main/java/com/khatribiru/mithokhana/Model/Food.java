package com.khatribiru.mithokhana.Model;

import java.util.List;

public class Food {
    private String Name;                 // Name of the Food
    private String Type;                 // Veg or Non-veg
    private String Price;                // Price in NRS
    private String Image;                // Image link
    private String Description;          // Description of Food
    private String RatingSum;            // Sum of ratings
    private String RatingCount;          // Number of individual reviews
    private List< Review > Reviews;      // All of the Individual Reviews
    private List< String > Favourites;   // List of individual phone numbers who marked Favourite

    public Food() {
    }

    public Food(String name, String type, String price, String image, String description, String ratingSum, String ratingCount, List<Review> reviews, List<String> favourites) {
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
}
