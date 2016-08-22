package com.horizon.gank.hgank.di.module;

import android.Manifest;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.util.FileUtils;
import com.horizon.gank.hgank.util.PermissionUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application application;

    public AppModule(Application application){
        this.application = application;
    }

    @Singleton
    @Provides
    public ImageLoader provideImageLoader(){
        ImageLoader imageLoader = ImageLoader.getInstance();
        final String IMG_CACHE_PATH = FileUtils.getEnvPath(application, true, Constants.IMG_CACHE_DIR);
        File imgFile = new File(IMG_CACHE_PATH);
        if(!imgFile.exists()){
            imgFile.mkdirs();
        }
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(application)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs();
        if(PermissionUtils.checkPermissions(application, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})){
            builder.discCache(new UnlimitedDiskCache(imgFile));
        }
        imageLoader.init(builder.build());

        return imageLoader;
    }

    @Singleton
    @Provides
    public DisplayImageOptions provideDisplayImageOptions(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(2000))
                .build();
        return options;
    }
}
