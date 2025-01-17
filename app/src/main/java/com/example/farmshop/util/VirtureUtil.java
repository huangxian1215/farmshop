/*
* 各个功能监听用的虚函数，可以理解为回调
* 用于各个Actitivy Listener，使用时重写方法
* */
package com.example.farmshop.util;

import android.view.View;

import com.example.farmshop.farmshop;

public class VirtureUtil {
    //网络监听用，与服务器
    public static interface onGetNetDataListener {
        public abstract void onGetNetData(Object info, farmshop.MsgId msgid);
    }
    public interface updateProgressListener{
        public abstract void updateProgress(int progress);
    }

    //列表视图点击事件
    public interface onClickItemListener{
        public abstract void onItemClick(View view, int position);
    }
    public interface onLongClickItemListener{
        public abstract void onLongClickItem(View view, int position);
    }
    public interface onDeleteItemListener{
        public abstract void onDeleteItem(View view, int position);
    }

    //对话框点击事件
    public interface onClickSureListener{
        public abstract void onClickSure(int index);
    }
    public interface onClickCancleListener{
        public abstract void onClickCancle();
    }

    //讯飞语音事件
    public interface onPlayVoiceListener{
        public abstract void finishSpeak();
    }

    //百度地图事件
    public interface onMyLocationListener{
        public abstract void getMyAllLocation(String info);
    }

    //MobPhone 短信认证事件
    public interface onResultMobPhoneListener{
        public abstract void getMessageResult(String phonenum, Boolean flag);
    }
}
