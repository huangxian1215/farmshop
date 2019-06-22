package com.example.farmshop.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.adapter.OrderItemAdapter;
import com.example.farmshop.bean.BuyBuyBuyList;
import com.example.farmshop.util.VirtureUtil.onClickItemListener;

import java.util.ArrayList;

public class BasketActivity extends AppCompatActivity implements OnClickListener, onClickItemListener {
    private EditText et_tips;
    private TextView tv_click_copy;
    private TextView tv_upOrder;
    private TextView tv_totle;
    private ListView lv_order;
    private OrderItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        et_tips = (EditText)findViewById(R.id.et_tip);
        tv_click_copy = (TextView)findViewById(R.id.tv_adminId);
        tv_upOrder = (TextView)findViewById(R.id.tv_upOrder);
        tv_totle = (TextView)findViewById(R.id.tv_totle);
        lv_order = (ListView)findViewById(R.id.lv_vegetable);

        tv_click_copy.setOnClickListener(this);
        tv_upOrder.setOnClickListener(this);

        initData();
        countTotle();
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.tv_adminId){
            ClipboardManager myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            String adminId = "XIANXIANGUAIGUAI";
            ClipData myClip = ClipData.newPlainText("text", adminId);
            Toast.makeText(getApplicationContext(), "已复制到剪切板",Toast.LENGTH_LONG).show();
            myClipboard.setPrimaryClip(myClip);
        }

        if(v.getId() == R.id.tv_upOrder){

        }
    }

    @Override
    public void onItemClick(View view, int position){
        mAdapter.notifyDataSetChanged();
        countTotle();
    }

    private void initData(){
        mAdapter = new OrderItemAdapter(this, MainApplication.getInstance().mBasketList);
        mAdapter.setOnClickItemListener(this);
        lv_order.setAdapter(mAdapter);
    }

    private void countTotle(){
        //计算总价
        ArrayList<BuyBuyBuyList> list = MainApplication.getInstance().mBasketList;
        double totle = 0.0;
        for(int i = 0; i < list.size(); i++){
            totle += list.get(i).amount;
        }
        String ttle = String.format("%.2f", totle);
        tv_totle.setText(ttle+"￥");
    }
}
