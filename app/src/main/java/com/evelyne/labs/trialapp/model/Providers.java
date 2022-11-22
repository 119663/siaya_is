package com.evelyne.labs.trialapp.model;

public class Providers {

    public String name,email,company, phone;
    public Providers(){};

    public Providers(String textfullnamesp, String textemailsp, String textcompanysp, String textphonenumbersp) {
        this.name = textfullnamesp;
        this.email=textemailsp;
        this.company=textcompanysp;
        this.phone=textphonenumbersp;
    }
}
