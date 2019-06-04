package com.example.farmshop.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonUtil {

    public static String getJson(String fileName){
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(fileName);
            InputStream in = null;
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                sb.append((char) tempbyte);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static ArrayList<String> objStringToArrayList(String objdata){
        ArrayList<String> arrList = new ArrayList<String>();
        try{
            JSONObject obj = new JSONObject(objdata);
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

    public static ArrayList<String> arrayStringToArrayList(String arraydata){
        ArrayList<String> arrList = new ArrayList<String>();
        try{
             JSONArray jsonArray = new JSONArray(arraydata);
             for (int i=0; i < jsonArray.length(); i++){
                 arrList.add(jsonArray.get(i).toString());
             }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return arrList;
    }
}
