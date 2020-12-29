package com.khatribiru.mithokhana.Model;

public class Order {
    private String MenuId;
    private String MenuImage;
    private String MenuName;
    private String Quantity;
    private String Price;
    private String Status;
    private String Date;

    public Order() {
    }

    public Order(String menuId, String menuImage, String menuName, String quantity, String price, String status, String date) {
        MenuId = menuId;
        MenuImage = menuImage;
        MenuName = menuName;
        Quantity = quantity;
        Price = price;
        Status = status;
        Date = date;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menId) {
        MenuId = menId;
    }

    public String getMenuImage() {
        return MenuImage;
    }

    public void setMenuImage(String menuImage) {
        MenuImage = menuImage;
    }

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String menuName) {
        MenuName = menuName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTotalPrice(){
        int price = (Integer.parseInt(this.getPrice())) * (Integer.parseInt(this.getQuantity()));
        return String.valueOf(price);
    }
}
