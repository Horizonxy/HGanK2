package com.horizon.gank.hgank.ui.widget.web;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;

public class WebView extends android.webkit.WebView {

    public WebView(Context context) {
        this(context, null);
    }

    public WebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initSetting();
    }

    private void initSetting(){
        setBackgroundColor(0);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        getSettings().setJavaScriptEnabled(true);
        //getSettings().setUserAgentString("YooYo/1.0 " + getSettings().getUserAgentString());
        // 打开本地缓存提供JS调用
        getSettings().setDomStorageEnabled(true);
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //开启 database storage API 功能
        getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getContext().getCacheDir().getAbsolutePath()+"/web_cache";
        getSettings().setDatabasePath(cacheDirPath);
        getSettings().setAppCacheEnabled(true);
        getSettings().setAppCachePath(cacheDirPath);

        getSettings().setLoadWithOverviewMode(true);
        getSettings().setPluginState(WebSettings.PluginState.ON);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setAllowFileAccess(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setSupportMultipleWindows(true);

        clearHistory();
    }
}
