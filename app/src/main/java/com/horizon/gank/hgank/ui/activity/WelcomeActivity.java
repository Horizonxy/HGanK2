package com.horizon.gank.hgank.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.MainActivity;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.util.SystemStatusManager;
import com.nostra13.universalimageloader.cache.disc.DiskCache;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeActivity extends Activity {

    @Bind(R.id.iv_welcome)
    ImageView vWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemStatusManager.setTranslucentStatusRes(this, R.color.transparent);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        DiskCache diskCache = Application.application.getImageLoader().getDiskCache();
        File directory = diskCache.getDirectory();
        if(directory != null && directory.exists() && directory.listFiles() != null && directory.listFiles().length > 0){
            int index = (int) (Math.random() * (directory.listFiles().length -1));
            Bitmap bitmap = BitmapFactory.decodeFile(directory.listFiles()[index].getAbsolutePath());
            if(bitmap == null){
                vWelcome.setImageResource(R.mipmap.welcome);
            } else {
                vWelcome.setImageBitmap(bitmap);
            }
        } else {
            vWelcome.setImageResource(R.mipmap.welcome);
        }

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_start);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        vWelcome.startAnimation(anim);
    }

}
