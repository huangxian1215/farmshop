package com.example.farmshop.fragment;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.bean.ByteData;
import com.example.farmshop.farmshop;
import com.example.farmshop.iflytek.MySpeakOut;
import com.example.farmshop.upfiles.utils.PackProtoUtil;
import com.example.farmshop.util.VirtureUtil.onPlayVoiceListener;
import com.example.farmshop.util.VirtureUtil.onGetNetDataListener;
import com.google.protobuf.Any;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CommunityFragment extends Fragment implements OnClickListener ,onGetNetDataListener ,onPlayVoiceListener {
    private RecyclerView rvDoc;
    private TextView tv_chat;
    private EditText et_message;
    private TextView tv_play;
    private TextView tv_send;

    private ArrayList<String> messageList = new ArrayList<>();
    private MySpeakOut mSpk;
    private Boolean isPlay = false;

    private String newMessage = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
        rvDoc = rootView.findViewById(R.id.rv_doc);
        tv_chat = (TextView)rootView.findViewById(R.id.tv_chat);
        et_message = rootView.findViewById(R.id.et_message);
        tv_play = rootView.findViewById(R.id.tv_play);
        tv_play.setOnClickListener(this);
        rootView.findViewById(R.id.tv_send).setOnClickListener(this);
        MainApplication.getInstance().mTransmit.addOnNetListener("CommunityFragment", this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private Handler strhd = new Handler();
    private Runnable strRb = new Runnable() {
        @Override
        public void run() {
            if(messageList.size() > 0){
                mSpk.startSpeakOut(messageList.get(0));
            }else{
                strhd.postDelayed(strRb, 5000);
            }
        }
    };
    private Runnable disRb = new Runnable() {
        @Override
        public void run() {
            tv_chat.setText(newMessage);
        }
    };

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.tv_play){
            if(isPlay){
                mSpk.pauseSpeak();
            }else{
                if(messageList.size() != 0){
                    mSpk.startSpeakOut(messageList.get(0));
                }
            }
        }

        if(v.getId() == R.id.tv_send){
            farmshop.baseType.Builder sendmessage = farmshop.baseType.newBuilder().setType(farmshop.MsgId.SEND_MESSAGE_REQ).setSessionId(MainApplication.getInstance().mSessionId);
            farmshop.SendMessageRequest req = farmshop.SendMessageRequest.newBuilder().setWords(et_message.getText().toString()).build();
            sendmessage.addObject(Any.pack(req));
            PackProtoUtil.packSend(sendmessage);
            et_message.setText("");
        }
    }

    @Override
    public void onGetNetData(Object message) {
            farmshop.baseType data = (farmshop.baseType) message;
            try {
                Any any = data.getObject(0);
                farmshop.SendMessageResponse resp = farmshop.SendMessageResponse.parseFrom(any.getValue());
                if (resp.getResult() == 0) {
                    newMessage +="\n" + resp.getWords();
                    strhd.postDelayed(disRb, 100);
                    messageList.add(resp.getWords());
                } else {

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void finishSpeak(){
        messageList.remove(0);
        if(messageList.size() != 0){
            strhd.postDelayed(strRb, 2000);
        }else{
            strhd.postDelayed(strRb, 5000);
        }
    }

    private void initData() {
        mSpk = new MySpeakOut(getActivity());
        mSpk.initParam();
        mSpk.setPlayVoiceListener(this);

    }

}
