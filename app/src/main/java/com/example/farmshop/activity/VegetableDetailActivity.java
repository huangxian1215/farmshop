package com.example.farmshop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.adapter.DetailPictureAdapter;
import com.example.farmshop.task.LoadFilesTask;
import com.example.farmshop.task.LoadFilesTask.onGetFileListener;
import com.example.farmshop.util.JsonUtil;
import com.example.farmshop.util.MyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VegetableDetailActivity extends AppCompatActivity implements OnClickListener ,onGetFileListener {
    private TextView tv_desc;
    private ListView lv_img;
    private DetailPictureAdapter madapter;
    private String mName = "";
    private String mType = "";
    private ArrayList<String> mInfo;

    LoadFilesTask mloadconfig;
    private ArrayList<String> mVegetableDownList;
    private int mDownCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent  = getIntent();
        Bundle bundle = intent.getExtras();
        mType = bundle.getString("click_vegetable_type");
        mName = bundle.getString("click_vegetable_name");
        setContentView(R.layout.activity_vegetabledetail);
        tv_desc = (TextView) findViewById(R.id.tv_descript);
        tv_desc.setOnClickListener(this);
        lv_img = (ListView) findViewById(R.id.lv_img);

        initPictureConfig();
        startDownLoadTask();
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.tv_descript){
            Intent intent = new Intent(this, WebBrowserActivity.class);
            intent.putExtra("baidubaike_search", mName);
            startActivity(intent);
        }
    }
    //将所有图片下载缓存到全局变量供使用
    private void initPictureConfig(){
        mInfo = new ArrayList<>();
        mInfo.add("好消息！大减价，赶快抢购啊啊啊啊啊 啊啊!!!");
        String savePath = MainApplication.getInstance().savePath + "pictureConfig.json";
        String strJson = JsonUtil.getText(savePath);
        mVegetableDownList = new ArrayList<>();

        Map<String, String> everyObj = new HashMap<>();
        everyObj = JsonUtil.objStringToMapList(strJson);
        for(Map.Entry<String, String> entry : everyObj.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ((key).equals(mType)) {
                ArrayList<String>  every = new ArrayList<>();
                every = JsonUtil.arrayStringToArrayList(value);
                for(int n = 0; n < every.size(); n++){
                    String filePath = MainApplication.getInstance().savePath + MyUtil.getFileName(every.get(n))+".jpg";
                    File File = new File(filePath);
                    if(!File.exists()){
                        mVegetableDownList.add(every.get(n));
                    }else{
                        mInfo.add(filePath);
                    }
                }
                break;
            }
        }
        madapter = new DetailPictureAdapter(this, mInfo);
        lv_img.setAdapter(madapter);
    }

    private void startDownLoadTask(){
        if(mVegetableDownList.size() > 0 && mDownCount == 0){
            mloadconfig = new LoadFilesTask();
            mloadconfig.setOnGetFileListener(this);
            String downUrl = MainApplication.getInstance().httpUrl + mVegetableDownList.get(0);
            String savePath = MainApplication.getInstance().savePath + MyUtil.getFileName(mVegetableDownList.get(0)) + ".jpg";
            mloadconfig.execute(downUrl,savePath, "jpg");
            mDownCount++;
        }
    }

    @Override
    public void onGetFile(String info){
        mInfo.add(MainApplication.getInstance().savePath + MyUtil.getFileName(mVegetableDownList.get(mDownCount - 1)) + ".jpg");

        madapter.notifyDataSetChanged();
        if(mDownCount < mVegetableDownList.size()){
            String savePath = MainApplication.getInstance().savePath + MyUtil.getFileName(mVegetableDownList.get(mDownCount))+".jpg";
            mloadconfig = new LoadFilesTask();
            mloadconfig.setOnGetFileListener(this);
            String downUrl = MainApplication.getInstance().httpUrl + mVegetableDownList.get(mDownCount);
            mloadconfig.execute(downUrl, savePath, "jpg");
            mDownCount++;
        }
    }

}
