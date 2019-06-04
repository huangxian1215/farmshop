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

import java.util.ArrayList;
import java.util.List;

public class VegetableGardenFragment extends Fragment implements OnGetFileListener {
    private RecyclerView rvDoc;
    private MainApplication app;
    LoadFilesTask mloadconfig;
    private ArrayList<String> mDownTaskList;
    private int mDownCount = 0;
    private List<VegetableInfo> mData;
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
        getPicLoadList();
        startDownLoadTask();
        initData();
    }

    private void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //设置RecyclerView 布局
        rvDoc.setLayoutManager(linearLayoutManager);

        //test two pics
        String path = MainApplication.getInstance().savePath;
        mData = new ArrayList<>();
        mpptListAdapter = new VegetableItemAdapter(getActivity(), mData);
        rvDoc.setAdapter(mpptListAdapter);
    }

    private void getPicLoadList(){
        String savePath = app.savePath + "pictureConfig.json";
        String strJson = JsonUtil.getJson(savePath);
        ArrayList<String>  config = new ArrayList<>();
        mDownTaskList = new ArrayList<>();
        config = JsonUtil.objStringToArrayList(strJson);
        for(int i = 0; i < config.size(); i++){
            ArrayList<String>  every = new ArrayList<>();
            every = JsonUtil.arrayStringToArrayList(config.get(i));
            for(int n = 0; n < every.size(); n++){
                mDownTaskList.add(every.get(n));
            }
        }
    }

    private void startDownLoadTask(){
        if(mDownTaskList.size() > 0 && mDownCount == 0){
            mloadconfig = new LoadFilesTask();
            mloadconfig.setOnGetFileListener(this);
            String downUrl = app.Url + mDownTaskList.get(0);
            String savePath = app.savePath + MyUtil.getFileName(mDownTaskList.get(0)) + ".jpg";
            mSavePath = savePath;
            mloadconfig.execute(downUrl,savePath, "jpg");
            mDownCount++;
        }
    }

    @Override
    public void onGetFile(String info){
        VegetableInfo vg = new VegetableInfo();
        vg.picPath = mSavePath;
        vg.name = MyUtil.getFileName(mDownTaskList.get(mDownCount - 1));
        vg.desc = "test pic";
        vg.price = 2.0;
        mData.add(vg);
        mpptListAdapter.notifyDataSetChanged();
        if(mDownCount < mDownTaskList.size()){
            mloadconfig = new LoadFilesTask();
            mloadconfig.setOnGetFileListener(this);
            String downUrl = app.Url + mDownTaskList.get(mDownCount);
            String savePath = app.savePath + MyUtil.getFileName(mDownTaskList.get(mDownCount)) + ".jpg";
            mSavePath = savePath;
            mloadconfig.execute(downUrl,savePath, "jpg");
            mDownCount++;
        }
    }
}
