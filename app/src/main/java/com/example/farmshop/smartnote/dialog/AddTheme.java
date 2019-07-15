package com.example.farmshop.smartnote.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.farmshop.R;
import com.example.farmshop.smartnote.adapter.KeyValueEditAdapter;
import com.example.farmshop.smartnote.bean.KeyValueEditItem;
import com.example.farmshop.smartnote.util.JsonDataTtransfer;

import java.util.ArrayList;

public class AddTheme extends Activity implements View.OnClickListener {

    private ArrayList<KeyValueEditItem> keyValueItemList;
    private KeyValueEditAdapter adapter;
    //控件
    private EditText themeEdt;
    private ListView lv_item;
    private Drawable drawable;
    private int dividerHeight = 2;
    //消息ID
    private static final int ADDTHEME_REQUESTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addtheme);

        lv_item = (ListView) findViewById(R.id.lv_key_value);
//        drawable = getResources().getDrawable(R.drawable.line_h);
        init();
        //test
        putIntoItemAdapter(1);
    }

    private void init() {
        findViewById(R.id.insert_confirm).setOnClickListener(this);
        findViewById(R.id.insert_cancel).setOnClickListener(this);
        findViewById(R.id.insert_add).setOnClickListener(this);
        themeEdt = (EditText) findViewById(R.id.add_edit_theme);
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.insert_confirm:
                confirm();
                break;
            case R.id.insert_cancel:
                cancle();
                break;
            case R.id.insert_add:
                addItem();
                break;
        }

    }

    private void confirm() {
        String theme = String.valueOf(themeEdt.getText());
        //把信息做成字符串，再转成json,title为空时省略
        ArrayList<String> itemList = new ArrayList<String>();
        for (int i=0; i<keyValueItemList.size(); i++) {
            KeyValueEditItem info = keyValueItemList.get(i);
            if(info.title.length() == 0) continue;
            itemList.add(info.title);
            itemList.add(info.valueType);
        }
        String jsonDesc = JsonDataTtransfer.transToString(itemList);
        Intent intent = new Intent();
        intent.putExtra("theme", theme);
        intent.putExtra("desc", jsonDesc);
        setResult(ADDTHEME_REQUESTCODE, intent);
        finish();
    }

    private void cancle() {
        setResult(ADDTHEME_REQUESTCODE);
        finish();
    }

    private void addItem(){
        KeyValueEditItem.addOne(keyValueItemList);
        adapter.notifyDataSetChanged();
    }

    private  void putIntoItemAdapter(int count){
        keyValueItemList = KeyValueEditItem.getKeyValueList(count);
        adapter = new KeyValueEditAdapter(this, R.layout.item_edit_key_value, keyValueItemList, Color.WHITE);
        lv_item.setAdapter(adapter);
        lv_item.setDivider(drawable);
        lv_item.setDividerHeight(dividerHeight);
        lv_item.setPadding(0, 0, 0, 0);
        lv_item.setBackgroundColor(Color.TRANSPARENT);
        lv_item.setPadding(0, dividerHeight, 0, dividerHeight);
        lv_item.setBackgroundDrawable(drawable);
    }

}
