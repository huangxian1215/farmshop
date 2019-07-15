package com.example.farmshop.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
/**
 * Created by ouyangshen on 2016/11/11.
 */
public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = null;
        if(MainApplication.getInstance().mSessionId.equals("")){
            intent = new Intent(this, LoginActivity.class);
        }else{
            intent = new Intent(this, CenterActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
