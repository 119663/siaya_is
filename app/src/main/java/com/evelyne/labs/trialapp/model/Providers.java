package com.evelyne.labs.trialapp.model;

import com.google.firebase.database.Exclude;

public class Providers {

    public String name,email,company, phone;
    public Providers(){};

    public Providers(String textfullnamesp, String textemailsp, String textcompanysp, String textphonenumbersp) {
        this.name = textfullnamesp;
        this.email=textemailsp;
        this.company=textcompanysp;
        this.phone=textphonenumbersp;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
