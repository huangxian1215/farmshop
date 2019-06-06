package com.example.farmshop.upfiles.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.upfiles.thread.TcpSendData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UpFileMainActivity extends AppCompatActivity{

    private EditText et_type;
    private Button btnMedia;
    private EditText et_ip;
    private EditText et_port;
    private ImageView iv_image;
    private TextView tv_filename;
    private String mFileurl;
    private String mFilename;
    private String socket_ip = "";
    private int socket_port = 0;
    private File mFile;

    TcpSendData sd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main_upfile);
        btnMedia = findViewById(R.id.btn_media);
        et_ip = (EditText) findViewById(R.id.et_ip);
        et_port = (EditText) findViewById(R.id.et_port);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        tv_filename = (TextView) findViewById(R.id.tv_filename);
        et_type = (EditText) findViewById(R.id.et_type);

        sd = new TcpSendData();
        btnMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpFileMainActivity.this,MediaStoreActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket_ip = et_ip.getText().toString();
                String strport = et_port.getText().toString();
                String type = et_type.getText().toString();
                socket_port = Integer.parseInt(strport);
                if(mFileurl.equals("")){
                    showMessage("请选择一个文件");
                }else{
                    sd.setIpPort(socket_ip, socket_port);
                    sd.setInfo(mFile, type);
                    new Thread(sd).start();
                }
            }
        });
        getPermissions();
    }

    private void getPermissions(){
        //申请SD卡读写权限
        ActivityCompat.requestPermissions(UpFileMainActivity.this, new String[]{android
                .Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE
        }, 1);
    }

    public void refreshWidget(){
        tv_filename.setText(mFilename);
        Bitmap bitmap = null;
        try{
            FileInputStream fis = new FileInputStream(mFileurl);
            bitmap = BitmapFactory.decodeStream(fis);
            iv_image.setImageBitmap(bitmap);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();

        mFileurl = MainApplication.getInstance().selectFileUrl;
        mFile = new File(mFileurl);
        if(mFile.exists()){
            mFilename = mFile.getName();
        }
        if(!mFileurl.equals("")){
            refreshWidget();
        }
    }
}
