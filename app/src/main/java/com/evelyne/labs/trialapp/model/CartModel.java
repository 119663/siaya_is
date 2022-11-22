package com.evelyne.labs.trialapp.model;

public class CartModel {
    private String key,uid,image,companyname,price,plate,date,time,capacity;
    private int customercapacity;
    private float totalrice;

    public CartModel(){}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setCustomercapacity(int customercapacity) {
        this.customercapacity = customercapacity;
    }

    public int getCustomercapacity() {
        return customercapacity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getTotalrice() {
        return totalrice;
    }

    public void setTotalrice(float totalrice) {
        this.totalrice = totalrice;
    }
}
