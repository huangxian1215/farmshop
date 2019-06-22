package com.example.farmshop.bean;

public class BuyBuyBuyList {
    public String picUrl;
    public String name;
    public double count;
    public double price;
    public double amount;
    public String desc;

    public BuyBuyBuyList(String PicUrl, String Name, double Price, String Desc){
        picUrl = PicUrl;
        name = Name;
        count = 1;
        price = Price;
        amount = Price * count;
        desc = Desc;
    }
}
