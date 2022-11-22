package com.evelyne.labs.trialapp.model;

public class User {
    public String nameC, phoneC, emailC;

    public User() {
    }

    public String getNameC() {
        return nameC;
    }

    public void setNameC(String nameC) {
        this.nameC = nameC;
    }

    public String getPhoneC() {
        return phoneC;
    }

    public void setPhoneC(String phoneC) {
        this.phoneC = phoneC;
    }

    public String getEmailC() {
        return emailC;
    }

    public void setEmailC(String emailC) {
        this.emailC = emailC;
    }

    public User(String textfullnamec, String textemailc, String textphonenumberc) {
        this.nameC = textfullnamec;
        this.emailC = textemailc;
        this.phoneC = textphonenumberc;
        //this.location=locaTion;
    }
}


