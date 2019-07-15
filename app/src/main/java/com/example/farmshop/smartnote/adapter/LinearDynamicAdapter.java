package com.example.farmshop.smartnote.adapter;


import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.farmshop.R;
import com.example.farmshop.smartnote.widget.RecyclerExtras.OnItemClickListener;
import com.example.farmshop.smartnote.widget.RecyclerExtras.OnItemDeleteClickListener;
import com.example.farmshop.smartnote.widget.RecyclerExtras.OnItemLongClickListener;

public class LinearDynamicAdapter extends RecyclerView.Adapter<ViewHolder>
        implements OnClickListener, OnLongClickListener {
    private final static String TAG = "LinearDynamicAdapter";
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mData;
    private ArrayList<String> mTitles;
    private int mTitleCount;
    private int mCount;

    public LinearDynamicAdapter(Context context,ArrayList<String> data, ArrayList<String> titles) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = data;
        mTitles = titles;
        mTitleCount = titles.size();
        mCount = data.size()/(titles.size()+2);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
        View v = null;
        ViewHolder holder = null;
        v = mInflater.inflate(R.layout.item_linear, vg, false);
        holder = new ItemHolder(v);
        return holder;
    }


    private int CLICK = 0;
    private int DELETE = 1;
    @Override
    public void onClick(View v) {
        int position = v.getId();
        int type = v.getId();
        if (type == CLICK) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, position);
            }
        } else if (type == DELETE) {
            if (mOnItemDeleteClickListener != null) {
                mOnItemDeleteClickListener.onItemDeleteClick(v, position);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int position = v.getId();
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(v, position);
        }
        return true;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {
        String time = mData.get(position*(mTitleCount+2) + 1);//yyyyMMddHHmmss
        String hm = time.substring(8, 10) + ":" + time.substring(10, 12);
        String ymd = time.substring(0, 4)+ "-" +time.substring(4, 6)+ "-" +time.substring(6, 8);
        ItemHolder holder = (ItemHolder) vh;
        holder.tv_num.setText(mData.get(position*(mTitleCount+2)));//String.valueOf(position)
        holder.tv_time_hm.setText(hm);
        holder.tv_time_ymd.setText(ymd);
//        holder.tv_delete.setVisibility((item.bPressed)?View.VISIBLE:View.GONE);
//        holder.tv_delete.setId(item.id*10 + DELETE);
//        holder.tv_delete.setOnClickListener(this);
//
//        holder.ll_item.setId(item.id*10 + CLICK);
        holder.ll_item.setOnClickListener(this);
        holder.ll_item.setOnLongClickListener(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(45,
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        layoutParams.setMargins(0, 0, 10, 0);
        holder.ll_item.removeAllViews();



        Random rand = new Random();
        int colorid = getColorByid(rand.nextInt(6));
        for(int i = 0; i < mTitleCount; i++){
            TextView textView = new TextView(mContext);
            textView.setGravity(Gravity.CENTER);

            textView.setTextColor(colorid);
            if(mTitles.get(i).equals("buer")){
                String buerValue = mData.get(position*(mTitleCount+2)+i+2);
                if(buerValue.equals("1")){
                    textView.setText("是的");
                    textView.setTextColor(Color.GREEN);
                }else{
                    textView.setText("没有");
                    textView.setTextColor(Color.GRAY);
                }
            }else{
                textView.setText(mData.get(position*(mTitleCount+2)+i+2));
            }

            textView.setLayoutParams(layoutParams);
            holder.ll_item.addView(textView);
        }


    }
    public  int getColorByid(int id){
        switch (id){
            case 0:
                return Color.RED;
            case 1:
                return 0XFFFFA500;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.CYAN;
            case 4:
                return Color.BLUE;
            case 5:
                return Color.MAGENTA;
        }
        return Color.BLACK;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_item;
        public TextView tv_num;
        public TextView tv_time_hm;
        public TextView tv_time_ymd;
        public TextView tv_delete;

        public ItemHolder(View v) {
            super(v);
            ll_item = (LinearLayout) v.findViewById(R.id.ll_data_detail);
            tv_num = (TextView) v.findViewById(R.id.tv_num);
            tv_time_hm = (TextView) v.findViewById(R.id.tv_time_hm);
            tv_time_ymd = (TextView) v.findViewById(R.id.tv_time_ymd);
            tv_delete = (TextView) v.findViewById(R.id.tv_delete);
        }

    }

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private OnItemLongClickListener mOnItemLongClickListener;
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    private OnItemDeleteClickListener mOnItemDeleteClickListener;
    public void setOnItemDeleteClickListener(OnItemDeleteClickListener listener) {
        this.mOnItemDeleteClickListener = listener;
    }

}

