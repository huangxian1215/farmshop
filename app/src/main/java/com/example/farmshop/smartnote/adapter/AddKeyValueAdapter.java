package com.example.farmshop.smartnote.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.example.farmshop.R;
import com.example.farmshop.smartnote.bean.KeyValueEditItem;
import com.example.farmshop.smartnote.database.UserDBHelper;

import java.util.ArrayList;

import android.util.Log;

import static android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;

public class AddKeyValueAdapter extends BaseAdapter {
    private static final String TAG = "AddKeyValueAdapter";
    private LayoutInflater mInflater;
    private Context mContext;
    private int mLayoutId;
    private ArrayList<KeyValueEditItem> mItemList;
    private int mBackground;
    private String mrealName;
    private ArrayList<String> madata = null;
    private String sfg = "";

    public AddKeyValueAdapter(Context context, int layout_id, ArrayList<KeyValueEditItem> item_list, int background, String name){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mLayoutId = layout_id;
        mItemList = item_list;
        mBackground = background;
        mrealName = name;
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
            holder.ll_title = (TextView) convertView.findViewById(R.id.key_name);
            holder.edt_name = (EditText) convertView.findViewById(R.id.key_edit);
            holder.rg_admit = (RadioGroup) convertView.findViewById(R.id.rg_choose);
            holder.rb_true = (RadioButton) convertView.findViewById(R.id.rb_true);
            holder.rb_false = (RadioButton) convertView.findViewById(R.id.rb_false);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        KeyValueEditItem item = mItemList.get(position);
        holder.ll_item.setBackgroundColor(mBackground);
        holder.ll_title.setText(item.title);

        switch (item.valueType){
            case "txt":
                holder.rg_admit.setVisibility(View.GONE);
                holder.edt_name.setVisibility(View.VISIBLE);
                break;
            case "num":
                holder.rg_admit.setVisibility(View.GONE);
                holder.edt_name.setVisibility(View.VISIBLE);
                //输入数字的保护
                holder.edt_name.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                break;
            case "buer":
                holder.rg_admit.setVisibility(View.VISIBLE);
                holder.edt_name.setVisibility(View.GONE);
                mItemList.get(position).value = "true";
                break;
        }

        holder.rg_admit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_true:
                        mItemList.get(position).value = "true";
                        break;
                    case R.id.rb_false:
                        mItemList.get(position).value = "false";
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
                mItemList.get(position).value = s.toString();
                Log.d(TAG, "mItemListlast" +String.valueOf(position) +"---"+ mItemList.get(position).value);
                if(position == 0){
                    String msgId = s.toString();
                    //执行数据库操作填充下面的
                    UserDBHelper Helper = UserDBHelper.getmHelper();
                    if(Helper != null && msgId.length() > 0){
                        //queryTabByDesc(String condition, String tabname, int num)
                        String condition = "_id = " + msgId;
                        madata = Helper.queryTabByDesc(condition, mrealName, getCount() - 1);
                        if(madata.size() == 0) return;
                        for(int n = 0; n < getCount()-1; n++){
                            Log.d(TAG, "mItemList: " + mItemList.get(n+1).value);
                            if(mItemList.get(n+1).valueType.equals("buer")){
                                if(madata.get(n+2).equals("1")){
                                    mItemList.get(n+1).value = "true";
                                }else{
                                    mItemList.get(n+1).value = "false";
                                }
                            }else{
                                mItemList.get(n+1).value = madata.get(n+2);
                            }
                        }
                    }
                }
            }
        });
        //第一行为 _id 修改和删除时必填
        if(position == 0){
            holder.edt_name.setHint("修改和删除时填写");
            holder.edt_name.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        }
        return convertView;
    }


    class ViewHolder{
        LinearLayout ll_item;
        TextView ll_title;
        EditText edt_name;
        RadioGroup rg_admit;
        RadioButton rb_true;
        RadioButton rb_false;
    }



}
