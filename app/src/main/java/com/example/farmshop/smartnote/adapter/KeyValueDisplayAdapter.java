package com.example.farmshop.smartnote.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.example.farmshop.R;
import com.example.farmshop.smartnote.bean.KeyValueEditItem;
import java.util.ArrayList;

public class KeyValueDisplayAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private int mLayoutId;
    private ArrayList<KeyValueEditItem> mItemList;
    private int mBackground;



    public KeyValueDisplayAdapter(Context context, int layout_id, ArrayList<KeyValueEditItem> item_list, int background){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mLayoutId = layout_id;
        mItemList = item_list;
        mBackground = background;
    }
    @Override
    public int getCount() {
        return mItemList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(mLayoutId, null);
            holder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item_key_value_dis);
            holder.dis_name = (TextView) convertView.findViewById(R.id.key_name_dis);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        KeyValueEditItem item = mItemList.get(position);
        holder.ll_item.setBackgroundColor(mBackground);
        holder.dis_name.setText(item.title);
        return convertView;
    }


    class ViewHolder{
        LinearLayout ll_item;
        TextView dis_name;
    }



}
