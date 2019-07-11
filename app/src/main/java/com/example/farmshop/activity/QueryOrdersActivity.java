package com.example.farmshop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.adapter.QueryOrderAdapter;
import com.example.farmshop.bean.EachOneInfo;
import com.example.farmshop.bean.QueryInfo;
import com.example.farmshop.farmshop;
import com.example.farmshop.upfiles.utils.PackProtoUtil;
import com.example.farmshop.util.VirtureUtil.onClickItemListener;
import com.example.farmshop.util.VirtureUtil.onGetNetDataListener;
import com.google.protobuf.Any;

import java.io.IOException;
import java.util.ArrayList;

public class QueryOrdersActivity extends AppCompatActivity  implements onGetNetDataListener, onClickItemListener {
    private farmshop.QueryOrderResponse mData = null;
    private ListView lv_queryorder;
    private Handler hd_fresh = new Handler();
    private Runnable rd_fresh = new Runnable() {
        @Override
        public void run() {
            fresh();
        }
    };
    private QueryOrderAdapter mAdapter = null;
    private ArrayList<QueryInfo> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queryorder);
        lv_queryorder = findViewById(R.id.lv_queryorder);
        MainApplication.getInstance().mTransmit.addOnNetListener("QueryOrdersActivity", this);
        queryOrder();
    }

    @Override
    public void onGetNetData(Object info, farmshop.MsgId msgid){
        farmshop.baseType data = (farmshop.baseType) info;
        try{
            Any any = data.getObject(0);
            if(msgid == farmshop.MsgId.DELETEORDER_RES){
                queryOrder();
            }else{
                mData = farmshop.QueryOrderResponse.parseFrom(any.getValue());
                hd_fresh.postDelayed(rd_fresh, 10);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position){
        if(mList.size() > position){
            int id = mList.get(position).id;
            farmshop.baseType.Builder delete = farmshop.baseType.newBuilder().setType(farmshop.MsgId.DELETEORDER_REQ).setSessionId(MainApplication.getInstance().mSessionId);
            farmshop.DeleteOrderRequest req = farmshop.DeleteOrderRequest.newBuilder().setId(id).build();
            delete.addObject(Any.pack(req));
            PackProtoUtil.packSend(delete);
        }
    }

    private void queryOrder(){
        farmshop.baseType.Builder query = farmshop.baseType.newBuilder().setType(farmshop.MsgId.QUERYORDER_REQ).setSessionId(MainApplication.getInstance().mSessionId);
        farmshop.QueryOrderRequest req = farmshop.QueryOrderRequest.newBuilder().setLastTime(0).build();
        query.addObject(Any.pack(req));
        PackProtoUtil.packSend(query);
    }

    private void fresh(){
        mList.clear();
        int size = mData.getOrdersCount();

        for(int i = 0; i < size; i++){
            farmshop.Order order = mData.getOrders(i);
            long time = order.getTime();
            int id = order.getId();
            int state = order.getState();
            int type = order.getType();
            int amount = order.getAmount();
            String message = order.getMessage();
            ArrayList<EachOneInfo> list = new ArrayList<>();
            for(int n = 0; n < order.getListList().size(); n++){
                String name = order.getList(n).getName();
                int weight = order.getList(n).getWeight();
                int price = order.getList(n).getPrice();
                list.add(new EachOneInfo(name, weight, price));
            }
            QueryInfo each = new QueryInfo(time, id, state, type, amount, message, list);
            mList.add(each);
        }
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }else{
            mAdapter = new QueryOrderAdapter(this, mList);
            mAdapter.setOnClickItemListener(this);
            lv_queryorder.setAdapter(mAdapter);
        }
    }


}
