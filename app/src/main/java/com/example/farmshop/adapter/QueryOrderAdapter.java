package com.example.farmshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.farmshop.R;
import com.example.farmshop.bean.EachOneInfo;
import com.example.farmshop.bean.QueryInfo;

import java.util.ArrayList;

public class QueryOrderAdapter extends BaseAdapter {
    private static final String TAG = "QueryOrderAdapter";
    private ArrayList<QueryInfo> mListQueryInfo;
    private LayoutInflater mInflater;


    public QueryOrderAdapter(Context context,  ArrayList<QueryInfo> listQueryInfo){
        mListQueryInfo = listQueryInfo;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        holder = new ViewHolder();
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_queryorder, null);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_id = (TextView) convertView.findViewById(R.id.tv_ordernum);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
            holder.lv_list = (ListView) convertView.findViewById(R.id.lv_eachone);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tv_time.setText("时间:" + String.valueOf(mListQueryInfo.get(position).time));
        holder.tv_id.setText("订单号:" + String.valueOf(mListQueryInfo.get(position).id));
        holder.tv_state.setText("状态:" + String.valueOf(mListQueryInfo.get(position).state));
        holder.tv_type.setText("类别:" + String.valueOf(mListQueryInfo.get(position).type));
        holder.tv_amount.setText("金额:" + String.valueOf(mListQueryInfo.get(position).amount) + "￥");
        holder.tv_message.setText("留言:" + String.valueOf(mListQueryInfo.get(position).message));

        EachOneAdapter adapter = new EachOneAdapter(holder.lv_list.getContext(), mListQueryInfo.get(position).list);

        holder.lv_list.setAdapter(adapter);
        return convertView;
    }
    @Override
    public  Object getItem(int i){
        return mListQueryInfo.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public int getCount(){
        return mListQueryInfo.size();
    }


    class ViewHolder{
        TextView tv_time;
        TextView tv_id;
        TextView tv_state;
        TextView tv_type;
        TextView tv_amount;
        TextView tv_message;
        ListView lv_list;
    }
}
