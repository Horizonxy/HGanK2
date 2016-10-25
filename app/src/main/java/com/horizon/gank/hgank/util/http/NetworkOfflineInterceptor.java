package com.horizon.gank.hgank.util.http;

import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.util.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 无网络时读取缓存
 * @author 蒋先明
 * @date 2016/8/28
 */
public class NetworkOfflineInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetUtils.isNetworkConnected(Application.application.getApplicationContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        return chain.proceed(request);
    }
}
