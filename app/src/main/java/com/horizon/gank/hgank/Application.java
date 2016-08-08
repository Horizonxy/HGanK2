package com.horizon.gank.hgank;

import android.Manifest;
import android.graphics.Bitmap;

import com.horizon.gank.hgank.model.api.ApiService;
import com.horizon.gank.hgank.util.FileUtils;
import com.horizon.gank.hgank.util.LogUtils;
import com.horizon.gank.hgank.util.PermissionUtils;
import com.horizon.gank.hgank.util.RetrofitUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

public class Application extends android.app.Application {

    public static Application application;

    DisplayImageOptions defaultOptions;
    ImageLoader imageLoader;
    ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.setState(BuildConfig.LOG_DEBUG);
        application = this;
    }

    public DisplayImageOptions getDefaultOptions() {
        if(defaultOptions == null) {
            synchronized (Application.class) {
                if (defaultOptions == null) {
                    defaultOptions = new DisplayImageOptions.Builder()
                            .cacheOnDisk(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .displayer(new FadeInBitmapDisplayer(2000))
                            .build();
                }
            }
        }
        return defaultOptions;
    }

    public ImageLoader getImageLoader() {
        if(imageLoader == null) {
            synchronized (Application.class) {
                if(imageLoader == null) {
                    imageLoader = ImageLoader.getInstance();
                    final String IMG_CACHE_PATH = FileUtils.getEnvPath(application, true, Constants.IMG_CACHE_DIR);
                    File imgFile = new File(IMG_CACHE_PATH);
                    if (!imgFile.exists()) {
                        imgFile.mkdirs();
                    }
                    ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(application)
                            .threadPoolSize(5)
                            .threadPriority(Thread.NORM_PRIORITY - 2)
                            .denyCacheImageMultipleSizesInMemory()
                            .tasksProcessingOrder(QueueProcessingType.LIFO)
                            .writeDebugLogs();
                    if(PermissionUtils.checkPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})){
                        builder.discCache(new UnlimitedDiskCache(imgFile));
                    }
                    imageLoader.init(builder.build());
                }
            }
        }
        return imageLoader;
    }

    public ApiService getApiService(){
        if(apiService == null) {
            synchronized (Application.class) {
                if (apiService == null) {
                    apiService = RetrofitUtil.createRetrofit().create(ApiService.class);
                }
            }
        }
        return apiService;
    }
}
