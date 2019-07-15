package com.example.farmshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.farmshop.R;
import com.example.farmshop.bean.QueryInfo;
import com.example.farmshop.util.VirtureUtil.onClickItemListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QueryOrderAdapter extends BaseAdapter implements OnClickListener {
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
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_queryorder, null);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_id = (TextView) convertView.findViewById(R.id.tv_ordernum);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
            holder.lv_list = (ListView) convertView.findViewById(R.id.lv_eachone);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(mListQueryInfo.get(position).time*1000);
        String day = simpleDateFormat.format(date);
        holder.tv_time.setText("时间:" + day);
        holder.tv_id.setText("订单号:" + String.valueOf(mListQueryInfo.get(position).id));
        holder.tv_state.setText("状态:" + String.valueOf(mListQueryInfo.get(position).state));
        holder.tv_type.setText("类别:" + String.valueOf(mListQueryInfo.get(position).type));
        holder.tv_amount.setText("金额:" + String.valueOf(mListQueryInfo.get(position).amount) + "￥");

        holder.tv_message.setText("留言:" + String.valueOf(mListQueryInfo.get(position).message));

        if(mListQueryInfo.get(position).state == 1){
            holder.tv_delete.setText("订单已取消" );
            holder.tv_delete.setTextColor(Color.GRAY);
        }else{
            holder.tv_delete.setOnClickListener(this);
            holder.tv_delete.setId(position);
        }
        if(holder.lv_list.getTag() == null){
            holder.lv_list.setTag("has");
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)holder.lv_list.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
            linearParams.height = linearParams.height * mListQueryInfo.get(position).list.size();// 控件的宽强制设成30  //linearParams.height
            holder.lv_list.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        }
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

    @Override
    public void onClick(View v){
        if(mClickListener != null){
            mClickListener.onItemClick(v, v.getId());
        }
    }

    private onClickItemListener mClickListener;
    public void setOnClickItemListener(onClickItemListener listener){
        mClickListener = listener;
    }
    class ViewHolder{
        RelativeLayout rm_main;
        TextView tv_time;
        TextView tv_id;
        TextView tv_state;
        TextView tv_type;
        TextView tv_amount;
        TextView tv_delete;
        TextView tv_message;
        ListView lv_list;
    }
}
