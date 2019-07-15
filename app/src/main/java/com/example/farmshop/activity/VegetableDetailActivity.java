package com.example.farmshop.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.adapter.DetailPictureAdapter;
import com.example.farmshop.bean.BuyBuyBuyList;
import com.example.farmshop.task.LoadFilesTask;
import com.example.farmshop.task.LoadFilesTask.onGetFileListener;
import com.example.farmshop.util.JsonUtil;
import com.example.farmshop.util.MyUtil;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VegetableDetailActivity extends AppCompatActivity implements OnClickListener {
    private TextView tv_desc;
    private TextView tv_add;
    private TextView tv_delete;
    private TextView tv_basket;
    private ListView lv_img;
    private DetailPictureAdapter madapter;
    private String mName = "";
    private String mType = "";
    private String mAds = "";
    private String mPicUrl = "";
    private double mPrice = 0.0;
    private ArrayList<String> mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mType = bundle.getString("click_vegetable_type");
        mName = bundle.getString("click_vegetable_name");
        mAds = bundle.getString("click_vegetable_ads");
        mPicUrl = bundle.getString("click_vegetable_url");
        mPrice = bundle.getDouble("click_vegetable_price", 0.0);

        setContentView(R.layout.activity_vegetabledetail);
        lv_img = (ListView) findViewById(R.id.lv_img);
        tv_desc = (TextView) findViewById(R.id.tv_descript);
        tv_basket = (TextView) findViewById(R.id.tv_basket);
        tv_delete = (TextView) findViewById(R.id.tv_delete_this);
        tv_add = (TextView) findViewById(R.id.tv_add_this);
        tv_desc.setOnClickListener(this);
        tv_basket.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        tv_add.setOnClickListener(this);

        displayCount();
        initPictureConfig();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_descript) {
            Intent intent = new Intent(this, WebBrowserActivity.class);
            intent.putExtra("baidubaike_search", mName);
            startActivity(intent);
        }
        if (v.getId() == R.id.tv_basket) {
            Intent intent = new Intent(this, BasketActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.tv_delete_this) {
            if (indexOfChoose(mName) >= 0) {

                MainApplication.getInstance().mBasketList.remove(indexOfChoose(mName));
                displayCount();
            } else {
                Toast.makeText(this, "已拿掉", Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.tv_add_this) {
            if (indexOfChoose(mName) >= 0) {
                Toast.makeText(this, "已放入", Toast.LENGTH_SHORT).show();
            } else {
                BuyBuyBuyList buyone = new BuyBuyBuyList(mPicUrl, mName, mPrice, "");
                MainApplication.getInstance().mBasketList.add(buyone);
                displayCount();
            }
        }
    }

    private int indexOfChoose(String name) {
        int index = -1;
        ArrayList<BuyBuyBuyList> list = MainApplication.getInstance().mBasketList;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).name.equals(name)) {
                return i;
            }
        }
        return index;
    }

    private void displayCount() {
        String des = "篮子(" + String.valueOf(MainApplication.getInstance().mBasketList.size()) + ")";
        tv_basket.setText(des);
    }

    //将所有图片下载缓存到全局变量供使用
    private void initPictureConfig() {
        mInfo = new ArrayList<>();
        mInfo.add(mAds);
        String savePath = MainApplication.getInstance().savePath + "pictureConfig.json";
        String strJson = JsonUtil.getText(savePath);

        Map<String, String> everyObj = new HashMap<>();
        everyObj = JsonUtil.objStringToMapList(strJson);
        for (Map.Entry<String, String> entry : everyObj.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ((key).equals(mType)) {
                ArrayList<String> every = new ArrayList<>();
                every = JsonUtil.arrayStringToArrayList(value);
                for (int n = 0; n < every.size(); n++) {
//                    String filePath = MyUtil.getFileName(every.get(n)) + ".jpg";
                    mInfo.add(every.get(n));
                }
                break;
            }
        }
        madapter = new DetailPictureAdapter(this, mInfo);
        lv_img.setAdapter(madapter);
    }
}
