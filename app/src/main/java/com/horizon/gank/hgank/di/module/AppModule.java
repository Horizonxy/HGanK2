package com.horizon.gank.hgank.di.module;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.util.FileUtils;
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
    public Resources provideResources(){
        return application.getResources();
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
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(application)
                .threadPoolSize(20)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiskCache(imgFile))
                .writeDebugLogs()
                .build();
        imageLoader.init(config);

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
