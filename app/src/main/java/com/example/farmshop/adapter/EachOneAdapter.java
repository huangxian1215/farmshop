package com.example.farmshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.farmshop.R;
import com.example.farmshop.bean.EachOneInfo;

import java.util.ArrayList;

public class EachOneAdapter extends BaseAdapter {

    private static final String TAG = "EachOneAdapter";
    private ArrayList<EachOneInfo> mListEachInfo;
    private LayoutInflater mInflater;

    public EachOneAdapter(Context context, ArrayList<EachOneInfo> ListEachInfo){
        mListEachInfo = ListEachInfo;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        holder = new ViewHolder();
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_eachone, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_weight = (TextView) convertView.findViewById(R.id.tv_weight);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tv_name.setText("蔬菜:" + String.valueOf(mListEachInfo.get(position).name));
        holder.tv_weight.setText("重量:" + String.valueOf(mListEachInfo.get(position).weight) + "斤");
        holder.tv_price.setText("单价:" + String.valueOf(mListEachInfo.get(position).price) + "￥");

        return convertView;
    }
    @Override
    public  Object getItem(int i){
        return mListEachInfo.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public int getCount(){
        return mListEachInfo.size();
    }


    class ViewHolder{
        TextView tv_name;
        TextView tv_weight;
        TextView tv_price;
    }
}
