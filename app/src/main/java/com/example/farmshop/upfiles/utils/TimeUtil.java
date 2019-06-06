package com.example.farmshop.upfiles.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static String getTimeNow(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss.SSS");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);


    }
}
