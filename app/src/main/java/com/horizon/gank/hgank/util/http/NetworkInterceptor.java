package com.horizon.gank.hgank.util.http;

import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.util.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http缓存
 * @author 蒋先明
 * @date 2016/8/28
 */
public class NetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetUtils.isNetworkConnected(Application.application.getApplicationContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (NetUtils.isNetworkConnected(Application.application.getApplicationContext())) {
            response.newBuilder()
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=0")
                    .removeHeader("Pragma")
                    .build();
        } else {
            response.newBuilder()
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + Constants.CACHE_TIME)
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }

}
