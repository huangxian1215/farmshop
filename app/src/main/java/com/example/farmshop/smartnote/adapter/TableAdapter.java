package com.example.farmshop.smartnote.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmshop.R;
import com.example.farmshop.smartnote.TableDetailActivity;
import com.example.farmshop.smartnote.bean.TableItem;
import com.example.farmshop.smartnote.widget.RecyclerExtras.OnItemClickListener;
import java.util.ArrayList;

//OnItemClickListener,OnItemLongClickListene
public class TableAdapter extends BaseAdapter implements OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private int mLayoutId;
    private ArrayList<TableItem> mTableList;
    private int mBackground;

    public TableAdapter(Context context, int layout_id, ArrayList<TableItem> table_list, int background){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mLayoutId = layout_id;
        mTableList = table_list;
        mBackground = background;
    }
    @Override
    public int getCount() {
        return mTableList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mTableList.get(arg0);
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
            holder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            holder.tb_name = (TextView) convertView.findViewById(R.id.tb_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TableItem table = mTableList.get(position);
        //holder.ll_item.setBackgroundColor(mBackground);
        holder.tb_name.setText(table.name);
        holder.ll_item.setId(table.num);

        holder.ll_item.setOnClickListener(this);
        return convertView;
    }

    public final class ViewHolder{
        private LinearLayout ll_item;
        public TextView tb_name;
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        String desc = String.format("您点击了第%d个table，它的名字是%s", position + 1,
//                mTableList.get(position).name);
//        Toast.makeText(mContext, desc, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        String desc = String.format("您长按了第%d个行星，它的名字是%s", position + 1,
//                mTableList.get(position).name);
//        Toast.makeText(mContext, desc, Toast.LENGTH_LONG).show();
//        return false;
//    }

    @Override
    public void onClick(View v){
        if (mOnItemClickListener != null) {
            Log.d("tabadapter", String.valueOf(v.getId()));
            mOnItemClickListener.onItemClick(v, v.getId());
        }

    }

//    @Override
//    public boolean onLongClick(View v) {
//        return true;
//    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


}
