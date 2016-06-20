package com.horizon.gank.hgank;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.horizon.gank.hgank.di.component.DaggerAppComponent;
import com.horizon.gank.hgank.di.module.AppModule;
import com.horizon.gank.hgank.model.api.ApiService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import javax.inject.Inject;

public class Application extends android.app.Application {

    public int SCREENWIDTH, SCREENHEIGHT;
    public static Application application;

    @Inject
    DisplayImageOptions defaultOptions;
    @Inject
    ImageLoader imageLoader;
    @Inject
    Resources res;
    @Inject
    ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().appModule(new AppModule(this)).build().inject(this);

        application = this;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        SCREENHEIGHT = displayMetrics.heightPixels;
        SCREENWIDTH = displayMetrics.widthPixels;
    }

    public DisplayImageOptions getDefaultOptions() {
        return defaultOptions;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public Resources getRes(){
        return  res;
    }

    public ApiService getApiService(){
        return apiService;
    }
}
