package com.horizon.gank.hgank.ui.activity;

import android.animation.Animator;
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
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.horizon.gank.hgank.BaseActivity;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.ui.popup.SharePopupWindow;
import com.horizon.gank.hgank.ui.widget.FlashBackGroundTextView;
import com.horizon.gank.hgank.ui.widget.web.WebChromeClient;
import com.horizon.gank.hgank.ui.widget.web.WebView;
import com.horizon.gank.hgank.ui.widget.web.WebViewClient;
import com.horizon.gank.hgank.ui.widget.web.WebViewView;
import com.horizon.gank.hgank.util.BusEvent;
import com.horizon.gank.hgank.util.DrawableUtils;
import com.horizon.gank.hgank.util.PreUtils;
import com.horizon.gank.hgank.util.SimpleAnimatorListener;
import com.horizon.gank.hgank.util.SystemStatusManager;
import com.horizon.gank.hgank.util.ThemeUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

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
    @Bind(R.id.btn_right1)
    ImageView mBtnRefresh;
    @Bind(R.id.btn_right)
    ImageView mBtnShare;
    @Bind(R.id.tv_title)
    FlashBackGroundTextView mTvTitle;
    @Bind(R.id.refresh_layout)
    PtrClassicFrameLayout mRefreshLayout;

    private boolean mHasVideo;
    private SharePopupWindow sharePopup;

    private UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(WebViewActivity.this, platform + " 分享成功!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable throwable) {
            Toast.makeText(WebViewActivity.this, platform + " 分享失败!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(WebViewActivity.this, platform + " 分享取消!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemStatusManager.setTranslucentStatusColor(this, ThemeUtils.getThemeColor(this, R.attr.colorPrimary));
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
        mBtnLeft.setImageDrawable(DrawableUtils.getDrawable(this, MaterialDesignIconic.Icon.gmi_mail_reply));
        mBtnRefresh.setImageDrawable(DrawableUtils.getDrawable(this, MaterialDesignIconic.Icon.gmi_refresh));
        mBtnRefresh.setVisibility(View.GONE);
        mBtnShare.setImageDrawable(DrawableUtils.getDrawable(this, MaterialDesignIconic.Icon.gmi_share));

        int color = PreUtils.getInt(this, Constants.BUNDLE_OLD_THEME_COLOR, this.getResources().getColor(R.color.blue));
        ClipDrawable drawable = new ClipDrawable(new ColorDrawable(color), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        mProgress.setProgressDrawable(drawable);
        //mProgress.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        firstLoadAfter = true;

        mWebView.setWebChromeClient(new WebChromeClient(this));
        mWebView.setWebViewClient(new WebViewClient(this));

        firstLoad();

        RxView.clicks(tvOther)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(mWebView.getUrl() != null && mWebView.getUrl().startsWith("http")) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(mWebView.getUrl()));
                            startActivity(intent);
                        }
                    }
                });
        RxView.clicks(mBtnLeft).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        RxView.clicks(mBtnRefresh).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mWebView.reload();
                    }
                });
        RxView.clicks(mBtnShare).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showShare();
                    }
                });

        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        mRefreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mWebView.reload();
            }
        });
//        mRefreshLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mRefreshLayout.autoRefresh(true);
//            }
//        }, 150);
    }

    private void showShare() {
        sharePopup = new SharePopupWindow(this, url, mTvTitle.getText().toString(), mTvTitle.getText().toString(), null, umShareListener);
        sharePopup.show();
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

        if(mBtnShare.getVisibility() != View.VISIBLE) {
            mBtnShare.setVisibility(View.VISIBLE);
            mBtnShare.setAlpha(0f);
            mBtnShare.setScaleX(0.1f);
            mBtnShare.setScaleY(0.1f);
            mBtnShare.animate().alpha(1f).scaleX(1.3f).scaleY(1.3f).setDuration(400).setListener(new SimpleAnimatorListener(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    mBtnShare.animate().scaleX(1f).scaleY(1f).setDuration(400).start();
                }
            }).start();
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
        if(sharePopup != null){
            sharePopup.dismiss();
        }
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
        if(url.equals(mWebView.getUrl())){
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
            mRefreshLayout.refreshComplete();
            mProgress.setVisibility(View.GONE);
        } else {
            if(mProgress.getVisibility() == View.GONE){
                mProgress.setVisibility(View.VISIBLE);
            }
            mProgress.setProgress(newProgress);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
