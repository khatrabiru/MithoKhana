package com.khatribiru.mithokhana.Model;

import java.util.List;

public class User {
    private String FirstName;
    private String LastName;
    private String Phone;
    private String Password;
    private String Image;
    private List<Double> Location;
    private List< Food > FavouriteFoods;
    private List< Menu > FavouriteMenus;

    public User() {
    }

    public User(String firstName, String lastName, String phone, String password, String image, List<Double> location, List<Food> favouriteFoods, List<Menu> favouriteMenus) {
        FirstName = firstName;
        LastName = lastName;
        Phone = phone;
        Password = password;
        Image = image;
        Location = location;
        FavouriteFoods = favouriteFoods;
        FavouriteMenus = favouriteMenus;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public List<Double> getLocation() {
        return Location;
    }

    public void setLocation(List<Double> location) {
        Location = location;
    }

    public List<Food> getFavouriteFoods() {
        return FavouriteFoods;
    }

    public void setFavouriteFoods(List<Food> favouriteFoods) {
        FavouriteFoods = favouriteFoods;
    }

    public List<Menu> getFavouriteMenus() {
        return FavouriteMenus;
    }

    public void setFavouriteMenus(List<Menu> favouriteMenus) {
        FavouriteMenus = favouriteMenus;
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
