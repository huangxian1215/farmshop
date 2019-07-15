package com.example.farmshop.smartnote.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;

import com.example.farmshop.R;
import com.example.farmshop.smartnote.adapter.AddKeyValueAdapter;
import com.example.farmshop.smartnote.adapter.KeyValueEditAdapter;
import com.example.farmshop.smartnote.bean.KeyValueEditItem;
import com.example.farmshop.smartnote.util.DateUtil;

import java.util.ArrayList;

public class AddItem extends Activity implements OnClickListener{
    //控件
    private String TAG = "Additem";
    private ListView lv_item;
    private Drawable drawable;
    private int dividerHeight = 2;

    private ArrayList<String> mtitles;
    private ArrayList<String> mtypes;
    private String mName;
    private ArrayList<KeyValueEditItem> mkeyValueItemList;
    private AddKeyValueAdapter madapter;

    //消息ID
    private static final int ADD_REQUESTCODE = 1;
    private static final int DELETE_REQUESTCODE = 2;
    private static final int MODIFY_REQUESTCODE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_additem);
        lv_item = (ListView) findViewById(R.id.lv_values);
//        drawable = getResources().getDrawable(R.drawable.line_h);

        findViewById(R.id.insert_confirm).setOnClickListener(this);
        findViewById(R.id.insert_cancel).setOnClickListener(this);
        findViewById(R.id.insert_modify).setOnClickListener(this);
        findViewById(R.id.insert_delete).setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mtitles = bundle.getStringArrayList("titles");
        mtypes = bundle.getStringArrayList("types");
        mName = bundle.getString("names");
        putIntoItemAdapter();
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.insert_confirm:
                add();
                break;
            case R.id.insert_modify:
                modify();
                break;
            case R.id.insert_cancel:
                cancle();
                break;
            case R.id.insert_delete:
                delete();
            break;
        }
    }

    public void add(){
        ArrayList<String> values = new ArrayList<String>();
        //写上时间
        values.add(DateUtil.getNowDateTime(null));
        for(int i = 1; i < mkeyValueItemList.size(); i++){
            values.add(mkeyValueItemList.get(i).value);
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("values", values);
        intent.putExtras(bundle);
        setResult(ADD_REQUESTCODE, intent);
        finish();
    }
    public void modify(){
        ArrayList<String> values = new ArrayList<String>();
        values.add(mkeyValueItemList.get(0).value);
        //写上时间
        values.add(DateUtil.getNowDateTime(null));
        for(int i = 1; i < mkeyValueItemList.size(); i++){
            values.add(mkeyValueItemList.get(i).value);
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("values", values);
        intent.putExtras(bundle);
        setResult(MODIFY_REQUESTCODE, intent);
        finish();
    }
    public void cancle(){
        finish();
    }
    public void delete(){
        ArrayList<String> values = new ArrayList<String>();
        values.add(mkeyValueItemList.get(0).value);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("values", values);
        intent.putExtras(bundle);
        setResult(DELETE_REQUESTCODE, intent);
        finish();
    }

    private  void putIntoItemAdapter(){
        mkeyValueItemList = KeyValueEditItem.getKeyValueList(mtitles.toArray(new String[0]), mtypes.toArray(new String[0]));
        madapter = new AddKeyValueAdapter(this, R.layout.item_item_key_value, mkeyValueItemList, Color.WHITE, mName);
        lv_item.setAdapter(madapter);
        lv_item.setDivider(drawable);
        lv_item.setDividerHeight(dividerHeight);
        lv_item.setPadding(0, 0, 0, 0);
        lv_item.setBackgroundColor(Color.TRANSPARENT);
        lv_item.setPadding(0, dividerHeight, 0, dividerHeight);
        lv_item.setBackgroundDrawable(drawable);
    }
}
