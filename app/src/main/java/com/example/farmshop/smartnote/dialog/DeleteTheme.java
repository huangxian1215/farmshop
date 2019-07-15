package com.example.farmshop.smartnote.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.farmshop.R;

public class DeleteTheme extends Activity implements View.OnClickListener {
    //控件
    private EditText themeEdt;
    //消息ID
    private static final int DELETHEME_REQUESTCODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_deletetheme);
        init();
    }

    private void init() {
        findViewById(R.id.dele_confirm).setOnClickListener(this);
        findViewById(R.id.dele_cancel).setOnClickListener(this);
        themeEdt = (EditText) findViewById(R.id.dele_edit_theme);
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.dele_confirm:
                confirm();
                break;
            case R.id.dele_cancel:
                cancle();
                break;
        }
    }

    private void confirm() {
        String theme = String.valueOf(themeEdt.getText());
        Intent intent = new Intent();
        intent.putExtra("theme", theme);
        setResult(DELETHEME_REQUESTCODE, intent);
        finish();
    }

    private void cancle() {
        setResult(DELETHEME_REQUESTCODE);
        finish();
    }
}
