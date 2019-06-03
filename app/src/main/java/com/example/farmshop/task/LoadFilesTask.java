package com.example.farmshop.task;

import com.example.farmshop.task.Downloader.OnDownloadListener;
import android.os.AsyncTask;

public class LoadFilesTask extends AsyncTask<String, Integer, String> implements OnDownloadListener {
    public int lenght;
    public String savePath;

    public LoadFilesTask(){

    }
    @Override
    protected String doInBackground(String... params){
        new Downloader(this).download(params[0], params[1], params[2]);
        return params[2];
    }

    @Override
    public void onStart(int size, String path) {
        lenght = size;
        this.savePath = path;
    }

    @Override
    public void onDownloading(int currentSize) {
        //更新进度条
        int currentRate = 100 * currentSize / lenght;
        //publishProgress(currentRate);
    }

    @Override
    public void onDownloadFinish(String type){
        if(type.equals("mp3")){
        }
        if(type.equals("jpg")){//显示专辑图片
            //setImageView(mPicPath);
            //立即加载会失败
            //mHandler.postDelayed(r, 1000);
        }
    }

    @Override
    protected void onPostExecute(String info){
        mListener.onGetFile(info);
    }

    private OnGetFileListener mListener;
    public void setOnGetFileListener(OnGetFileListener listener){
        mListener = listener;
    }
    public static interface OnGetFileListener{
        public abstract void onGetFile(String info);
    }

}
