package com.horizon.gank.hgank.download;

import com.horizon.gank.hgank.util.BusEvent;
import com.horizon.gank.hgank.util.LogUtils;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;

import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/18.
 */
public abstract class FileCallBack implements Callback<ResponseBody> {

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        Observable.just(response.body().byteStream())
                .onBackpressureBuffer()
                .observeOn(Schedulers.io())
                .subscribe(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream stream) {
                        save(stream);
                    }
                });
    }

    public abstract void save(InputStream is);

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if(t != null){
            t.printStackTrace();
        }
    }
}
