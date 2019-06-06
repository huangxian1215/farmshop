package com.example.farmshop.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.task.LoadFilesTask;
import com.example.farmshop.task.LoadFilesTask.OnGetFileListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class WellcomeActivity extends AppCompatActivity implements OnGetFileListener {
    private MainApplication app;
    private String configUrl = "";
    private String savePath = "";

    private ArrayList<String>  mPictureConfig;
    private String type2 = "";

    LoadFilesTask mloadconfig;
    private ImageView tv_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        tv_image = (ImageView) findViewById(R.id.tv_image);

        app = MainApplication.getInstance();
        configUrl = app.httpUrl + "pictureConfig.json";
        savePath = app.savePath + "pictureConfig.json";

        File file = new File(app.savePath);
        if(!file.exists()){
            Boolean flag = file.mkdirs();
        }

//        if(jsonfile.equals("")){
//
//        }
//        file = new File(savePath);
//        if(!file.exists()){
            mloadconfig = new LoadFilesTask();
            mloadconfig.setOnGetFileListener(this);
            mloadconfig.execute(configUrl, savePath, "json");
//        }else{
            //setImageView(savePath);
//        }

    }

    @Override
    public void onGetFile(String info){

        Toast.makeText(this, "下载配置成功", Toast.LENGTH_SHORT).show();

        if(info.equals("jpg")){
            setImageView(app.savePath + "timg1.jpg");
        }
        if(info.equals("json")){
            String jsonfile = getJson(savePath);
            mPictureConfig = analyseStringToJson(jsonfile);
            type2 = mPictureConfig.get(1);
            LoadFilesTask picload =  new LoadFilesTask();
            picload.setOnGetFileListener(this);
            picload.execute(app.httpUrl + type2, app.savePath + "timg1.jpg", "jpg");
        }

    }

    public void setImageView(String url){
        Bitmap bitmap = null;
        try{
            FileInputStream fis = new FileInputStream(url);
            bitmap = BitmapFactory.decodeStream(fis);
            tv_image.setImageBitmap(bitmap);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public String getJson(String fileName){
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

    public ArrayList<String> analyseStringToJson(String data){
        ArrayList<String> arrList = new ArrayList<String>();
        try{
            JSONObject obj = new JSONObject(data);
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
