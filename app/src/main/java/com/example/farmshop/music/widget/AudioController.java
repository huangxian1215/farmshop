package com.example.farmshop.music.widget;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.music.util.Utils;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AudioController extends RelativeLayout implements OnClickListener, OnSeekBarChangeListener {
    private static final String TAG = "AudioController";
    private Context mContext;
    private ImageView mImagePlay;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private SeekBar mSeekBar;
    private MainApplication app;
    private int mBeginViewId = 0x7F24FFF0;
    private int dip_10, dip_40;
    private int mCurrent = 0;
    private int mBuffer = 0;
    private int mDuration = 0;
    private boolean bPause = false;

    public AudioController(Context context) {
        this(context, null);
    }

    public AudioController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        dip_10 = Utils.dip2px(mContext, 10);
        dip_40 = Utils.dip2px(mContext, 40);
        initView();
        app = MainApplication.getInstance();
    }

    private TextView newTextView(Context context, int id) {
        TextView tv = new TextView(context);
        tv.setId(id);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        tv.setLayoutParams(params);
        return tv;
    }

    private void initView() {
        mImagePlay = new ImageView(mContext);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(dip_40, dip_40);
        imageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mImagePlay.setLayoutParams(imageParams);
        mImagePlay.setId(mBeginViewId);
        mImagePlay.setOnClickListener(this);

        mCurrentTime = newTextView(mContext, mBeginViewId+1);
        RelativeLayout.LayoutParams currentParams = (LayoutParams) mCurrentTime.getLayoutParams();
        currentParams.setMargins(dip_10, 0, 0, 0);
        currentParams.addRule(RelativeLayout.RIGHT_OF, mImagePlay.getId());
        mCurrentTime.setLayoutParams(currentParams);

        mTotalTime = newTextView(mContext, mBeginViewId+2);
        RelativeLayout.LayoutParams totalParams = (LayoutParams) mTotalTime.getLayoutParams();
        totalParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mTotalTime.setLayoutParams(totalParams);

        mSeekBar = new SeekBar(mContext);
        RelativeLayout.LayoutParams seekParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        totalParams.setMargins(dip_10, 0, dip_10, 0);
        seekParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        seekParams.addRule(RelativeLayout.RIGHT_OF, mCurrentTime.getId());
        seekParams.addRule(RelativeLayout.LEFT_OF, mTotalTime.getId());
        mSeekBar.setLayoutParams(seekParams);
        mSeekBar.setMax(100);
        mSeekBar.setMinimumHeight(100);
        mSeekBar.setThumbOffset(0);
        mSeekBar.setId(mBeginViewId+3);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    private void reset() {
        if (mCurrent == 0 || bPause) {
            mImagePlay.setImageResource(R.drawable.btn_play);
        } else {
            mImagePlay.setImageResource(R.drawable.btn_pause);
        }
        mCurrentTime.setText(Utils.formatTime(mCurrent));
        mSeekBar.setSecondaryProgress(mBuffer);
        mTotalTime.setText(Utils.formatTime(mDuration));
        if (mDuration == 0) {
            mSeekBar.setProgress(0);
        } else {
            mSeekBar.setProgress((mCurrent==0)?0:(mCurrent*100/mDuration));
        }
    }

    private void refresh() {
        invalidate();
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        removeAllViews();
        reset();
        addView(mImagePlay);
        addView(mCurrentTime);
        addView(mTotalTime);
        addView(mSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int time = seekBar.getProgress() * mDuration / 100;
        app.mMediaPlayer.seekTo(time);
        if (mSeekListener != null) {
            mSeekListener.onMusicSeek(app.mMediaPlayer.getCurrentPosition(), time);
        }
    }

    private OnSeekChangeListener mSeekListener;
    public static interface OnSeekChangeListener {
        public void onMusicSeek(int current, int seekto);
        public void onMusicPause();
        public void onMusicResume();
    }
    public void setOnSeekChangeListener(OnSeekChangeListener listener) {
        mSeekListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mImagePlay.getId()) {
            if (app.mMediaPlayer.getDuration() == 0) {
                return;
            }
            if (app.mMediaPlayer.isPlaying()) {
                app.mMediaPlayer.pause();
                bPause = true;
                if (mSeekListener != null) {
                    mSeekListener.onMusicPause();
                }
            } else {
                if (mCurrent == 0 && mSeekListener != null) {
                    mSeekListener.onMusicSeek(0, 0);
                }
                app.mMediaPlayer.start();
                bPause = false;
                if (mSeekListener != null) {
                    mSeekListener.onMusicResume();
                }
            }
        }
        refresh();
    }

    public void setCurrentTime(int current_time, int buffer_time) {
        mDuration = app.mMediaPlayer.getDuration();
        mCurrent = current_time;
        mBuffer = buffer_time;
        bPause = !app.mMediaPlayer.isPlaying();
        refresh();
    }

}

