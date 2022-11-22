package com.evelyne.labs.trialapp.model;

import java.security.Key;

public class Upload {

    private String key,mcompany,mcapacity,mplate,mprice, mimageUrl,uploadId,
            mtimestamp, muid;


    public Upload(){}
    public Upload(String trim, String s){}

    public Upload(String company,String capacity,String plate, String price, String imageUrl,
                  String timestamp, String uid,String uploadId ){
        if(company.trim().equals("")){
            company = "no name";
        }
        if(capacity.trim().equals("")){
            capacity = "no name";
        }
        if(plate.trim().equals("")){
            plate = "no name";
        }
        if(price.trim().equals("")){
            price = "no name";
        }
        if(timestamp.trim().equals("")){
            timestamp = "no timestamp";
        }
        if(uid.trim().equals("")){
            uid = "no uid";
        }

        mcompany = company;
        mcapacity = capacity;
        mplate = plate;
        mprice = price;
        mimageUrl = imageUrl;
        mtimestamp = timestamp;
        muid = uid;
        this.uploadId=uploadId;


    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getMcapacity() {
        return mcapacity;
    }
    public String setMcapacity() {
        return mcapacity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getMcompany() {
        return mcompany;
    }

    public void setMcompany(String mcompany) {
        this.mcompany = mcompany;
    }


    public void setMcapacity(String mcapacity) {
        this.mcapacity = mcapacity;
    }

    public String getMplate() {
        return mplate;
    }

    public void setMplate(String mplate) {
        this.mplate = mplate;
    }

    public String getMprice() {
        return mprice;
    }

    public void setMprice(String mprice) {
        this.mprice = mprice;
    }

    public String getMimageUrl() {
        return mimageUrl;
    }

    public void setMimageUrl(String mimageUri) {
        this.mimageUrl = mimageUrl;
    }


}
