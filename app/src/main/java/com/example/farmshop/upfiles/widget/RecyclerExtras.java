package com.example.farmshop.upfiles.widget;
import android.view.View;
public class RecyclerExtras {

    public interface OnItemClickListener {
        void onItemClick(View view, int position, String fileurl);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position, String fileurl);
    }

    public interface OnItemDeleteClickListener {
        void onItemDeleteClick(View view, int position);
    }

}
