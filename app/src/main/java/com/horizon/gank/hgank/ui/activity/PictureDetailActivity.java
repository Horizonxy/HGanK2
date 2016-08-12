package com.horizon.gank.hgank.ui.activity;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.ui.widget.AnimationFrameLayout;
import com.horizon.gank.hgank.util.BitmapUtils;
import com.horizon.gank.hgank.util.DisplayUtils;
import com.horizon.gank.hgank.util.DrawableUtils;
import com.horizon.gank.hgank.util.FileUtils;
import com.horizon.gank.hgank.util.PreUtils;
import com.horizon.gank.hgank.util.SimpleAnimatorListener;
import com.horizon.gank.hgank.util.SmallPicInfo;
import com.horizon.gank.hgank.util.SystemStatusManager;
import com.jakewharton.rxbinding.view.RxView;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureDetailActivity extends Activity {

    @Bind(R.id.rl_root)
    AutoRelativeLayout rlRoot;
    @Bind(R.id.image_detail)
    ImageView ivDetail;
    @Bind(R.id.progress_load)
    ProgressBar progressLoad;
    @Bind(R.id.tv_desc)
    TextView tvDesc;
    @Bind(R.id.afl_desc)
    AnimationFrameLayout aflDesc;
    @Bind(R.id.btn_download)
    ImageView btnDownload;

    SmallPicInfo smallPicInfo;
    PhotoViewAttacher attacher;

    private static final int DURATION = 250;
    private int screenWidth, screenHeight;
    public static Bitmap bmp;
    private boolean isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemStatusManager.setTranslucentStatusRes(this, R.color.transparent);
        setContentView(R.layout.activity_picture_detail);
        ButterKnife.bind(this);

        rlRoot.setAlpha(0f);
        rlRoot.animate().alpha(1f).setDuration(DURATION);

        screenWidth = DisplayUtils.screenWidth(this);
        screenHeight = DisplayUtils.screenHeight(this);

        smallPicInfo = (SmallPicInfo) getIntent().getSerializableExtra(Constants.BUNDLE_PIC_INFOS);

        attacher = new PhotoViewAttacher(ivDetail);

        DiskCache diskCache = Application.application.getImageLoader().getDiskCache();
        File file = diskCache.get(smallPicInfo.data.getUrl());
        if (file != null && file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if(bitmap == null){
                loadOnNetwork();
            } else {
                loadOnCache(bitmap);
            }
        } else {
            loadOnNetwork();
        }

        if(!TextUtils.isEmpty(smallPicInfo.data.getDesc())){
            tvDesc.setText(smallPicInfo.data.getDesc());
            aflDesc.setAnimationVisibility(View.VISIBLE);
        }

        int theme = PreUtils.getInt(this, Constants.BUNDLE_THEME, R.style.red_theme);
        int color = Constants.Theme.byTheme(theme).getColor();
        DrawableUtils.setImageDrawable(btnDownload, MaterialDesignIconic.Icon.gmi_download, 30, color);
        RxView.clicks(btnDownload).throttleFirst(1, TimeUnit.SECONDS)
                .map(new Func1<Void, Bitmap>() {
                    @Override
                    public Bitmap call(Void aVoid) {
                        return ((BitmapDrawable)ivDetail.getDrawable()).getBitmap();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<Bitmap, Boolean>() {
                    @Override
                    public Boolean call(Bitmap bitmap) {
                        String downloadPath = FileUtils.getEnvPath(PictureDetailActivity.this, true, Constants.IMG_DOWNLOAD_DIR);
                        String name = smallPicInfo.data.getUrl().substring(smallPicInfo.data.getUrl().lastIndexOf("/"));
                        return  BitmapUtils.saveBmp2SD(bmp, downloadPath,name);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if(aBoolean != null && aBoolean.booleanValue() == true){
                            Snackbar.make(aflDesc, "保存图片到 " + FileUtils.getEnvPath(PictureDetailActivity.this, true, Constants.IMG_DOWNLOAD_DIR) +" 成功", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(aflDesc, "保存图片失败，请稍后重试！", Snackbar.LENGTH_LONG).show();
                        }
                    }
        });


    }

    private void loadOnNetwork(){
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivDetail.getLayoutParams();
        lp.width = smallPicInfo.width;
        lp.height = smallPicInfo.height;
        ivDetail.setLayoutParams(lp);

//        Bitmap small = BitmapFactory.decodeByteArray(smallPicInfo.bmp, 0, smallPicInfo.bmp.length);
        if(bmp == null) {
            ivDetail.setImageBitmap(bmp);
        }

        int smallDeltaX = smallPicInfo.left - (screenWidth - smallPicInfo.width) / 2;
        int smallDeltaY = smallPicInfo.top - (screenHeight - smallPicInfo.height + DisplayUtils.getStatusBarHeight(PictureDetailActivity.this)) / 2;

        ivDetail.setPivotX(0);
        ivDetail.setPivotY(0);
        ivDetail.setTranslationX(smallDeltaX);
        ivDetail.setTranslationY(smallDeltaY);

        ivDetail.animate().translationX(0).translationY(0).setDuration(DURATION).setListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressLoad.setVisibility(View.VISIBLE);

                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheOnDisk(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();

                Application.application.getImageLoader().displayImage(smallPicInfo.data.getUrl(), ivDetail, options, new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        progressLoad.setVisibility(View.GONE);
                        attacher.update();

                        float scaleX = screenWidth * 1f / smallPicInfo.width;
                        float scaleY = screenHeight * 1f / smallPicInfo.height;
                        float scale = Math.min(scaleX, scaleY);

                        ivDetail.setPivotX(smallPicInfo.width / 2);
                        ivDetail.setPivotY(smallPicInfo.height / 2);

                        ivDetail.animate().scaleX(scale).scaleY(scale).setDuration(DURATION).setListener(new SimpleAnimatorListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                ivDetail.setScaleX(1f);
                                ivDetail.setScaleY(1f);
                                setImageViewMatch();
                            }
                        });
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        progressLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        progressLoad.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void loadOnCache(Bitmap bitmap){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scale = screenWidth * 1.0f / w;

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivDetail.getLayoutParams();
        lp.height = (int) (scale * h);
        lp.width = screenWidth;
        ivDetail.setLayoutParams(lp);
        ivDetail.setImageBitmap(bitmap);
        attacher.update();

        float scaleX = smallPicInfo.width * 1.0f / lp.width;
        float scaleY = smallPicInfo.height * 1.0f / lp.height;

        int deltaX = smallPicInfo.left - (screenWidth - lp.width) / 2;
        int deltaY = smallPicInfo.top - (screenHeight - lp.height) / 2;

        ivDetail.setPivotX(0);
        ivDetail.setPivotY(0);
        ivDetail.setScaleX(scaleX);
        ivDetail.setScaleY(scaleY);

        ivDetail.setTranslationX(deltaX);
        ivDetail.setTranslationY(deltaY);

        ivDetail.animate()
                .scaleX(1f).scaleY(1f)
                .translationX(0).translationY(0)
                .setDuration(DURATION).setListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setImageViewMatch();
            }
        });
    }

    private void setImageViewMatch(){
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivDetail.getLayoutParams();
        lp.height = screenHeight;
        lp.width = screenWidth;
        ivDetail.setLayoutParams(lp);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onBackPressed() {
        if(isExit){
            return;
        }
        isExit = true;

        rlRoot.animate().alpha(0.4f).setDuration(DURATION);

        RectF rect = attacher.getDisplayRect();
        float scaleX = smallPicInfo.width * 1f / Math.min(rect.width(), ivDetail.getWidth());
        float scaleY = smallPicInfo.height * 1f / Math.min(rect.height(), ivDetail.getHeight());

        int[] location = new int[2];
        ivDetail.getLocationOnScreen(location);
        int deltaX = (int) (smallPicInfo.left - (location[0] + (ivDetail.getWidth() - rect.width() > 0 ? scaleX * (ivDetail.getWidth() - rect.width()) / 2 : 0)));
        int deltaY = (int) (smallPicInfo.top - (location[1] + (ivDetail.getHeight() - rect.height() > 0 ? scaleY * (ivDetail.getHeight() - rect.height()) / 2 : 0)));

        ivDetail.setPivotX(0);
        ivDetail.setPivotY(0);
        ivDetail.setScaleX(1f);
        ivDetail.setScaleY(1f);
        ivDetail.setTranslationX(0);
        ivDetail.setTranslationY(0);

        ivDetail.animate().scaleX(scaleX).scaleY(scaleY).translationX(deltaX).translationY(deltaY).setDuration(DURATION).setListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                PictureDetailActivity.super.onBackPressed();
                isExit = false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smallPicInfo = null;
        bmp = null;
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(0, 0);
    }
}
