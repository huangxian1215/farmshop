
package com.example.farmshop.smartnote.bean;

import java.security.Key;
import java.util.ArrayList;

public class KeyValueEditItem {
    public String title;
    public String valueType;//txt,num,buer
    public String value;


    public KeyValueEditItem() {
        this.title = "";
        this.valueType = "txt";
    }

    public  KeyValueEditItem(String title, String type){
        this.title = title;
        this.valueType = type;
    }

    public static ArrayList<KeyValueEditItem> getKeyValueList(String[] title, String[] type) {
        ArrayList<KeyValueEditItem> keyValueList = new ArrayList<KeyValueEditItem>();
        //默认写上一条序号，删除和修改是需要
        keyValueList.add(new KeyValueEditItem("序号", "num"));
        for(int i = 0; i < title.length; i++){
            keyValueList.add(new KeyValueEditItem(title[i], type[i]));
        }
        return keyValueList;
    }
    public static ArrayList<KeyValueEditItem> getKeyValueList(int count) {
        ArrayList<KeyValueEditItem> keyValueList = new ArrayList<KeyValueEditItem>();
        for(int i = 0; i < count; i++){
            keyValueList.add(new KeyValueEditItem());
        }
        return keyValueList;
    }

    public static ArrayList<KeyValueEditItem> addOne(ArrayList<KeyValueEditItem> keyValueList){
        keyValueList.add(new KeyValueEditItem());
        return keyValueList;
    }

}