package com.horizon.gank.hgank.presenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter {

    CompositeSubscription mCompositeSubscription;

    public void addSubscription(Subscription subscription) {
        if(mCompositeSubscription == null || mCompositeSubscription.isUnsubscribed()){
            mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(subscription);
    }

    public void onDestroy() {
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
}
