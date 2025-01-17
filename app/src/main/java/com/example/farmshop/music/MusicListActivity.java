package com.example.farmshop.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.music.adapter.MusicInfoAdapter;
import com.example.farmshop.music.bean.MusicInfo;
import com.example.farmshop.music.bean.dbMusicInfo;
import com.example.farmshop.music.bean.qqMusicInfo;
import com.example.farmshop.music.database.UserDBHelper;
import com.example.farmshop.music.widget.AudioController;
import com.example.farmshop.util.VirtureUtil.onClickItemListener;

import java.util.ArrayList;


public class MusicListActivity extends AppCompatActivity implements OnClickListener, onClickItemListener {
    private ListView lv_music;
    private AudioController ac_play;
    private MainApplication app;
    private MusicInfoAdapter mMusicInfoAdapter;
    MusicInfo mMusic;
    private ArrayList<dbMusicInfo> mAllLocalMusic;
    private int mpack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        lv_music = (ListView) findViewById(R.id.lv_music);
        ac_play = (AudioController) findViewById(R.id.ac_play);
        mpack = getIntent().getIntExtra("type", 0);
        app = MainApplication.getInstance();
        findViewById(R.id.ac_play).setOnClickListener(this);
        display();
        app.downtype = 0;
    }

    public void display(){
        String condition = "1=1";
        if(mpack == 1){
            condition = "love=1";
        }
        if(mpack == 2){
            condition = "packname = 'down'";
        }
        mAllLocalMusic = UserDBHelper.getmHelper().query_tab(condition);
        app.mqqMusicInfo = new ArrayList<qqMusicInfo>();
        for(int n = 0; n < mAllLocalMusic.size(); n++){
            dbMusicInfo song = mAllLocalMusic.get(n);
            qqMusicInfo qsong = new qqMusicInfo();
            qsong.mMusicName = song.mname;
            qsong.mMusicSinger = song.msinger;
            qsong.mMusicAlbum = song.malbum;
            qsong.mMusicTime = song.mtime;
            qsong.mMusicUrl = song.murl;
            qsong.mIndex = n;
            app.mqqMusicInfo.add(qsong);
        }

        mMusicInfoAdapter = new MusicInfoAdapter(this, R.layout.activity_music_info,  app.mqqMusicInfo);
        mMusicInfoAdapter.setOnItemClickListener(this);
        lv_music.setAdapter(mMusicInfoAdapter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_play:
                if (app.mMusic != null) {
                    Intent intent = new Intent(this, MusicDetailActivity.class);
                    intent.putExtra("music", app.mMusic);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        //缓存MP3文件
        qqMusicInfo clickSong =  app.mqqMusicInfo.get(position);
        mMusic = new MusicInfo();
        mMusic.setTitle(clickSong.mMusicName);
        mMusic.setArtist(clickSong.mMusicSinger);
        mMusic.setUrl(clickSong.mMusicUrl);

        Intent intent = new Intent(this, MusicDetailActivity.class);
        intent.putExtra("music", mMusic);
        intent.putExtra("currentNum", position);
        startActivity(intent);
    }

}

