package com.horizon.gank.hgank.util.http;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http user-agent 拦截器
 * @author 蒋先明
 * @date 2016/8/28
 */
public class UserAgentInterceptor implements Interceptor {

    public String userAgent;

    public UserAgentInterceptor(String userAgent){
        this.userAgent = userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!TextUtils.isEmpty(userAgent)) {
            request.newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", userAgent)
                    .build();
        }
        return chain.proceed(request);
    }
}
