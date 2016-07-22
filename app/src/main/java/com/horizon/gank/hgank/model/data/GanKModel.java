package com.horizon.gank.hgank.model.data;

import com.horizon.gank.hgank.model.api.ApiManager;
import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.model.bean.GanKResult;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GanKModel {

    public Subscription getGanKList(String type, int pageNo, Action1<GanKResult<GanKData>> onNext, Action1<Throwable> onError, Subscriber<GanKResult<GanKData>> subscriber){
        return ApiManager.getGanKList(type, pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(onNext)
                .doOnError(onError)
                .subscribe(subscriber);
    }

}
