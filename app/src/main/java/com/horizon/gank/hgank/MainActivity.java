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
import com.horizon.gank.hgank.util.SystemStatusManager;
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

    private ImageView btnLeft;
    private ActionBar ab;
    private View topView;

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
        btnLeft = (ImageView) topView.findViewById(R.id.btn_left);
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
    public void onThemeColorEvent(final BusEvent.ThemeColorEvent event){
        SystemStatusManager.setTranslucentStatusColor(MainActivity.this, event.getColor());
        DrawableUtils.setImageDrawable(btnLeft, MaterialDesignIconic.Icon.gmi_palette, 30, PreUtils.getInt(MainActivity.this, Constants.BUNDLE_OLD_THEME_COLOR,
                getResources().getColor(R.color.blue)));
        mToolBar.setBackgroundColor(event.getColor());
        topView.setBackgroundColor(event.getColor());
        mTabLayout.setBackgroundColor(event.getColor());
        System.gc();

//        final View rootView = getWindow().getDecorView();
//        rootView.setDrawingCacheEnabled(true);
//        rootView.buildDrawingCache(true);
//
//        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
//        rootView.setDrawingCacheEnabled(false);
//        if (null != localBitmap && rootView instanceof ViewGroup) {
//            final View tmpView = new View(getApplicationContext());
//            tmpView.setBackgroundDrawable(new BitmapDrawable(getResources(), localBitmap));
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            ((ViewGroup) rootView).addView(tmpView, params);
//
//            tmpView.animate().alpha(0).setDuration(500).setListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    //SystemStatusManager.setTranslucentStatusColor(MainActivity.this, event.getColor());
//                    DrawableUtils.setImageDrawable(btnLeft, MaterialDesignIconic.Icon.gmi_palette, 30, PreUtils.getInt(MainActivity.this, Constants.BUNDLE_OLD_THEME_COLOR,
//                            getResources().getColor(R.color.blue)));
//                    mToolBar.setBackgroundColor(event.getColor());
//                    topView.setBackgroundColor(event.getColor());
//                    mTabLayout.setBackgroundColor(event.getColor());
//                    System.gc();
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    ((ViewGroup) rootView).removeView(tmpView);
//                    localBitmap.recycle();
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//                }
//            }).start();
//        }
    }

    @BusReceiver
    public void onNetEvent(BusEvent.NetEvent event){
        mFlNoNet.setVisibility(event.isHasNet() ? View.GONE : View.VISIBLE);
    }

    void changeTheme(){
        int current = PreUtils.getInt(this, Constants.BUNDLE_THEME, 0);
        int theme;
        if(current == 0 || current == R.style.red_theme){
            theme =  R.style.blue_theme;
        } else {
            theme = R.style.red_theme;
        }
        PreUtils.putInt(this, Constants.BUNDLE_OLD_THEME_COLOR, ThemeUtils.getThemeColor(this, R.attr.colorPrimary));
        setTheme(theme);
        PreUtils.putInt(this, Constants.BUNDLE_THEME, theme);

        BusEvent.ThemeColorEvent event = new BusEvent.ThemeColorEvent();
        event.setColor(ThemeUtils.getThemeColor(this, R.attr.colorPrimary));
        Bus.getDefault().post(event);

//        finish();
//        startActivity(getBaseContext().getPackageManager()
//                .getLaunchIntentForPackage(getBaseContext().getPackageName()));
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
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
