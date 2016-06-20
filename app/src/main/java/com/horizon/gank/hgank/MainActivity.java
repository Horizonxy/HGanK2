package com.horizon.gank.hgank;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.horizon.gank.hgank.ui.adapter.GanKTabAdapter;
import com.horizon.gank.hgank.util.DrawableUtils;
import com.horizon.gank.hgank.util.PreUtils;
import com.horizon.gank.hgank.util.ThemeUtils;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tool_bar)
    Toolbar mToolBar;
    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.btn_left)
    ImageView mBtnLeft;
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    private static final List<String> TITLES = Arrays.asList(new String[]{ "福利", "Android", "iOS", "前端", "休息视频", "App", "瞎推荐", "拓展资源" });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator(DrawableUtils.getDrawable(this, MaterialDesignIconic.Icon.gmi_palette));
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle("干货集中营");
        DrawableUtils.setImageDrawable(mBtnLeft, MaterialDesignIconic.Icon.gmi_palette, 30, PreUtils.getInt(this, Constants.BUNDLE_THEME_BTN_COLOR,
                getResources().getColor(R.color.blue)));
        mTvTitle.setText("干货集中营");

        GanKTabAdapter adapter = new GanKTabAdapter(TITLES, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick(R.id.btn_left)
    void changeTheme(){
        int current = PreUtils.getInt(this, Constants.BUNDLE_THEME, 0);
        if(current == 0 || current == R.style.red_theme){
            PreUtils.putInt(this, Constants.BUNDLE_THEME, R.style.blue_theme);
        } else {
            PreUtils.putInt(this, Constants.BUNDLE_THEME, R.style.red_theme);
        }
        PreUtils.putInt(this, Constants.BUNDLE_THEME_BTN_COLOR, ThemeUtils.getThemeColor(this, R.attr.colorPrimary));
        finish();
        startActivity(getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName()));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
