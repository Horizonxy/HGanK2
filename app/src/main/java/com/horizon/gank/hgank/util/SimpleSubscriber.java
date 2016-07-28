package com.horizon.gank.hgank.util;

import rx.Subscriber;

public abstract class SimpleSubscriber<T> extends Subscriber<T> {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        if(e != null){
            e.printStackTrace();
        }
    }

    @Override
    public void onNext(T obj) {
        try {
            onNextRx(obj);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public abstract void onNextRx(T obj);
}
