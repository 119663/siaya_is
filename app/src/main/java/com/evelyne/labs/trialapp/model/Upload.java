package com.evelyne.labs.trialapp.model;

import java.security.Key;

public class Upload {

    public  String key,CompanyName,TankCapacity,NumberPlate,ServicePrice, imageUri,uploadId,
            mtimestamp, muid;


   public Upload(){}


    public Upload(String key, String mcompany, String mcapacity, String mplate, String mprice, String mimageUrl, String uploadId, String mtimestamp, String muid) {
        this.key = key;
        this.CompanyName = mcompany;
        this.TankCapacity = mcapacity;
        this.NumberPlate = mplate;
        this.ServicePrice = mprice;
        this.imageUri = mimageUrl;
        this.uploadId = uploadId;
        this.mtimestamp = mtimestamp;
        this.muid = muid;
    }

//    public Upload(String company, String capacity, String plate, String price, String imageUrl,
//                  String timestamp, String uid, String uploadId ){
////        if(company.trim().equals("")){
////            company = "no name";
////        }
////        if(capacity.trim().equals("")){
////            capacity = "no name";
////        }
////        if(plate.trim().equals("")){
////            plate = "no name";
////        }
////        if(price.trim().equals("")){
////            price = "no name";
////        }
////        if(timestamp.trim().equals("")){
////            timestamp = "no timestamp";
////        }
////        if(uid.trim().equals("")){
////            uid = "no uid";
////        }
//
////        mcompany = company;
////        mcapacity = capacity;
////        mplate = plate;
////        mprice = price;
////        mimageUrl = imageUrl;
////        mtimestamp = timestamp;
////        muid = uid;
////        this.uploadId=uploadId;
//
//
//    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMcompany() {
        return CompanyName;
    }

    public void setCompany(String mcompany) {
        this.CompanyName = mcompany;
    }

    public String getMcapacity() {
        return TankCapacity;
    }

    public void setMcapacity(String mcapacity) {
        this.TankCapacity = mcapacity;
    }

    public String getMplate() {
        return NumberPlate;
    }

    public void setMplate(String mplate) {
        this.NumberPlate = mplate;
    }

    public String getMprice() {
        return ServicePrice;
    }

    public void setMprice(String mprice) {
        this.ServicePrice = mprice;
    }

    public String getMimageUrl() {
        return imageUri;
    }

    public void setMimageUrl(String mimageUrl) {
        this.imageUri = mimageUrl;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getMtimestamp() {
        return mtimestamp;
    }

    public void setMtimestamp(String mtimestamp) {
        this.mtimestamp = mtimestamp;
    }

    public String getMuid() {
        return muid;
    }

    public void setMuid(String muid) {
        this.muid = muid;
    }
}
