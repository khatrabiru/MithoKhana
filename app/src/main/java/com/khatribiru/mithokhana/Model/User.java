package com.khatribiru.mithokhana.Model;


import java.util.List;

public class User {
    private String id;
    private String FirstName;
    private String LastName;
    private String Phone;
    private String Password;
    private String Image;
    private List< Double > Location;

    public User() {
    }

    public User(String id, String firstName, String lastName, String phone, String password, String image, List<Double> location) {
        this.id = id;
        FirstName = firstName;
        LastName = lastName;
        Phone = phone;
        Password = password;
        Image = image;
        Location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
