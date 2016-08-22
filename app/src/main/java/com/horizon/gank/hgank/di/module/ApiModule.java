package com.horizon.gank.hgank.di.module;

import android.Manifest;
import android.graphics.Bitmap;

import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.model.api.ApiService;
import com.horizon.gank.hgank.util.FileUtils;
import com.horizon.gank.hgank.util.PermissionUtils;
import com.horizon.gank.hgank.util.RetrofitUtil;
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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


@Module
public class ApiModule {

    @Singleton
    @Provides
    public ApiService provideApiService(){
        return  RetrofitUtil.createRetrofit().create(ApiService.class);
    }
}
