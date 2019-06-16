package com.example.farmshop.fragment;

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
import com.example.farmshop.adapter.VegetableItemAdapter;
import com.example.farmshop.bean.VegetableInfo;
import com.example.farmshop.util.JsonUtil;
import com.example.farmshop.task.LoadFilesTask;
import com.example.farmshop.task.LoadFilesTask.OnGetFileListener;
import com.example.farmshop.util.MyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VegetableGardenFragment extends Fragment implements OnGetFileListener {
    private RecyclerView rvDoc;
    private MainApplication app;
    LoadFilesTask mloadconfig;
    private ArrayList<String> mDownTaskList;
    private int mDownCount = 0;
    //本地存在
    private List<VegetableInfo> mData;
    //需要下载
    private List<VegetableInfo> mDataDown;
    private String mSavePath = "";
    private VegetableItemAdapter mpptListAdapter;

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
        initData();
        getVegetablePicList();
        startDownLoadTask();
    }

    private void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvDoc.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();
        mDataDown = new ArrayList<>();
        mpptListAdapter = new VegetableItemAdapter(getActivity(), mData);
        rvDoc.setAdapter(mpptListAdapter);
    }

    private void getVegetablePicList(){
        String savePath = app.savePath + "vegetableConfig.json";
        String strJson = JsonUtil.getText(savePath);
        ArrayList<String>  config = new ArrayList<>();
        mDownTaskList = new ArrayList<>();
        config = JsonUtil.objStringToArrayList(strJson);
        for(int i = 0; i < config.size(); i++){
            Map<String, String> every = new HashMap<>();
            every = JsonUtil.objStringToMapList(config.get(i));
            //遍历,存在图片的先放进去
            VegetableInfo vg = new VegetableInfo();
            Boolean needdown = false;
            for(Map.Entry<String, String> entry : every.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                switch (key){
                    case "name":
                        vg.name = value;
                        break;
                    case "pictureurl":

                        String filePath = app.savePath + MyUtil.getFileName(value)+".jpg";
                        vg.pictureurl = filePath;
                        File File = new File(filePath);
                        if(!File.exists()) {
                            mDownTaskList.add(value);
                            needdown = true;
                        }
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
                }
            }

            if(needdown){
                mDataDown.add(vg);
            }else{
                mData.add(vg);
                mpptListAdapter.notifyDataSetChanged();
            }

        }
    }

    private void getPicLoadList(){
        String savePath = app.savePath + "pictureConfig.json";
        String strJson = JsonUtil.getText(savePath);
        ArrayList<String>  config = new ArrayList<>();
        mDownTaskList = new ArrayList<>();
        config = JsonUtil.objStringToArrayList(strJson);
        for(int i = 0; i < config.size(); i++){
            ArrayList<String>  every = new ArrayList<>();
            every = JsonUtil.arrayStringToArrayList(config.get(i));
            for(int n = 0; n < every.size(); n++){
                String filePath = app.savePath + MyUtil.getFileName(every.get(n))+".jpg";
                File File = new File(filePath);
                if(!File.exists()){
                    mDownTaskList.add(every.get(n));
                }else{
                    VegetableInfo vg = new VegetableInfo();
                    vg.pictureurl = filePath;
                    vg.name = MyUtil.getFileName(every.get(n));
                    vg.desc = "test pic";
                    vg.price = 2.0;
                    mData.add(vg);
                    mpptListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void startDownLoadTask(){
        if(mDownTaskList.size() > 0 && mDownCount == 0){
            mloadconfig = new LoadFilesTask();
            mloadconfig.setOnGetFileListener(this);
            String downUrl = app.httpUrl + mDownTaskList.get(0);
            String savePath = app.savePath + MyUtil.getFileName(mDownTaskList.get(0)) + ".jpg";
            mSavePath = savePath;
            mloadconfig.execute(downUrl,savePath, "jpg");
            mDownCount++;
        }
    }

    @Override
    public void onGetFile(String info){
        mData.add(mDataDown.get(mDownCount - 1));
        mpptListAdapter.notifyDataSetChanged();
        if(mDownCount < mDownTaskList.size()){
            String savePath = app.savePath + MyUtil.getFileName(mDownTaskList.get(mDownCount))+".jpg";
            mloadconfig = new LoadFilesTask();
            mloadconfig.setOnGetFileListener(this);
            String downUrl = app.httpUrl + mDownTaskList.get(mDownCount);
            mSavePath = savePath;
            mloadconfig.execute(downUrl, savePath, "jpg");

            mDownCount++;
        }
    }
}
