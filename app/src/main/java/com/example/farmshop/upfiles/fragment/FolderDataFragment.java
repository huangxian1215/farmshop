package com.example.farmshop.upfiles.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.farmshop.R;
import com.example.farmshop.upfiles.adapter.FolderDataRecycleAdapter;
import com.example.farmshop.upfiles.widget.RecyclerExtras.OnItemLongClickListener;

import com.example.farmshop.upfiles.model.FileInfo;

import java.util.List;

/**
 * Created by yis on 2018/4/17.
 */

public class FolderDataFragment extends Fragment implements OnItemLongClickListener {

    private RecyclerView rvDoc;
    public FolderDataRecycleAdapter mpptListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doc, container, false);
        rvDoc = rootView.findViewById(R.id.rv_doc);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        Bundle bundle = this.getArguments();

        List<FileInfo> data = bundle.getParcelableArrayList("file_data");
        boolean isImage = bundle.getBoolean("is_image");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //设置RecyclerView 布局
        rvDoc.setLayoutManager(linearLayoutManager);
        FolderDataRecycleAdapter mpptListAdapter = new FolderDataRecycleAdapter(getActivity(), data, isImage);
        mpptListAdapter.setOnItemClickListener(this);
        rvDoc.setAdapter(mpptListAdapter);
    }

    @Override
    public void onItemLongClick(View view, int position, String fileurl){
        String geturl = fileurl;
        getActivity().finish();
    }
}
