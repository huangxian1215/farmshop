package com.example.farmshop.smartnote;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.farmshop.R;
import com.example.farmshop.smartnote.adapter.KeyValueDisplayAdapter;
import com.example.farmshop.smartnote.adapter.LinearDynamicAdapter;
import com.example.farmshop.smartnote.bean.KeyValueEditItem;
import com.example.farmshop.smartnote.bean.TableItem;
import com.example.farmshop.smartnote.database.UserDBHelper;
import com.example.farmshop.smartnote.dialog.AddItem;
import com.example.farmshop.smartnote.util.JsonDataTtransfer;

import com.example.farmshop.smartnote.widget.RecyclerExtras.OnItemDeleteClickListener;
import com.example.farmshop.smartnote.widget.SpacesItemDecoration;
import com.example.farmshop.smartnote.widget.RecyclerExtras.OnItemClickListener;
import com.example.farmshop.smartnote.widget.RecyclerExtras.OnItemLongClickListener;
import java.util.ArrayList;

public class TableDetailActivity extends AppCompatActivity  implements OnClickListener
        ,OnItemClickListener, OnItemLongClickListener, OnItemDeleteClickListener{
//,OnItemClickListener, OnItemLongClickListener, OnItemDeleteClickListener

    private String mTabName = "";
    private String mTabRealName = "";
    private String mDesc = "";
    private ArrayList<String> mDescList;
    private TextView tv_detail_item;
    private LinearLayout ll_detail;
    private ArrayList<KeyValueEditItem> mkeyValueItemList;
    private KeyValueDisplayAdapter madapter;

    private ArrayList<String> mtitles;
    private ArrayList<String> mtypes;
    private ArrayList<String> mData;
    private UserDBHelper mHelper;

    private RecyclerView rv_dynamic;
    private LinearDynamicAdapter mAdapter;

    //消息ID
    private static final int ADD_REQUESTCODE = 1;//添加
    private static final int ADD_RESULTCODE = 1;
    private static final int DELETE_REQUESTCODE = 2;//删除
    private static final int DELETE_RESULTSTCODE = 2;
    private static final int MODIFY_REQUESTCODE = 3;//修改
    private static final int MODIFY_RESULTSTCODE = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabledetail);
        tv_detail_item = findViewById(R.id.tv_detail_1);
        ll_detail = findViewById(R.id.ll_detail);
        //得到上层传过来的数据
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mTabName = bundle.getString("name", "");
        mDesc = bundle.getString("desc", "");
        mDescList = JsonDataTtransfer.anaylse(mDesc);
        //初始化标题栏
        initTitle();
        //从数据库获取数据
        getDataFromDB();
        //设置adapter，处理和填充数据
        rv_dynamic = (RecyclerView) findViewById(R.id.rv_dynamic);

        fillData();

        findViewById(R.id.btn_add_one).setOnClickListener(this);//
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    public void initTitle() {
        //获取数据库的表名字
        mHelper = UserDBHelper.getInstance(this, 2);
        mTabRealName = "tab" + mHelper.queryByname(mTabName, "tabInfo").dbNum;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(45,
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        layoutParams.setMargins(0, 0, 10, 0);
            mtitles = new ArrayList<String>();
            mtypes  = new ArrayList<String>();
        for(int i = 0; i < mDescList.size(); i = i+2){
            mtitles.add(mDescList.get(i));
            mtypes.add(mDescList.get(i+1));
            TextView textView = new TextView(this);
            //textView = tv_detail_item;
            textView.setGravity(Gravity.CENTER);
            textView.setText(mDescList.get(i));
            textView.setLayoutParams(layoutParams);
            ll_detail.addView(textView);
        }
    }

    public void fillData(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayout.VERTICAL);
        rv_dynamic.setLayoutManager(manager);
        mAdapter = new LinearDynamicAdapter(this, mData, mtypes);
//        mAdapter.setOnItemClickListener(this);
//        mAdapter.setOnItemLongClickListener(this);
//        mAdapter.setOnItemDeleteClickListener(this);
        rv_dynamic.setAdapter(mAdapter);
        rv_dynamic.setItemAnimator(new DefaultItemAnimator());
        rv_dynamic.addItemDecoration(new SpacesItemDecoration(1));
    }

    public void getDataFromDB(){
        mData = mHelper.queryTabByDesc("1=1", mTabRealName, mtitles.size());
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_add_one:
                editItem();
                break;
            case R.id.btn_back:
                finish();
        }
    }

    public void editItem(){
        Intent intent = new Intent(this, AddItem.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("titles", mtitles);
        bundle.putStringArrayList("types", mtypes);
        bundle.putString("names", mTabRealName);
        intent.putExtras(bundle);
        startActivityForResult(intent, ADD_RESULTCODE);
//        startActivityForResult(intent, DELETE_RESULTSTCODE);
//        startActivityForResult(intent, MODIFY_RESULTSTCODE);
    }

    public void writeItemToSQLite(ArrayList<String> values){
        String [] arr = values.toArray(new String[0]);
        mHelper.insert(arr, mtypes.toArray(new String[0]), mTabRealName);
    }

    public void deleteItemToSQLite(ArrayList<String> values){
        String [] arr = values.toArray(new String[0]);
        if(arr[0] == "") return;
        String condition = "_id = " + arr[0];
        int count = mHelper.delete(condition, mTabRealName);
        if(count < 1){
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void modifyItemToSQLite(ArrayList<String> values){
        String [] arr = values.toArray(new String[0]);
        mHelper.update(arr, mtypes.toArray(new String[0]), mTabRealName);
    }

    public void refreshTab(){
        getDataFromDB();
        fillData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = UserDBHelper.getInstance(this, 2);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case ADD_REQUESTCODE:
                if(resultCode == ADD_RESULTCODE){//添加
                    if(data != null){
//                        ArrayList<String> values = (ArrayList<String>) data.getSerializableExtra("values");
                        ArrayList<String> values = data.getStringArrayListExtra("values");
                        writeItemToSQLite(values);
                        refreshTab();
                    }else{
                        Toast.makeText(this, "空信息添加", Toast.LENGTH_SHORT).show();
                    }
                }
                if(resultCode == DELETE_RESULTSTCODE){
                    if(data != null){
                        ArrayList<String> values = data.getStringArrayListExtra("values");
                        deleteItemToSQLite(values);
                        refreshTab();
                    }else{

                    }
                }
                if(resultCode == MODIFY_RESULTSTCODE){
                    if(data != null){
                        ArrayList<String> values = data.getStringArrayListExtra("values");
                        modifyItemToSQLite(values);
                        refreshTab();
                    }else{

                    }
                }
                break;
            case DELETE_REQUESTCODE:

                break;
            case MODIFY_REQUESTCODE:

                break;
        }
    }


    @Override
    public void onItemLongClick(View view, int position) {
//        GoodsInfo item = mPublicArray.get(position);
//        item.bPressed = !item.bPressed;
//        mPublicArray.set(position, item);
//        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void onItemClick(View view, int position) {
//        String desc = String.format("您点击了第%d项，标题是%s", position + 1,
//                mPublicArray.get(position).title);
//        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemDeleteClick(View view, int position) {
//        mPublicArray.remove(position);
//        mAdapter.notifyItemRemoved(position);
    }
}
