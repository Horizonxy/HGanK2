package com.horizon.gank.hgank;

import com.horizon.gank.hgank.di.component.DaggerAppComponent;
import com.horizon.gank.hgank.di.module.ApiModule;
import com.horizon.gank.hgank.di.module.AppModule;
import com.horizon.gank.hgank.model.api.ApiService;
import com.mcxiaoke.bus.Bus;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

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
        UMShareAPI.get(this);
        DaggerAppComponent.builder().apiModule(new ApiModule()).appModule(new AppModule(this)).build().inject(this);
        application = this;
        Logger.init(getResources().getString(R.string.app_name));

        Bus.getDefault().setStrictMode(true);
        Bus.getDefault().setDebug(BuildConfig.DEBUG);
    }

    //各个平台的配置
    {
        PlatformConfig.setWeixin("wx6a339bbc5745b8e3", "adc1430044f7dae9e3bae94395641d82");
        PlatformConfig.setQQZone("1105462216", "bES9PBXDeWAj2bmq");
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
