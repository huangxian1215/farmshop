package com.example.farmshop.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.farmshop.R;
import com.example.farmshop.activity.LoginActivity;
import com.example.farmshop.upfiles.activity.UpFileMainActivity;

/**
 * Created by ouyangshen on 2016/11/11.
 */
public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoginActivity.class);
        //Intent intent = new Intent(this, WellcomeActivity.class);
        startActivity(intent);
    }

}
