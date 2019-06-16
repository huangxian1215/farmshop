package com.example.farmshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.farmshop.R;
import com.example.farmshop.bean.VegetableInfo;
import com.google.protobuf.StringValue;

import java.util.List;

public class VegetableItemAdapter extends RecyclerView.Adapter<VegetableItemAdapter.ViewHolder>{

    private Context mContext;
    private List<VegetableInfo> data;
    private int mcount;

    public VegetableItemAdapter(Context mContext, List<VegetableInfo> data) {
        this.mContext = mContext;
        this.data = data;
        mcount = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vegetable, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_name.setText(data.get(position).name);
        holder.tv_desc.setText(data.get(position).desc);
        holder.tv_price.setText("单价:"+String.valueOf(data.get(position).price + "￥/斤"));
        Boolean has = data.get(position).hasstore;
        String text = has ? "有货" : "无货";
        holder.tv_hasstore.setText(text);
        if(!has){
            holder.tv_hasstore.setTextColor(Color.GRAY);
        }
        holder.tv_timetoeat.setText("产期:"+data.get(position).timetoeat);

        //封面图
        Glide.with(mContext).load(data.get(position).pictureurl).into(holder.iv_img);
        holder.rlMain.setId(mcount++);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlMain;
        TextView tv_name;
        TextView tv_desc;
        TextView tv_price;
        TextView tv_hasstore;
        TextView tv_timetoeat;
        ImageView iv_img;

        public ViewHolder(View itemView) {
            super(itemView);
            rlMain = itemView.findViewById(R.id.rl_main);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_hasstore = itemView.findViewById(R.id.tv_hasstore);
            tv_timetoeat = itemView.findViewById(R.id.tv_timetoeat);
            iv_img = itemView.findViewById(R.id.iv_img);
        }
    }
}
