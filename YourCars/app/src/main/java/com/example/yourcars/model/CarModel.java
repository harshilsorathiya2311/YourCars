package com.example.yourcars.model;

public class CarModel {
    private int id;

    private byte[] image;

    String brandValue, title, price, date, description;
    public CarModel(int id, byte[] image, String brandValue, String title, String price, String date, String description) {
        this.id = id;
        this.image = image;
        this.brandValue = brandValue;
        this.title = title;
        this.price = price;
        this.date = date;
        this.description = description;
    }

    // getters...
    public int getId() { return id; }
    public byte[] getImage() { return image; }
    public String getBrandValue() { return brandValue; }
    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getDate() { return date; }
    public String getDescription() { return description; }

}

