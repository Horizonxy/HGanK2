package com.horizon.gank.hgank.model.data;

import com.horizon.gank.hgank.model.api.ApiManager;
import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.model.bean.GanKResult;
import com.horizon.gank.hgank.util.RxJavaUtils;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

public class GanKModel {

    public Subscription getGanKList(String type, int pageNo, Action1<GanKResult<GanKData>> onNext, Action1<Throwable> onError, Subscriber<GanKResult<GanKData>> subscriber){
        return RxJavaUtils.schedulersIoMainNextError(ApiManager.getGanKList(type, pageNo), onNext, onError).subscribe(subscriber);
    }

}
