package com.example.farmshop.bean;

public class EachOneInfo {
    public String name;
    public double weight;
    public double price;

    public EachOneInfo(String tname, int tweight, int tprice){
        name = tname;
        weight = (double)tweight/10;
        price = (double)tprice/100;
    }
}
