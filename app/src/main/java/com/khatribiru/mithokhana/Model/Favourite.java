package com.khatribiru.mithokhana.Model;

public class Favourite {

    private String Id;
    private String Image;
    private String Name;
    private String Price;
    private boolean IsMenu;

    public Favourite() {
    }

    public Favourite(String id, String image, String name, String price, boolean isMenu) {
        Id = id;
        Image = image;
        Name = name;
        Price = price;
        IsMenu = isMenu;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public boolean isMenu() {
        return IsMenu;
    }

    public void setMenu(boolean menu) {
        IsMenu = menu;
    }
}
