package com.example.farmshop.upfiles.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.dialog.MyDialog;
import com.example.farmshop.upfiles.thread.TcpSendData;
import com.example.farmshop.util.VirtureUtil.updateProgressListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UpFileMainActivity extends AppCompatActivity implements updateProgressListener{

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

    private MyDialog myDialog;

    TcpSendData sdFile = null;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main_upfile);
        btnMedia = findViewById(R.id.btn_media);
        et_ip = (EditText) findViewById(R.id.et_ip);
        et_port = (EditText) findViewById(R.id.et_port);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        tv_filename = (TextView) findViewById(R.id.tv_filename);
        et_type = (EditText) findViewById(R.id.et_type);

        sdFile = new TcpSendData();
        sdFile.setProgressListener(this);
        btnMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpFileMainActivity.this,MediaStoreActivity.class);
                startActivity(intent);
            }
        });

        myDialog = new MyDialog();
        myDialog.createProgressDialog(mContext);
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
                    sdFile.setIpPort(socket_ip, socket_port);
                    sdFile.setInfo(mFile, type);
                    new Thread(sdFile).start();
                    myDialog.showProgressDialog("稍等", "文件上传中...");
                }
            }
        });
        init();
    }

    private void init(){
        if(!MainApplication.getInstance().getLocalStore("login_ip").equals("")) {
            et_ip.setText(MainApplication.getInstance().getLocalStore("login_ip"));
        }
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
    public void updateProgress(int progress){
        if(myDialog != null){
            myDialog.freshProgress(progress);
            if(progress >= 100){
                myDialog.hideProgressDialog();
            }
        }
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
