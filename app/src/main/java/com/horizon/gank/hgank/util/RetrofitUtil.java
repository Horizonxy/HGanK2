package com.horizon.gank.hgank.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class RetrofitUtil {

	public static OkHttpClient createOkHttpClient(){
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

		Interceptor header = new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				Request newRequest = chain.request()
						.newBuilder()
						.removeHeader("User-Agent")
						.addHeader("User-Agent", "YooYo/1.0")
						.build();
				return chain.proceed(newRequest);
			}

		};

		OkHttpClient client = new OkHttpClient.Builder()
				.addInterceptor(header)
				.addInterceptor(logging)
				.build();

		return client;
	}
	
	public static Gson createGson(){
		GsonBuilder builder = GsonUtils.GSON_BUILDER;

		return builder.create();
	}
	
}
