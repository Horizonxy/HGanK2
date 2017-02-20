package com.horizon.gank.hgank.di.module;

import com.horizon.gank.hgank.model.api.ApiService;
import com.horizon.gank.hgank.util.RetrofitUtil;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {

    @Singleton
    @Provides
    public ApiService provideApiService(){
        return  RetrofitUtil.createRetrofit().create(ApiService.class);
    }
}
