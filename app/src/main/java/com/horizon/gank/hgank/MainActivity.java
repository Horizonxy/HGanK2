package com.horizon.gank.hgank;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.horizon.gank.hgank.receiver.NetReceiver;
import com.horizon.gank.hgank.ui.adapter.GanKTabAdapter;
import com.horizon.gank.hgank.ui.widget.AnimationFrameLayout;
import com.horizon.gank.hgank.util.BusEvent;
import com.horizon.gank.hgank.util.DrawableUtils;
import com.horizon.gank.hgank.util.NetUtils;
import com.horizon.gank.hgank.util.PreUtils;
import com.horizon.gank.hgank.util.ThemeUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

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
    private  NetReceiver receiver;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Bus.getDefault().register(this);

        setSupportActionBar(mToolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View topView = getLayoutInflater().inflate(R.layout.view_base_top, null);
        TextView tvTitle = (TextView) topView.findViewById(R.id.tv_title);
        ImageView btnLeft = (ImageView) topView.findViewById(R.id.btn_left);
        tvTitle.setText("干货集中营");
        DrawableUtils.setImageDrawable(btnLeft, MaterialDesignIconic.Icon.gmi_palette, 30, PreUtils.getInt(this, Constants.BUNDLE_OLD_THEME_COLOR,
                getResources().getColor(R.color.blue)));
        ab.setCustomView(topView);

        RxView.clicks(btnLeft)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        changeTheme();
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

        receiver = new NetReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    @BusReceiver
    public void onNetEvent(BusEvent.NetEvent event){
        mFlNoNet.setVisibility(event.isHasNet() ? View.GONE : View.VISIBLE);
    }

    void changeTheme(){
        int current = PreUtils.getInt(this, Constants.BUNDLE_THEME, 0);
        if(current == 0 || current == R.style.red_theme){
            PreUtils.putInt(this, Constants.BUNDLE_THEME, R.style.blue_theme);
        } else {
            PreUtils.putInt(this, Constants.BUNDLE_THEME, R.style.red_theme);
        }
        PreUtils.putInt(this, Constants.BUNDLE_OLD_THEME_COLOR, ThemeUtils.getThemeColor(this, R.attr.colorPrimary));
        finish();
        startActivity(getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName()));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        if(receiver!= null) {
            unregisterReceiver(receiver);
        }
        if(mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()){
            mCompositeSubscription.unsubscribe();
        }
        Bus.getDefault().unregister(this);
        super.onDestroy();
    }
}
