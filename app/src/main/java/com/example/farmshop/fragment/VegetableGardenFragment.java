package com.example.farmshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.activity.VegetableDetailActivity;
import com.example.farmshop.adapter.VegetableItemAdapter;
import com.example.farmshop.bean.VegetableInfo;
import com.example.farmshop.util.JsonUtil;
import com.example.farmshop.util.MyUtil;
import com.example.farmshop.util.VirtureUtil.onClickItemListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VegetableGardenFragment extends Fragment implements onClickItemListener {
    private RecyclerView rvDoc;
    private MainApplication app;
    private List<VegetableInfo> mData;
    private VegetableItemAdapter mVegetableListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vegetable, container, false);
        rvDoc = rootView.findViewById(R.id.rv_doc);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //图片较多时需要边下载边刷新,下载任务队列
        app = MainApplication.getInstance();
        getVegetablePicList();
        initData();
    }

    @Override
    public void onItemClick(View v, int position){
        VegetableInfo click = mData.get(position);
        Intent intent = new Intent(getActivity(), VegetableDetailActivity.class);
        intent.putExtra("click_vegetable_type", click.type);
        intent.putExtra("click_vegetable_name", click.name);
        intent.putExtra("click_vegetable_ads", click.ads);
        intent.putExtra("click_vegetable_url", click.pictureurl);
        intent.putExtra("click_vegetable_price", click.price);
        startActivity(intent);
    }

    private void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvDoc.setLayoutManager(linearLayoutManager);
        mVegetableListAdapter = new VegetableItemAdapter(getActivity(), mData);
        mVegetableListAdapter.setOnClickItemListener(this);
        rvDoc.setAdapter(mVegetableListAdapter);
    }

    private void getVegetablePicList(){
        mData = new ArrayList<>();
        String savePath = app.savePath + "vegetableConfig.json";
        String strJson = JsonUtil.getText(savePath);
        ArrayList<String>  config = new ArrayList<>();
        config = JsonUtil.objStringToArrayList(strJson);
        for(int i = 0; i < config.size(); i++){
            Map<String, String> every = new HashMap<>();
            every = JsonUtil.objStringToMapList(config.get(i));
            //遍历,存在图片的先放进去
            VegetableInfo vg = new VegetableInfo();
            for(Map.Entry<String, String> entry : every.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                switch (key){
                    case "name":
                        vg.name = value;
                        break;
                    case "type":
                        vg.type = value;
                        break;
                    case "pictureurl":
                        String filePath = app.savePath + MyUtil.getFileName(value)+".jpg";
                        vg.pictureurl = filePath;
                        vg.pictureDownUrl = value;
                        break;
                    case "timetoeat":
                        vg.timetoeat = value;
                        break;
                    case "hasstore":
                        vg.hasstore = value.equals("true");
                        break;
                    case "price":
                        vg.price = Double.parseDouble(value);
                        break;
                    case "desc":
                        vg.desc = value;
                        break;
                    case "ads":
                        vg.ads = value;
                        break;
                }
            }
            mData.add(vg);
        }
    }


}
