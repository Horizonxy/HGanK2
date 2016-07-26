package com.horizon.gank.hgank;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.horizon.gank.hgank.model.api.ApiService;
import com.horizon.gank.hgank.util.FileUtils;
import com.horizon.gank.hgank.util.RetrofitUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Application extends android.app.Application {

    public int SCREENWIDTH, SCREENHEIGHT;
    public static Application application;

//    @Inject
    DisplayImageOptions defaultOptions;
//    @Inject
    ImageLoader imageLoader;
//    @Inject
    Resources res;
//    @Inject
    ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
//        DaggerAppComponent.builder().appModule(new AppModule(this)).build().inject(this);

        application = this;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        SCREENHEIGHT = displayMetrics.heightPixels;
        SCREENWIDTH = displayMetrics.widthPixels;
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
                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(application)
                            .threadPoolSize(20)
                            .threadPriority(Thread.NORM_PRIORITY - 2)
                            .denyCacheImageMultipleSizesInMemory()
                            .tasksProcessingOrder(QueueProcessingType.LIFO)
                            .discCache(new UnlimitedDiskCache(imgFile))
                            .writeDebugLogs()
                            .build();
                    imageLoader.init(config);
                }
            }
        }
        return imageLoader;
    }

    public Resources getRes(){
        return  application.getRes();
    }

    public ApiService getApiService(){
        if(apiService == null) {
            synchronized (Application.class) {
                if (apiService == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .client(RetrofitUtil.createOkHttpClient())
                            .baseUrl(Constants.END_POIND)
                            .addConverterFactory(GsonConverterFactory.create(RetrofitUtil.createGson()))
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                    apiService = retrofit.create(ApiService.class);
                }
            }
        }
        return apiService;
    }
}
