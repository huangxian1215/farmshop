package com.example.farmshop.upfiles.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
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
import java.io.FileOutputStream;

import id.zelory.compressor.Compressor;

public class UpfileActivity extends AppCompatActivity implements OnClickListener, updateProgressListener {
    private EditText et_ip;
    private ImageView iv_picture;
    private TextView tv_picinfo;


//    private static final int COMPRESS = 1;
    private static final int OPENPIC = 2;
    private static final int SAVEHEIGHQUALITY = 3;

    private String mFileName = "";
    private String mTarFileName = "";
    private File mTarFile = null;
    private String mTarDir = "";
    private String mTarname = "";

    TcpSendData sdFile = null;
    private MyDialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upfile);

        Intent intent  = getIntent();
        Bundle bundle = intent.getExtras();
        mTarDir = bundle.getString("target_dir");
        mTarname = bundle.getString("target_name");
        et_ip = findViewById(R.id.et_ip);
        iv_picture = findViewById(R.id.iv_image);
        tv_picinfo = findViewById(R.id.tv_fileinfo);

        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_compress).setOnClickListener(this);
        findViewById(R.id.btn_camera).setOnClickListener(this);
        findViewById(R.id.btn_picture).setOnClickListener(this);

        initPhotoError();


        sdFile = new TcpSendData();
        sdFile.setProgressListener(this);
        myDialog = new MyDialog();
        myDialog.createProgressDialog(this);

        et_ip.setText(MainApplication.getInstance().getLocalStore("upfile_ip"));
    }

    private void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                if(mTarFileName.equals("")) return;
                if(mTarFile.length() > 1024 * 500){
                    Toast.makeText(this, "文件大于500K，请压缩", Toast.LENGTH_SHORT).show();
                    return;
                }
                sdFile.setIpPort(et_ip.getText().toString(), MainApplication.upFilePort);
                sdFile.setInfo(mTarFile, mTarDir);
                new Thread(sdFile).start();
                myDialog.showProgressDialog("稍等", "文件上传中...");

                MainApplication.getInstance().setLocalStore("upfile_ip", et_ip.getText().toString());
                break;
            case R.id.btn_compress:
                if(mTarFileName.equals("")) return;
                writefileData();
                break;
            case R.id.btn_camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mFileName = MainApplication.getInstance().savePath + String.valueOf(System.currentTimeMillis()) + ".jpeg";
                File file = new File(mFileName);
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(intent, SAVEHEIGHQUALITY);
                break;
            case R.id.btn_picture:
                Intent picture = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, OPENPIC);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

//        if(requestCode == SAVEHEIGHQUALITY && resultCode == Activity.RESULT_OK && null != data) {
//            //拍照进入
//        }

        if(requestCode == OPENPIC && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            mFileName = c.getString(columnIndex);
            c.close();
        }

        mTarFileName = mFileName;
        refreshInfo();
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

    public void writefileData(){
        mTarFileName = MainApplication.getInstance().savePath + String.valueOf(System.currentTimeMillis()) + ".jpeg";
        File tarFile = new Compressor.Builder(this)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(100)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(MainApplication.getInstance().savePath+"camerabuff/")
                .build()
                .compressToFile(new File(mFileName));
        try{
            FileOutputStream fos = new FileOutputStream(mTarFileName);
            byte[] bytes = new byte[1024];
            FileInputStream in = new FileInputStream(tarFile);

            while (in.read(bytes) != -1){
                fos.write(bytes);
            }
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        refreshInfo();
    }

    public void refreshInfo(){
        mTarFile = new File(mTarFileName);
        String size = String.valueOf(mTarFile.length()/1024) + "K";
        String info = "上传到->" + mTarDir + "(" + mTarname + ")" + "  文件:" + mTarFile.getName() + "  大小:"+ size;
        tv_picinfo.setText(info);
        try{
            FileInputStream fis = new FileInputStream(mTarFileName);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            iv_picture.setImageBitmap(bitmap);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
