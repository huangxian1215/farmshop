package com.example.farmshop.music.httpTask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.farmshop.music.http.HttpRequestUtil;
import com.example.farmshop.music.tool.HttpReqData;
import com.example.farmshop.music.tool.HttpRespData;

public class SearchMusicTask extends AsyncTask<String, Integer, String>{
    private String mSearchName = "";
    private final static String TAG = "SearchMusicTask";
    public SearchMusicTask(String searchName, String time){
        super();
        mSearchName = "http://c.y.qq.com/soso/fcgi-bin/client_search_cp?ct=24&qqmusic_ver=1298&new_json=1&remoteplace=txt.yqq.center&t=0&aggr=1&cr=1&catZhida=1&lossless=0&flag_qc=0&p=1&n=20&w="+ searchName +"&&jsonpCallback=searchCallbacksong2020&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0";
    }

    @Override
    protected String doInBackground(String... params){

        HttpReqData req_data = new HttpReqData(mSearchName);
        HttpRespData resp_data = HttpRequestUtil.getData(req_data);
        Log.d(TAG, "return json = " + resp_data.content);

        String dataStr = "";
        if (resp_data.err_msg.length() <= 0) {
            //查询歌曲
            dataStr = resp_data.content;
        }
        return dataStr;
    }

    @Override
    protected void onPostExecute(String info){
        mListener.onGetHttpInfo(info);
    }

    private OnGetSearchInfoListener mListener;
    public void setOnSearchMusicListener(OnGetSearchInfoListener listener){
        mListener = listener;
    }
    public static interface OnGetSearchInfoListener{
        public abstract void onGetHttpInfo(String info);
    }
}

