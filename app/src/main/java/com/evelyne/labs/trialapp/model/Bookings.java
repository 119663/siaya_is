package com.evelyne.labs.trialapp.model;

public class Bookings {
    String  description, cap,price, noplate, bookurl;
    public Bookings(){};

    public Bookings(String description,String cap,String price,String noplate,
                     String bookurl){
        this.description = description;
        this.cap = cap;
        this.noplate= noplate;
        this.price = price;
       // this.pickUpDate=pickUpDate;
        // this.pickUpLocation = pickUpLocation;
        this.bookurl=bookurl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setNoPlate(String noplate) {
        this.noplate = noplate;
    }

    //public void setPickUpLocation(String pickUpLocation) {
     //   this.pickUpLocation = pickUpLocation;
   // }

    public void setBookurl(String bookurl) {
        this.bookurl = bookurl;
    }


    public String getDescription() {
        return description;
    }

    public String getCap() {
        return cap;
    }

    public String getPrice(){return price;}

    public String getNoPlate() {
        return noplate;
    }

    public String getBookurl() {
        return bookurl;
    }

    // public String getPickUpLocation() {
    //    return pickUpLocation;
  //  }

}


