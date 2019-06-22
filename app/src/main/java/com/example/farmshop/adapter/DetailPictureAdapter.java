package com.example.farmshop.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.farmshop.R;

import java.io.File;
import java.util.ArrayList;

public class DetailPictureAdapter extends BaseAdapter {
    private static final String TAG = "DetailPictureAdapter";
    private LayoutInflater mInflater;
    private ArrayList<String> mItemList;
    private Context mContext;

    public DetailPictureAdapter(Context context, ArrayList<String> item_list){

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItemList = item_list;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ViewHolder1 holder1 = null;
        int layoutid = 0;
        if(position == 0){
            layoutid = R.layout.item_textview;
            holder1 = new ViewHolder1();
        }else{
            layoutid = R.layout.item_imgview;
            holder = new ViewHolder();
        }
        if(convertView == null) {
//            holder = new ViewHolder();
            convertView = mInflater.inflate(layoutid, null);
            if(position == 0){
                holder1.tv_desc = (TextView) convertView.findViewById(R.id.tv_textview);
                convertView.setTag(holder1);
            }else{
                holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_img1);
                convertView.setTag(holder);
            }
        } else {
            if(position == 0){
                holder1 = (ViewHolder1) convertView.getTag();
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
        }
        if(position == 0){
            holder1.tv_desc.setText(mItemList.get(position));
        }else {
            Glide.with(mContext).load(mItemList.get(position)).into(holder.iv_pic);
        }
        return convertView;
    }

    @Override
    public Object getItem(int arg0) {
        return mItemList.get(arg0);
    }
    @Override
    public long getItemId(int arg0) {
        return arg0;
    }
    @Override
    public int getCount() {
        return mItemList.size();
    }
    @Override
    public int getItemViewType(int position) {
        int p = position;
        if (p == 0)
            return 0;
        else
            return 1;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    final class ViewHolder{
        ImageView iv_pic;
    }

    class  ViewHolder1{
        TextView tv_desc;
    }
}

