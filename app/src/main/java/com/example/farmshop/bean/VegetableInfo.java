package com.example.farmshop.bean;

public class VegetableInfo {
    public String pictureDownUrl;
    public String pictureurl;
    public String name;
    public String type;
    public String desc;
    public String ads;
    public Boolean hasstore;
    public String timetoeat;
    public double price;

    public VegetableInfo(){
        name = "";
        pictureurl = "";
        timetoeat = "";
        hasstore = false;
        price = 0.0;
        desc = "";
        type = "";
        ads = "";
        pictureDownUrl = "";
    }
}
