package com.example.farmshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.farmshop.R;
import com.example.farmshop.activity.UserDetailActivity;
import com.example.farmshop.upfiles.activity.UpFileMainActivity;

public class MyselfFragment extends Fragment implements OnClickListener{
    private Button bt_upfile;
    private TextView tv_edtuserinfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myself, container, false);
        bt_upfile = rootView.findViewById(R.id.btn_upfile);
        tv_edtuserinfo = rootView.findViewById(R.id.tv_editUserinfo);
        bt_upfile.setOnClickListener(this);
        tv_edtuserinfo.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onClick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.tv_editUserinfo:
                intent = new Intent(getActivity(), UserDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_upfile:
                intent = new Intent(getActivity(), UpFileMainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initData() {

    }
}
