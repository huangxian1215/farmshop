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

public class KeyValueEditAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private int mLayoutId;
    private ArrayList<KeyValueEditItem> mItemList;
    private int mBackground;



    public KeyValueEditAdapter(Context context, int layout_id, ArrayList<KeyValueEditItem> item_list, int background){
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
            holder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item_key_value);
            holder.edt_name = (EditText) convertView.findViewById(R.id.key_edit);
            holder.rg_admit = (RadioGroup) convertView.findViewById(R.id.rg_admit);
            holder.rb_txt = (RadioButton) convertView.findViewById(R.id.rb_txt);
            holder.rb_num = (RadioButton) convertView.findViewById(R.id.rb_num);
            holder.rb_bool = (RadioButton) convertView.findViewById(R.id.rb_bool);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        KeyValueEditItem item = mItemList.get(position);
        holder.ll_item.setBackgroundColor(mBackground);
        holder.edt_name.setText(item.title);
        holder.edt_name.setText(item.title);
        holder.rg_admit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_txt:
                        mItemList.get(position).valueType = "txt";
                        break;
                    case R.id.rb_num:
                        mItemList.get(position).valueType = "num";
                        break;
                    case R.id.rb_bool:
                        mItemList.get(position).valueType = "buer";
                        break;
                }
            }
        });

        holder.edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mItemList.get(position).title = s.toString();
            }
        });
        return convertView;
    }


    class ViewHolder{
        LinearLayout ll_item;
        EditText edt_name;
        RadioGroup rg_admit;
        RadioButton rb_txt;
        RadioButton rb_num;
        RadioButton rb_bool;
    }



}
