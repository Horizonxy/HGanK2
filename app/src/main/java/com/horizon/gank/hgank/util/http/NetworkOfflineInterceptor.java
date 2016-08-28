package com.horizon.gank.hgank.util.http;

import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.util.NetUtils;

import java.io.IOException;

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
        //离线的时候为CACHE_TIME天的缓存。
        if (!NetUtils.isNetworkConnected(Application.application.getApplicationContext())) {
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + Constants.CACHE_TIME)
                    .build();
        }
        return chain.proceed(request);
    }
}
