package com.horizon.gank.hgank.di.module;

import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.model.api.ApiService;
import com.horizon.gank.hgank.util.RetrofitUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class ApiModule {

    @Singleton
    @Provides
    public Retrofit provideRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .client(RetrofitUtil.createOkHttpClient())
                .baseUrl(Constants.END_POIND)
                .addConverterFactory(GsonConverterFactory.create(RetrofitUtil.createGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    @Singleton
    @Provides
    public ApiService provideApiService(Retrofit retrofit){
        return  retrofit.create(ApiService.class);
    }

}
