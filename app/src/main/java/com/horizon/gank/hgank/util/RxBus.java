package com.horizon.gank.hgank.util;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

    private static volatile RxBus mInstance;
    private final Subject<Object, Object> BUS;

    public RxBus() {
        BUS = new SerializedSubject<Object, Object>(PublishSubject.create());
    }

    public static RxBus getInstance() {
        if(mInstance == null){
            synchronized (RxBus.class) {
                if(mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }

    public void send(Object obj){
        BUS.onNext(obj);
    }

    public <T> Observable<T> toObservable(Class<T>  clazz){
        return BUS.ofType(clazz);
    }


    public static class NetEvent{
        private boolean hasNet;

        public boolean isHasNet() {
            return hasNet;
        }

        public void setHasNet(boolean hasNet) {
            this.hasNet = hasNet;
        }
    }
}
