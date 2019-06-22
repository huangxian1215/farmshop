/*
 * 对话框
 * 提示对话框，一般对话框，确定返回-1，取消返回-2
 * 单选对话框返回数组的下标
 * 多线对话框返回2的冥次方之和
 * */
package com.example.farmshop.dialog;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.farmshop.R;
import com.example.farmshop.util.VirtureUtil.onClickSureListener;

public class MyDialog {
    AlertDialog.Builder mBuilder;
    private int selectId = -1;
    private Context mContext;
    private ProgressDialog mPD;

    public MyDialog(){

    }

    public void createAlerDialog(Context context){
        mContext = context;
        mBuilder = new AlertDialog.Builder(context);
    }

    public void createProgressDialog(Context context){
        mContext = context;
        mPD = new ProgressDialog(context);
    }

    //1---提示对话框
    public void showDialog(String title, String message, String btnNameOK) {
        if(mBuilder == null) return;
        mBuilder.setIcon(R.mipmap.ic_launcher);
        mBuilder.setTitle(title);
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton(btnNameOK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickOk(selectId);
            }
        });
        mBuilder.setCancelable(true);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
    //2---一般对话框
    public void showDialog(String title, String message, String btnNameOK, String btnNameNo) {
        if(mBuilder == null) return;
        mBuilder.setIcon(R.mipmap.ic_launcher);
        mBuilder.setTitle(title);
        mBuilder.setMessage(message);
        mBuilder.setPositiveButton(btnNameOK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickOk(selectId);
            }
        });
        mBuilder.setNegativeButton(btnNameNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickOk(-2);
            }
        });
        mBuilder.setCancelable(true);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
    //3---单选选按钮型对话框
    public void showChooseOneDialog(String title, String []message, String btnNameOK, String btnNameNo) {
        if(mBuilder == null) return;
        mBuilder.setIcon(R.mipmap.ic_launcher);
        mBuilder.setTitle(title);
        selectId = 0;
        mBuilder.setPositiveButton(btnNameOK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickOk(selectId);
            }
        });
        mBuilder.setNegativeButton(btnNameNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickOk(-2);
            }
        });
        mBuilder.setSingleChoiceItems(message, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectId = i;
            }
        });
        mBuilder.setCancelable(true);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    //4---多选选按钮型对话框
    public void showChooseMultipleDialog(String title, String []message, String btnNameOK, String btnNameNo) {
        if(mBuilder == null) return;
        mBuilder.setIcon(R.mipmap.ic_launcher);
        mBuilder.setTitle(title);
        selectId = 0;
        mBuilder.setPositiveButton(btnNameOK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickOk(selectId);
            }
        });
        mBuilder.setNegativeButton(btnNameNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickOk(-2);
            }
        });
        boolean[] initdt = new boolean[message.length];
        mBuilder.setMultiChoiceItems(message,initdt, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                int num = (int)Math.pow(2,i);
                if(b){
                    selectId += num;
                }else{
                    selectId -= num;
                }
            }
        });
        mBuilder.setCancelable(true);
        AlertDialog dialog=mBuilder.create();
        dialog.show();
    }

    public void showNoProgressDialog(String title, String message) {
        if(mPD == null) return;
        //静态圆进度形条可取消
        mPD = ProgressDialog.show(mContext, title, message, false, true);
        mPD.show();
    }

    public void hideProgressDialog() {
        if(mPD == null) return;
        mPD.dismiss();
    }

    public void showProgressDialog(String title, String message) {
        if(mPD == null) return;
        mPD.setTitle(title);
        mPD.setMessage(message);
        mPD.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mPD.show();
    }
    public void freshProgress(int num){
        if(mPD == null) return;
        mPD.setProgress(num);
    }

    private onClickSureListener mListerner;

    public void setClickListener(onClickSureListener listener){
        mListerner = listener;
    }

    private void onClickOk(int index){
        mListerner.onClickSure(index);
    }
}
