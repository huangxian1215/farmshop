package com.example.farmshop.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class TimeUtils {
    private static final String TAG = "Utils";

    public static String getNowDateTime() {
        String format = "yyyyMMddHHmmss";
        SimpleDateFormat s_format = new SimpleDateFormat(format);
        Date d_date = new Date();
        String s_date = "";
        s_date = s_format.format(d_date);
        return s_date;
    }

    public static String getNowDateTimeFull() {
        String format = "yyyyMMddHHmmssSSS";
        SimpleDateFormat s_format = new SimpleDateFormat(format);
        Date d_date = new Date();
        String s_date = "";
        s_date = s_format.format(d_date);
        return s_date;
    }

    public static String getNowDateTimeFormat() {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat s_format = new SimpleDateFormat(format);
        Date d_date = new Date();
        String s_date = "";
        s_date = s_format.format(d_date);
        return s_date;
    }

    public static String getNowTime() {
        SimpleDateFormat s_format = new SimpleDateFormat("HH:mm:ss");
        return s_format.format(new Date());
    }

    public static long getNowTimeLongSS(){
        return new Date().getTime()/1000;
    }

}
