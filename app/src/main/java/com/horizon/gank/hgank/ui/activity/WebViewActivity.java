package com.horizon.gank.hgank.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.horizon.gank.hgank.BaseActivity;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.ui.widget.FlashBackGroundTextView;
import com.horizon.gank.hgank.ui.widget.web.WebChromeClient;
import com.horizon.gank.hgank.ui.widget.web.WebView;
import com.horizon.gank.hgank.ui.widget.web.WebViewClient;
import com.horizon.gank.hgank.ui.widget.web.WebViewView;
import com.horizon.gank.hgank.util.BusEvent;
import com.horizon.gank.hgank.util.DrawableUtils;
import com.horizon.gank.hgank.util.PreUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class WebViewActivity extends BaseActivity implements WebViewView {

    @Bind(R.id.web_view)
    WebView mWebView;
    @Bind(R.id.progress)
    ProgressBar mProgress;

    boolean firstLoadAfter;
    String url;

    @Bind(R.id.tv_other)
    TextView tvOther;
    @Bind(R.id.btn_left)
    ImageView mBtnLeft;
    @Bind(R.id.btn_right)
    ImageView mBtnRight;
    @Bind(R.id.tv_title)
    FlashBackGroundTextView mTvTitle;

    private boolean mHasVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        Bus.getDefault().register(this);

        url = getIntent().getStringExtra(Constants.BUNDLE_WEBVIEW_URL);
        mHasVideo = getIntent().getBooleanExtra(Constants.BUNDLE_WEBVIEW_VEDIO, false);
        if(url != null) {
            if (!url.startsWith("http")) {
                url = "http://".concat(url);
            }
        }

        mTvTitle.setmAnimating(false);
        mBtnLeft.setImageDrawable(DrawableUtils.getDrawable(this, MaterialDesignIconic.Icon.gmi_mail_reply_all));
        mBtnRight.setImageDrawable(DrawableUtils.getDrawable(this, MaterialDesignIconic.Icon.gmi_refresh));
        mBtnRight.setVisibility(View.VISIBLE);

        int color = PreUtils.getInt(this, Constants.BUNDLE_OLD_THEME_COLOR, this.getResources().getColor(R.color.blue));
        ClipDrawable drawable = new ClipDrawable(new ColorDrawable(color), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        mProgress.setProgressDrawable(drawable);

        firstLoadAfter = true;

        mWebView.setWebChromeClient(new WebChromeClient(this));
        mWebView.setWebViewClient(new WebViewClient(this));

        firstLoad();

        RxView.clicks(tvOther)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(mWebView.getUrl()));
                        startActivity(intent);
                    }
                });
        RxView.clicks(mBtnLeft).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        RxView.clicks(mBtnRight).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mWebView.reload();
                    }
                });
    }

    private void firstLoad(){
        mWebView.loadUrl(url);
    }

    @Override
    public void firstLoadAfter(){
        if(firstLoadAfter){
            firstLoadAfter = false;
        }

        if(mHasVideo && !tvOther.isShown()){
            tvOther.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onResume() {
        mWebView.onResume();
        mWebView.resumeTimers();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mWebView.onPause();
        mWebView.pauseTimers();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.stopLoading();
        mWebView.setVisibility(View.GONE);
        Bus.getDefault().unregister(this);
        super.onDestroy();
    }

    @BusReceiver
    public void onNetEvent(BusEvent.NetEvent event){
        mWebView.setCacheMode();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void back(){
        if(mWebView.getUrl().equals(url)){
            finish();
        } else if(mWebView.canGoBack()){
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void onReceivedTitle(String title) {
        mTvTitle.setText(title == null ? "" : title);
    }

    @Override
    public void onProgressChanged(int newProgress) {
        if(newProgress == 100){
            mProgress.setVisibility(View.GONE);
        } else {
            if(mProgress.getVisibility() == View.GONE){
                mProgress.setVisibility(View.VISIBLE);
            }
            mProgress.setProgress(newProgress);
        }
    }

}
