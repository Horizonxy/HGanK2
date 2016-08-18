package com.horizon.gank.hgank.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.download.DownLoadInterceptor;
import com.horizon.gank.hgank.model.api.DownLoadApiServide;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitUtil {

	public static OkHttpClient createOkHttpClient(){
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

		Interceptor header = new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
					Request request = chain.request();
					request.newBuilder()
							.removeHeader("User-Agent")
							.addHeader("User-Agent", System.getProperty("http.agent", ""))
							.build();
					return chain.proceed(request);
			}
		};

		OkHttpClient client = new OkHttpClient.Builder()
				.addInterceptor(header)
				.addInterceptor(new DownLoadInterceptor())
				.addInterceptor(logging)
				.connectTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
				.writeTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
				.readTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
				.build();

		return client;
	}

	public static Retrofit createRetrofit(){
		Retrofit retrofit = new Retrofit.Builder()
				.client(RetrofitUtil.createOkHttpClient())
				.baseUrl(Constants.END_POIND)
				.addConverterFactory(GsonConverterFactory.create())
				.addConverterFactory(ScalarsConverterFactory.create())
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.build();
		return retrofit;
	}
	
	public static Gson createGson(){
		GsonBuilder builder = GsonUtils.GSON_BUILDER;

		return builder.create();
	}

	public static DownLoadApiServide createDownLoadApi(){
		Retrofit retrofit = new Retrofit.Builder()
				.client(RetrofitUtil.createOkHttpClient())
				.baseUrl(Constants.DOWNLOAD_POIND)
				.build();
		return retrofit.create(DownLoadApiServide.class);
	}
}
