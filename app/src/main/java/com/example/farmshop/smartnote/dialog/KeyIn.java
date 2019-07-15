package com.example.farmshop.smartnote.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.farmshop.R;

public class KeyIn extends Activity implements View.OnClickListener {
    //控件
    private EditText keyInEdt;
    //消息ID
    private static final int KEYIN_REQUESTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_keyin);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        init(bundle.getString("newUser", ""));
    }

    private void init(String flag) {
        findViewById(R.id.btn_keyin).setOnClickListener(this);
        keyInEdt = (EditText) findViewById(R.id.edit_keyin);
        if(flag.equals("true")){
            keyInEdt.setHint("设置一个登陆密码");
        }
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.btn_keyin:
                confirm();
                break;
        }
    }

    private void confirm() {
        String keyin = String.valueOf(keyInEdt.getText());
        Intent intent = new Intent();
        intent.putExtra("keyin", keyin);
        setResult(KEYIN_REQUESTCODE, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        String keyin = "";
        Intent intent = new Intent();
        intent.putExtra("keyin", keyin);
        setResult(KEYIN_REQUESTCODE, intent);
        super.onDestroy();
        finish();
    }

}

