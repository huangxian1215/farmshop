package com.example.farmshop.bean;

import java.util.ArrayList;

public class QueryInfo {
    public long time;
    public int id;
    public int state;
    public int type;
    public double amount;
    public String message;
    public ArrayList<EachOneInfo> list = new ArrayList<>();

    public QueryInfo(long ttime, int tid, int tstate, int ttype, int tamount, String tmessage, ArrayList<EachOneInfo> tlist){
        time = ttime;
        id = tid;
        state = tstate;
        type = ttype;
        amount = (double)tamount/100;
        message = tmessage;
        list = tlist;
    }
}
