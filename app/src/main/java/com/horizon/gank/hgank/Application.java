package com.horizon.gank.hgank;

import com.horizon.gank.hgank.di.component.DaggerAppComponent;
import com.horizon.gank.hgank.di.module.ApiModule;
import com.horizon.gank.hgank.di.module.AppModule;
import com.horizon.gank.hgank.model.api.ApiService;
import com.horizon.gank.hgank.util.LogUtils;
import com.mcxiaoke.bus.Bus;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import javax.inject.Inject;

import dagger.Lazy;

public class Application extends android.app.Application {

    public static Application application;

    @Inject
    Lazy<DisplayImageOptions> defaultOptions;
    @Inject
    Lazy<ImageLoader> imageLoader;
    @Inject
    Lazy<ApiService> apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().apiModule(new ApiModule()).appModule(new AppModule(this)).build().inject(this);
        application = this;

        LogUtils.setState(BuildConfig.DEBUG);
        Bus.getDefault().setStrictMode(true);
        Bus.getDefault().setDebug(BuildConfig.DEBUG);
    }

    public DisplayImageOptions getDefaultOptions() {
        return defaultOptions.get();
    }

    public ImageLoader getImageLoader() {
        return imageLoader.get();
    }

    public ApiService getApiService(){
        return apiService.get();
    }
}
