package com.horizon.gank.hgank.util;

import rx.Subscriber;

public class SimpleSubscriber<T> extends Subscriber<T> {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(T obj) {
    }
}
