package com.khatribiru.mithokhana.Model;

public class Review {
    private String Phone;       // Phone number of Reviewer
    private String Name;        // Name of the Reviewer
    private String Star;        // Star given by Reviewer out of 5
    private String Comment;

    public Review() {
    }

    public Review(String phone,  String name, String star, String comment) {
        Phone = phone;
        Star = star;
        Name = name;
        Comment = comment;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getStar() {
        return Star;
    }

    public void setStar(String star) {
        Star = star;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
