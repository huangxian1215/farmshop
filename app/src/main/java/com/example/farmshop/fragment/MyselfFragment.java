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

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.activity.BasketActivity;
import com.example.farmshop.activity.QueryOrdersActivity;
import com.example.farmshop.activity.UserDetailActivity;
import com.example.farmshop.farmshop;
import com.example.farmshop.iflytek.MyVoiceSettingActivity;
import com.example.farmshop.iflytek.VoiceSettingsActivity;
import com.example.farmshop.music.MusicMainActivity;
import com.example.farmshop.smartnote.SmtMainActivity;
import com.example.farmshop.upfiles.activity.UpFileMainActivity;
import com.example.farmshop.upfiles.utils.PackProtoUtil;
import com.google.protobuf.Any;

public class MyselfFragment extends Fragment implements OnClickListener{
    private Button bt_upfile;
    private TextView tv_edtuserinfo;
    private TextView tv_basket;
    private TextView tv_voiceStyleSet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myself, container, false);
        bt_upfile = rootView.findViewById(R.id.btn_upfile);
        tv_edtuserinfo = rootView.findViewById(R.id.tv_editUserinfo);
        tv_basket  = rootView.findViewById(R.id.tv_basket);
        tv_voiceStyleSet  = rootView.findViewById(R.id.tv_voiceStyleSet);
        bt_upfile.setOnClickListener(this);
        tv_basket.setOnClickListener(this);
        tv_voiceStyleSet.setOnClickListener(this);
        tv_edtuserinfo.setOnClickListener(this);
        rootView.findViewById(R.id.tv_queryOrder).setOnClickListener(this);
        rootView.findViewById(R.id.tv_smartnote).setOnClickListener(this);
        rootView.findViewById(R.id.tv_simpleMusic).setOnClickListener(this);
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
                break;
            case R.id.btn_upfile:
                intent = new Intent(getActivity(), UpFileMainActivity.class);
                break;
            case R.id.tv_basket:
                intent = new Intent(getActivity(), BasketActivity.class);
                break;
            case R.id.tv_voiceStyleSet:
                intent = new Intent(getActivity(), MyVoiceSettingActivity.class);
                break;
            case R.id.tv_queryOrder:
                intent = new Intent(getActivity(), QueryOrdersActivity.class);
                break;
            case R.id.tv_smartnote:
                intent = new Intent(getActivity(), SmtMainActivity.class);
                break;
            case R.id.tv_simpleMusic:
                intent = new Intent(getActivity(), MusicMainActivity.class);
                break;
        }
        startActivity(intent);
    }

    private void initData() {

    }
}
