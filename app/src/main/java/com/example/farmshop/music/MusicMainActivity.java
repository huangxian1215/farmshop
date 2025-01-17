package com.example.farmshop.music;
        import java.util.ArrayList;

        import android.Manifest;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.media.AudioManager;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.widget.TabLayout;
        import android.support.design.widget.TabLayout.ViewPagerOnTabSelectedListener;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.view.ViewPager;
        import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.KeyEvent;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.farmshop.R;
        import com.example.farmshop.music.bean.dbMusicInfo;
        import com.example.farmshop.music.database.UserDBHelper;
        import com.example.farmshop.music.widget.VolumeDialog;
        import com.example.farmshop.music.widget.VolumeDialog.VolumeAdjustListener;

        import com.example.farmshop.music.adapter.PageAdapter;
        import com.example.farmshop.music.util.Utils;
        import com.example.farmshop.music.widget.VolumeDialog;

/**
 * Created by ouyangshen on 2016/10/21.
 */
public class MusicMainActivity extends AppCompatActivity implements VolumeAdjustListener{
    private final static String TAG = "TabCustomActivity";
    private Toolbar tl_head;
    private ViewPager vp_content;
    private TabLayout tab_title;
    private TextView tv_toolbar1, tv_toolbar2;
    private ArrayList<String> mTitleArray = new ArrayList<String>();
    private VolumeDialog dialog;
    private AudioManager mAudioMgr;
    private UserDBHelper mHelper;
    private PageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_custom);
        tl_head = (Toolbar) findViewById(R.id.tl_head);
        tab_title = (TabLayout) findViewById(R.id.tab_title);
        vp_content = (ViewPager) findViewById(R.id.vp_content);
        setSupportActionBar(tl_head);
        mAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mTitleArray.add("本地");
        mTitleArray.add("网络");
        initTabLayout();
        initTabViewPager();
    }
    //音量调节对话框
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            showVolumeDialog(AudioManager.ADJUST_RAISE);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            showVolumeDialog(AudioManager.ADJUST_LOWER);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }
    @Override
    public void onVolumeAdjust(int volume) {
    }

    private void showVolumeDialog(int direction) {
        if (dialog==null || dialog.isShowing()!=true) {
            dialog = new VolumeDialog(this);
            dialog.setVolumeAdjustListener(this);
            dialog.show();
        }
        dialog.adjustVolume(direction, true);
        onVolumeAdjust(mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    private void initTabLayout() {
        tab_title.addTab(tab_title.newTab().setCustomView(R.layout.item_toolbar1));
        tv_toolbar1 = (TextView) findViewById(R.id.tv_toolbar1);
        tv_toolbar1.setText(mTitleArray.get(0));
        tab_title.addTab(tab_title.newTab().setCustomView(R.layout.item_toolbar2));
        tv_toolbar2 = (TextView) findViewById(R.id.tv_toolbar2);
        tv_toolbar2.setText(mTitleArray.get(1));
        tab_title.setOnTabSelectedListener(new ViewPagerOnTabSelectedListener(vp_content));
    }

    private void initTabViewPager() {
        mAdapter = new PageAdapter(
                getSupportFragmentManager(), mTitleArray);
        vp_content.setAdapter(mAdapter);
        vp_content.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tab_title.getTabAt(position).select();
            }
        });
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        // 显示菜单项左侧的图标
        Utils.setOverflowIconVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_refresh) {
            return true;
        } else if (id == R.id.menu_about) {
            Toast.makeText(this, "版权属于QQ音乐", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.menu_quit) {
            finish();
        }
        return super.onOptionsItemSelected(item);//
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = UserDBHelper.getInstance(this, 2);
        mHelper.openReadLink();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHelper.closeLink();
    }
}
