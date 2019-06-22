package com.example.farmshop.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.bean.BuyBuyBuyList;
import com.example.farmshop.util.VirtureUtil.onClickItemListener;


import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderItemAdapter extends BaseAdapter implements OnClickListener {
    private static final String TAG = "OrderItemAdapter";
    private Context mContext;
        private LayoutInflater mInflater;
    private int mCount = 0;

    public OrderItemAdapter(Context context, ArrayList<BuyBuyBuyList> item_list){
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        holder = new ViewHolder();
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_order, null);
            holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
            holder.tv_sub = (TextView) convertView.findViewById(R.id.tv_sub);
            holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            holder.et_count = (EditText) convertView.findViewById(R.id.et_count);

            holder.tv_add.setOnClickListener(this);
            holder.tv_sub.setOnClickListener(this);
            holder.tv_delete.setOnClickListener(this);



            convertView.setTag(holder);
            mCount++;
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Glide.with(mContext).load(MainApplication.getInstance().mBasketList.get(position).picUrl).into(holder.iv_pic);
        holder.tv_name.setText(MainApplication.getInstance().mBasketList.get(position).name);
        holder.tv_price.setText(String.valueOf(MainApplication.getInstance().mBasketList.get(position).price)+"￥");
        holder.tv_add.setTag(MainApplication.getInstance().mBasketList.get(position));
        holder.tv_sub.setTag(MainApplication.getInstance().mBasketList.get(position));
        holder.tv_delete.setTag(MainApplication.getInstance().mBasketList.get(position));
        holder.et_count.setText(String.valueOf(MainApplication.getInstance().mBasketList.get(position).count));

        String amount = String.format("%.2f", MainApplication.getInstance().mBasketList.get(position).amount);
        holder.tv_amount.setText(amount+"￥");
        return convertView;
    }
    @Override
    public void onClick(View v){
        BuyBuyBuyList tag = (BuyBuyBuyList)v.getTag();
        if(v.getId() == R.id.tv_add){
            MainApplication.getInstance().mBasketList.get(indexOfChoose(tag.name)).count += 1;
            MainApplication.getInstance().mBasketList.get(indexOfChoose(tag.name)).amount += MainApplication.getInstance().mBasketList.get(indexOfChoose(tag.name)).price;
        }
        if(v.getId() == R.id.tv_sub){
            if(MainApplication.getInstance().mBasketList.get(indexOfChoose(tag.name)).count < 1) return;
            MainApplication.getInstance().mBasketList.get(indexOfChoose(tag.name)).count -= 1;
            MainApplication.getInstance().mBasketList.get(indexOfChoose(tag.name)).amount -= MainApplication.getInstance().mBasketList.get(indexOfChoose(tag.name)).price;
        }
        if(v.getId() == R.id.tv_delete){
            MainApplication.getInstance().mBasketList.remove(indexOfChoose(tag.name));
        }
        mClickListener.onItemClick(v, 0);
    }

    private onClickItemListener mClickListener;
    public void setOnClickItemListener(onClickItemListener listener){
        mClickListener = listener;
    }

    private int indexOfChoose(String name){
        int index = -1;
        ArrayList<BuyBuyBuyList> list = MainApplication.getInstance().mBasketList;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).name.equals(name)){
                return i;
            }
        }
        return index;
    }

    @Override
    public  Object getItem(int i){
        return MainApplication.getInstance().mBasketList.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public int getCount(){
        return MainApplication.getInstance().mBasketList.size();
    }

    class ViewHolder{
        ImageView iv_pic;
        TextView tv_name;
        TextView tv_price;
        TextView tv_delete;
        TextView tv_add;
        TextView tv_sub;
        TextView tv_amount;
        EditText et_count;
    }
}
