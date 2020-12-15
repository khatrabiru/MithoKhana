package com.khatribiru.mithokhana.Model;

import android.location.Location;

import java.util.List;

public class User {
    private String FirstName;
    private String LastName;
    private String Phone;
    private String Password;
    private String Image;
    private Location Location;
    private List< Food > FavouriteFoods;
    private List< Menu > FavouriteMenus;

    public User() {
    }

    public User(String firstName, String lastName, String phone, String password) {
        FirstName = firstName;
        LastName = lastName;
        Phone = phone;
        Password = password;
        this.Image = "";
        this.Location = null;
        this.FavouriteFoods = null;
        this.FavouriteMenus = null;
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

    public android.location.Location getLocation() {
        return Location;
    }

    public void setLocation(android.location.Location location) {
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
}
