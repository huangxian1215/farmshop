package com.example.farmshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.bean.VegetableInfo;
import com.example.farmshop.util.VirtureUtil.onClickItemListener;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class VegetableItemAdapter extends RecyclerView.Adapter<VegetableItemAdapter.ViewHolder> implements OnClickListener {

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
        viewHolder.setTag(mcount++);
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
        String picUrl = data.get(position).pictureurl;
        if(new File(picUrl).exists()){
            Glide.with(mContext).load(data.get(position).pictureurl).into(holder.iv_img);
        }else{
            String downUrl = MainApplication.getInstance().httpUrl + data.get(position).pictureDownUrl;
            downLoadPic(downUrl,picUrl,holder.iv_img);
        }
        holder.rlMain.setId(holder.tag);
        holder.rlMain.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v){
        if(mClickListener != null){
            mClickListener.onItemClick(v, v.getId());
        }
    }

    public void downLoadPic(String loadurl, final String disurl, final ImageView img){
        new AsyncTask<String, Integer, String>(){
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL link = new URL(params[0]);
                    HttpURLConnection con = (HttpURLConnection) link.openConnection();
                    int code = con.getResponseCode();
                    if (code == 200) {
                        //获取下载总大小
                        int len = con.getContentLength();
                        RandomAccessFile rf = new RandomAccessFile(params[1], "rw");
                        rf.setLength(len);
                        byte[] buf = new byte[1024];
                        //当次读取的数量
                        int num;
                        //当前下载的量
                        int count = 0;
                        InputStream in = con.getInputStream();
                        while ((num = in.read(buf)) != -1) {
                            rf.write(buf, 0, num);
                            count += num;
                        }
                        rf.close();
                        in.close();
                    }
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String result) {
                Glide.with(mContext).load(disurl).into(img);
            }
        }.execute(loadurl, disurl);
    }

    private onClickItemListener mClickListener;
    public void setOnClickItemListener(onClickItemListener listener){
        mClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlMain;
        TextView tv_name;
        TextView tv_desc;
        TextView tv_price;
        TextView tv_hasstore;
        TextView tv_timetoeat;
        ImageView iv_img;
        int tag = -1;

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

        public void setTag(int info){
            tag = info;
        }
    }
}
