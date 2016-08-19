package com.horizon.gank.hgank;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.horizon.gank.hgank.download.FileCallBack;
import com.horizon.gank.hgank.model.api.DownLoadApiServide;
import com.horizon.gank.hgank.receiver.NetReceiver;
import com.horizon.gank.hgank.ui.adapter.GanKTabAdapter;
import com.horizon.gank.hgank.ui.dialog.ThemeColorDialog;
import com.horizon.gank.hgank.ui.widget.AnimationFrameLayout;
import com.horizon.gank.hgank.util.BusEvent;
import com.horizon.gank.hgank.util.DrawableUtils;
import com.horizon.gank.hgank.util.FileUtils;
import com.horizon.gank.hgank.util.LogUtils;
import com.horizon.gank.hgank.util.NetUtils;
import com.horizon.gank.hgank.util.RetrofitUtil;
import com.horizon.gank.hgank.util.SystemStatusManager;
import com.horizon.gank.hgank.util.ThemeUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tool_bar)
    Toolbar mToolBar;
    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.afl_no_net)
    AnimationFrameLayout mFlNoNet;

    private static final List<String> TITLES = Arrays.asList(new String[]{ "福利", "Android", "iOS", "前端", "休息视频", "App", "瞎推荐", "拓展资源" });
    private NetReceiver netrRceiver;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private ActionBar ab;
    private View topView;

    private static final int NOTIFY_ID = MainActivity.class.getSimpleName().hashCode();
    private NotificationCompat.Builder notification;
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemStatusManager.setTranslucentStatusColor(this, ThemeUtils.getThemeColor(this, R.attr.colorPrimary));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Bus.getDefault().register(this);

        setSupportActionBar(mToolBar);
        ab = getSupportActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        topView = getLayoutInflater().inflate(R.layout.view_base_top, null);
        TextView tvTitle = (TextView) topView.findViewById(R.id.tv_title);
        ImageView btnLeft = (ImageView) topView.findViewById(R.id.btn_left);
        tvTitle.setText("干货集中营");
        DrawableUtils.setImageDrawable(btnLeft, MaterialDesignIconic.Icon.gmi_palette, 30, getResources().getColor(R.color.white));
        ab.setCustomView(topView);

        RxView.clicks(btnLeft)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        new ThemeColorDialog(MainActivity.this).show();
                    }
                });

        GanKTabAdapter adapter = new GanKTabAdapter(TITLES, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        RxView.clicks(mFlNoNet)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                });
        mFlNoNet.setVisibility(NetUtils.isNetworkConnected(this) ? View.GONE : View.VISIBLE);

        netrRceiver = new NetReceiver();
        IntentFilter netFilter = new IntentFilter();
        netFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netrRceiver, netFilter);

        if(BuildConfig.DEBUG){
            testDownLoad();
        }
    }

    private void testDownLoad(){
        final String path = FileUtils.getEnvPath(MainActivity.this, true, Constants.BASE_DIR);
        final String name = "test_download.apk";
        File file = new File(path,name);
        if(file.exists()){
            file.delete();
        }

        DownLoadApiServide downLoadApiServide = RetrofitUtil.createDownLoadApi();
        downLoadApiServide.loadFile().enqueue(new FileCallBack(){
            @Override
            public void save(InputStream is) {
                FileUtils.write2SDFromInput(path, name, is);
            }
        });
    }

    @BusReceiver
    public void onFileDownLoadEvent(BusEvent.FileDownLoadEvent event){
        if(notification != null) {
            notification.setProgress((int) event.getTotal(), (int) event.getProgress(), false);
            notification.setContentText("下载："+ (event.getProgress() * 100 / event.getTotal())+"%");
            mNotificationManager.notify(NOTIFY_ID, notification.build());
        } else{
            notification = new NotificationCompat.Builder(this);
            notification.setTicker("测试下载...");
            notification.setContentTitle(getResources().getString(R.string.app_name));
            notification.setContentText("下载准备中...");
            notification.setSmallIcon(R.mipmap.icon);
            notification.setWhen(System.currentTimeMillis());
//            notification.setOngoing(true);
            notification.setAutoCancel(true);

            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFY_ID, notification.build());
        }
    }

    @BusReceiver
    public void onThemeColorEvent(final BusEvent.ThemeColorEvent event){
        //SystemStatusManager.setTranslucentStatusColor(MainActivity.this, event.getColor());
        mToolBar.setBackgroundColor(event.getColor());
        topView.setBackgroundColor(event.getColor());
        mTabLayout.setBackgroundColor(event.getColor());
        System.gc();
    }

    @BusReceiver
    public void onNetEvent(BusEvent.NetEvent event){
        mFlNoNet.setVisibility(event.isHasNet() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        if(netrRceiver != null) {
            unregisterReceiver(netrRceiver);
        }
        if(mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()){
            mCompositeSubscription.unsubscribe();
        }
        Bus.getDefault().unregister(this);
        super.onDestroy();
    }
}
