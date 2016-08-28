package com.horizon.gank.hgank.util.http;

import com.horizon.gank.hgank.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * http缓存
 * @author 蒋先明
 * @date 2016/8/28
 */
public class NetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.header("Cache-Control") == null) {
            String cache = chain.request().header("cache");
            if (cache == null || "".equals(cache)) {
                cache = Constants.CACHE_TIME + "";
            }
            response = response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + cache)
                    .build();
            return response;
        } else {
            return response;
        }
    }
}
