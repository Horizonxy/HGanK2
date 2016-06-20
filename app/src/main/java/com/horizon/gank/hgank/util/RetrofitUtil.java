package com.horizon.gank.hgank.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class RetrofitUtil {

	public static OkHttpClient createOkHttpClient(){
		Interceptor interceptor = new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				Request newRequest = chain.request()
						.newBuilder()
						.addHeader("User-Agent", "YooYo/1.0")
						.build();
			    return chain.proceed(newRequest);
			}

		};
		OkHttpClient client = new OkHttpClient();
		client.interceptors().add(interceptor);
		
		return client;
	}
	
	public static Gson createGson(){
		GsonBuilder builder = GsonUtils.GSON_BUILDER;

		return builder.create();
	}
	
}
