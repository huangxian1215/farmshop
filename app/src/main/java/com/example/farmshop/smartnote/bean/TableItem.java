package com.example.farmshop.smartnote.bean;

import org.json.JSONObject;

import java.util.ArrayList;

public class TableItem {
    public int num;
    public String dbNum;
    public String name;
    public String desc;

    public TableItem() {
        this.name = "";
    }

    public  TableItem(String name){
        this.name = name;
    }
    public  TableItem(String name,String desc){
        this.name = name;
        this.desc = desc;
    }

}
