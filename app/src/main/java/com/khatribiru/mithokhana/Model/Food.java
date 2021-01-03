package com.khatribiru.mithokhana.Model;

import java.util.List;

public class Food {
    private String id;
    private String Name;                 // Name of the Food
    private String Type;                 // Veg or Non-veg
    private String Price;                // Price in NRS
    private String Image;                // Image link
    private String Description;          // Description of Food

    public Food() {
    }


    public Food(String id, String name, String type, String price, String image, String description) {
        this.id = id;
        Name = name;
        Type = type;
        Price = price;
        Image = image;
        Description = description;
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

}
