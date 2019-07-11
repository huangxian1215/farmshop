package com.example.farmshop.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.farmshop.farmshop;
import com.example.farmshop.upfiles.utils.PackProtoUtil;
import com.example.farmshop.util.TimeUtils;
import com.example.farmshop.util.VirtureUtil.onGetNetDataListener;
import com.example.farmshop.util.VirtureUtil.onClickItemListener;
import com.google.protobuf.Any;

import java.io.IOException;
import java.util.ArrayList;

public class BasketActivity extends AppCompatActivity implements OnClickListener, onClickItemListener ,onGetNetDataListener {
    private EditText et_tips;
    private TextView tv_totle;
    private ListView lv_order;
    private OrderItemAdapter mAdapter;
    private double mTtole;
    private Boolean upOrderSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        et_tips = findViewById(R.id.et_tip);
        findViewById(R.id.tv_adminId).setOnClickListener(this);
        findViewById(R.id.tv_upOrder).setOnClickListener(this);
        tv_totle = findViewById(R.id.tv_totle);
        lv_order = findViewById(R.id.lv_vegetable);


        initData();
        countTotle();
        MainApplication.getInstance().mTransmit.addOnNetListener("BasketActivity", this);
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
            if(mTtole < 0.01) return;
            gotoAccounts();
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
        mTtole = 0.0;
        for(int i = 0; i < list.size(); i++){
            mTtole += list.get(i).amount;
        }
        String ttle = String.format("%.2f", mTtole);
        tv_totle.setText(ttle+"￥");
    }

    private void gotoAccounts(){
        farmshop.baseType.Builder uporder = farmshop.baseType.newBuilder().setType(farmshop.MsgId.UPORDER_REQ).setSessionId(MainApplication.getInstance().mSessionId);
        farmshop.UpOrderRequest.Builder orders = farmshop.UpOrderRequest.newBuilder();

        //TimeUtils.getNowTimeLongSS()
        ArrayList<BuyBuyBuyList> list = MainApplication.getInstance().mBasketList;
        for(int i = 0; i < list.size(); i++){
            String name = list.get(i).name;
            int count =(int)(list.get(i).count*10); //两
            int price =(int)(list.get(i).price*100); //分
            farmshop.BuyOneInfo.Builder one = farmshop.BuyOneInfo.newBuilder()
                    .setName(name)
                    .setWeight(count)
                    .setPrice(price);
            orders.addBuyList(one);
        }
        int totle = (int)(mTtole*100); //分

        orders.setType(farmshop.OrderState.ORDER_NOTRECIEVE.getNumber()).setTime(TimeUtils.getNowTimeLongSS()).setAmount(totle).setMessage(et_tips.getText().toString());

        Any any = Any.pack(orders.build());
        uporder.addObject(any);
        PackProtoUtil.packSend(uporder);

    }
    @Override
    public void onGetNetData(Object info, farmshop.MsgId msgid){
        farmshop.baseType data = (farmshop.baseType) info;
        try{
            Any any = data.getObject(0);
            farmshop.UpOrderResponse resp = farmshop.UpOrderResponse.parseFrom(any.getValue());
            upOrderSuccess = (resp.getResult() == 0);
            hd_uporder.postDelayed(rb_uporder, 0);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    Handler hd_uporder = new Handler();
    Runnable rb_uporder = new Runnable(){
        @Override
        public void run(){
            if(upOrderSuccess){
                Toast.makeText(getApplicationContext(), "下单成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "下单失败,请稍后重试", Toast.LENGTH_SHORT).show();
            }
            MainApplication.getInstance().mTransmit.deleteOnNetListener("BasketActivity");
            finish();
        }
    };
}
