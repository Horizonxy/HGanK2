package com.horizon.gank.hgank.model.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2016/8/18.
 */
public interface DownLoadApiServide {

    @GET("app-debug.apk")
    Call<ResponseBody> loadFile();
}
