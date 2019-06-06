package com.example.farmshop.upfiles.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.bumptech.glide.Glide;
import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.upfiles.model.FileInfo;
import com.example.farmshop.upfiles.utils.FileUtil;
import com.example.farmshop.upfiles.widget.RecyclerExtras;
import com.example.farmshop.upfiles.widget.RecyclerExtras.OnItemLongClickListener;

import java.util.List;


/**
 * 使用遍历文件夹的方式
 * Created by yis on 2018/4/17.
 */

public class FolderDataRecycleAdapter extends RecyclerView.Adapter<FolderDataRecycleAdapter.ViewHolder> implements /*OnClickListener, */OnLongClickListener {

    private Context mContext;
    private List<FileInfo> data;

    private boolean isPhoto = false;
    private int mcount = 0;

    public FolderDataRecycleAdapter(Context mContext, List<FileInfo> data, boolean isPhoto) {
        this.mContext = mContext;
        this.data = data;
        this.isPhoto = isPhoto;
        mcount = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_folder_data_rv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_content.setText(data.get(position).getFileName());
        holder.tv_size.setText(FileUtil.FormetFileSize(data.get(position).getFileSize()));
        holder.tv_time.setText(data.get(position).getTime());

        //封面图
        if (isPhoto) {
            Glide.with(mContext).load(data.get(position).getFilePath()).into(holder.iv_cover);
        } else {
            Glide.with(mContext).load(FileUtil.getFileTypeImageId(mContext, data.get(position).getFilePath())).fitCenter().into(holder.iv_cover);
        }
        holder.rlMain.setId(mcount++);
        holder.rlMain.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlMain;
        TextView tv_content;
        TextView tv_size;
        TextView tv_time;
        ImageView iv_cover;

        public ViewHolder(View itemView) {
            super(itemView);
            rlMain = itemView.findViewById(R.id.rl_main);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_size = itemView.findViewById(R.id.tv_size);
            tv_time = itemView.findViewById(R.id.tv_time);
            iv_cover = itemView.findViewById(R.id.iv_cover);
        }
    }

    //文件选择
//    @Override
//    public void onClick(View v){
//        if(v != null){
//            int i = v.getId();
//            if (mOnItemClickListener != null){
//                mOnItemClickListener.onItemLongClick(v, v.getId(), data.get(i).getFilePath());
//            }
//        }
//    }

    @Override
    public  boolean onLongClick(View v){
        if(v != null){
            int i = v.getId();
            if (mOnItemClickListener != null){
                String fileurl = data.get(i).getFilePath();
                mOnItemClickListener.onItemLongClick(v, v.getId(), fileurl);
                MainApplication.getInstance().selectFileUrl = fileurl;
            }
        }
        return true;
    }

    private OnItemLongClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemLongClickListener listener) {
        this.mOnItemClickListener = listener;
    }


}
