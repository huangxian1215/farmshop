package com.example.farmshop.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.farmshop.R;

public class UserDetailActivity extends AppCompatActivity{
    private ImageView iv_headimg;
    private TextView tv_name;
    private TextView tv_realname;
    private TextView tv_id;
    private TextView tv_sex;
    private TextView tv_age;
    private TextView tv_phone;
    private TextView tv_location;
    private Button btn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetail);
        initwidget();
    }

    public void initwidget(){
        iv_headimg = (ImageView)findViewById(R.id.iv_head);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_realname = (TextView)findViewById(R.id.tv_realname);
        tv_id = (TextView)findViewById(R.id.tv_id);
        tv_sex = (TextView)findViewById(R.id.tv_sex);
        tv_age = (TextView)findViewById(R.id.tv_age);
        tv_phone = (TextView)findViewById(R.id.tv_phone);
        tv_location = (TextView)findViewById(R.id.tv_location);
        btn_edit = (Button)findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
    }


}
