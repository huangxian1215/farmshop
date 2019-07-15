package com.example.farmshop.smartnote.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class JsonDataTtransfer {
    public static String transToString(ArrayList<String> info){
        String [] arr = info.toArray(new String[0]);

        JSONObject json = new JSONObject();
        try{
            for(int i = 0; i < arr.length; i = i+2){
                json.put("item" + String.valueOf(i),arr[i]);
                json.put("item" + String.valueOf(i+1),arr[i+1]);
            }
        }catch ( JSONException e){
            e.printStackTrace();
        }
        return json.toString();
    }


    public static ArrayList<String> anaylse(String data){
        ArrayList<String> arrList = new ArrayList<String>();
        try{
            JSONObject obj = new JSONObject((String) data);
            Iterator it = obj.keys();
            String vol = "";//值
            String key = null;//键
            while(it.hasNext()){//遍历JSONObject
                key = (String) it.next().toString();
                vol = obj.getString(key);
                arrList.add(vol);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return arrList;
    }
}
