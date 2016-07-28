package com.horizon.gank.hgank.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.horizon.gank.hgank.BaseActivity;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.ui.widget.ColorHorizontalProgressBar;
import com.horizon.gank.hgank.ui.widget.FlashBackGroundTextView;
import com.horizon.gank.hgank.ui.widget.web.WebChromeClient;
import com.horizon.gank.hgank.ui.widget.web.WebView;
import com.horizon.gank.hgank.ui.widget.web.WebViewClient;
import com.horizon.gank.hgank.ui.widget.web.WebViewView;
import com.horizon.gank.hgank.util.DrawableUtils;
import com.horizon.gank.hgank.util.PreUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class WebViewActivity extends BaseActivity implements WebViewView {

    @Bind(R.id.web_view)
    WebView mWevView;
    @Bind(R.id.progress)
    ColorHorizontalProgressBar mProgress;

    boolean firstLoadAfter;
    String url;

    @Bind(R.id.tv_other)
    TextView tvOther;
    @Bind(R.id.btn_left)
    ImageView mBtnLeft;
    @Bind(R.id.tv_title)
    FlashBackGroundTextView mTvTitle;

    private boolean mHasVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        mTvTitle.setmAnimating(false);
        mBtnLeft.setImageDrawable(DrawableUtils.getDrawable(this, MaterialDesignIconic.Icon.gmi_mail_reply_all));

        int color = PreUtils.getInt(this, Constants.BUNDLE_OLD_THEME_COLOR, this.getResources().getColor(R.color.blue));
        mProgress.setProgressColor(color);

        firstLoadAfter = true;

        mWevView.setWebChromeClient(new WebChromeClient(this));
        mWevView.setWebViewClient(new WebViewClient(this));

        url = getIntent().getStringExtra(Constants.BUNDLE_WEBVIEW_URL);
        mHasVideo = getIntent().getBooleanExtra(Constants.BUNDLE_WEBVIEW_VEDIO, false);

        if(url != null) {
            if(!url.startsWith("http")){
                url = "http://".concat(url);
            }
            firstLoad();
        }

        RxView.clicks(tvOther)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(mWevView.getUrl()));
                        startActivity(intent);
                    }
                });

        RxView.clicks(mBtnLeft)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        back();
                    }
                });
    }

    private void firstLoad(){
        mWevView.loadUrl(url);
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
        mWevView.onResume();
        mWevView.resumeTimers();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mWevView.onPause();
        mWevView.pauseTimers();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWevView.stopLoading();
        mWevView.setVisibility(View.GONE);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    back();
                    return true;
                default:
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void back(){
        if (url != null) {
            if(url.equals(mWevView.getUrl())){
                finish();
            } else if (null != mWevView.getUrl() && mWevView.getUrl().startsWith("data:")) {//页面请求失败，报错
                if (mWevView.canGoBackOrForward(-2)) {
                    mWevView.goBackOrForward(-2);
                    finish();
                } else if (mWevView.canGoBack()) {
                    mWevView.goBack();
                }else {
                    finish();
                }
            } else if (mWevView.canGoBack()) {
                mWevView.goBack();
            } else {
                finish();
            }
        } else if(mWevView.canGoBack()){
            mWevView.goBack();
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
