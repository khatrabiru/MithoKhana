package com.khatribiru.mithokhana.Model;


import java.util.List;

public class Menu {

    private String id;
    private String Name;                 // Name of the Menu
    private String Type;                 // Veg or Non-veg
    private String Price;                // Price in NRS: sum of prices of all foods in the menu
    private String Image;                // Image link
    private String Description;
    private List< Food > Foods;          // List of Foods in this Menu

    public Menu() {
    }

    public Menu(String id, String name, String type, String price, String image, String description,  List<Food> foods) {
        this.id = id;
        Name = name;
        Type = type;
        Price = price;
        Description = description;
        Image = image;
        Foods = foods;
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

    public List<Food> getFoods() {
        return Foods;
    }

    public void setFoods(List<Food> foods) {
        Foods = foods;
    }

}
