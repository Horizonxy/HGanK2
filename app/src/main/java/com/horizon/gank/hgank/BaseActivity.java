package com.horizon.gank.hgank;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.horizon.gank.hgank.util.PreUtils;
import com.zhy.autolayout.AutoLayoutActivity;

public class BaseActivity extends AutoLayoutActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = PreUtils.getInt(this, Constants.BUNDLE_THEME, R.style.red_theme);
        setTheme(theme);
    }

}
