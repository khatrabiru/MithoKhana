package com.khatribiru.mithokhana.Model;

public class Order {
    private String MenuImage;
    private String MenuName;
    private String Quantity;
    private String Price;

    public Order() {
    }

    public Order(String menuImage, String menuName, String quantity, String price) {
        MenuImage = menuImage;
        MenuName = menuName;
        Quantity = quantity;
        Price = price;
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

    public String getTotalPrice(){
        int price = (Integer.parseInt(this.getPrice())) * (Integer.parseInt(this.getQuantity()));
        return String.valueOf(price);
    }
}
