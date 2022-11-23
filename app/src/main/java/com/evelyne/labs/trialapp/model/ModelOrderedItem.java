package com.evelyne.labs.trialapp.model;


public class ModelOrderedItem {

    public  String key,Bookingdate,Bookingtime,Location,TankCapacity,
            timestamp;

    public ModelOrderedItem(String key, String bookingdate, String bookingtime, String location, String tankCapacity, String timestamp) {
        this.key = key;
        Bookingdate = bookingdate;
        Bookingtime = bookingtime;
        Location = location;
        TankCapacity = tankCapacity;
        this.timestamp = timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBookingdate() {
        return Bookingdate;
    }

    public void setBookingdate(String bookingdate) {
        Bookingdate = bookingdate;
    }

    public String getBookingtime() {
        return Bookingtime;
    }

    public void setBookingtime(String bookingtime) {
        Bookingtime = bookingtime;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTankCapacity() {
        return TankCapacity;
    }

    public void setTankCapacity(String tankCapacity) {
        TankCapacity = tankCapacity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
