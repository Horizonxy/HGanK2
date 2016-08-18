package com.horizon.gank.hgank.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/18.
 */
public class DownLoadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        return response.newBuilder().body(new FileResponseBody(response)).build();
    }
}
