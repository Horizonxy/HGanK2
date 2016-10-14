package com.horizon.gank.hgank.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.horizon.gank.hgank.BuildConfig;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.MainActivity;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.util.AppUtils;
import com.horizon.gank.hgank.util.FileUtils;
import com.horizon.gank.hgank.util.SystemStatusManager;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UmengTool;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeActivity extends Activity {

    @Bind(R.id.iv_welcome)
    ImageView vWelcome;

    private Bitmap bitmap;

    private static final String[] permissions = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemStatusManager.setTranslucentStatusRes(this, R.color.transparent);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        AppUtils.initCarsh(this);

        File directory = new File(FileUtils.getEnvPath(this, true, Constants.IMG_CACHE_DIR));
        if(directory != null && directory.exists() && directory.listFiles() != null && directory.listFiles().length > 0){
            int index = (int) (Math.random() * (directory.listFiles().length -1));
            try {
                bitmap = BitmapFactory.decodeFile(directory.listFiles()[index].getAbsolutePath());
            } catch (OutOfMemoryError e){
                e.printStackTrace();
            }
            if(bitmap == null){
                vWelcome.setImageResource(R.mipmap.welcome);
            } else {
                vWelcome.setImageBitmap(bitmap);
            }
        } else {
            vWelcome.setImageResource(R.mipmap.welcome);
        }

        initUmeng();

        RxPermissions.getInstance(this).request(permissions)
                .subscribe(granted -> {
                    if(granted){
                        start();
                    } else {
                        Toast.makeText(WelcomeActivity.this, "请在设置中同意应用权限！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initUmeng() {
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.enableEncrypt(true);
    }

    private void start(){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_start);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        vWelcome.startAnimation(anim);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
        }
    }
}
