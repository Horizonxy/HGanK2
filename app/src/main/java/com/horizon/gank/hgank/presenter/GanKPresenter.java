package com.horizon.gank.hgank.presenter;

import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.model.bean.GanKResult;
import com.horizon.gank.hgank.model.data.GanKModel;
import com.horizon.gank.hgank.ui.iview.GanKFragmentViewListener;
import com.horizon.gank.hgank.util.SimpleSubscriber;

import java.util.List;

import rx.Subscription;

public class GanKPresenter extends BasePresenter{

    private GanKModel mGank;
    private GanKFragmentViewListener vGank;

    public GanKPresenter(GanKModel mGank, GanKFragmentViewListener vGank){
        this.mGank = mGank;
        this.vGank = vGank;
    }

    public void loadData(){
        final int pageNo = vGank.getPageNo();
        Subscription subscription = mGank.getGanKList(vGank.getType(), pageNo, new SimpleSubscriber<GanKResult<GanKData>>(){

            @Override
            public void onError(Throwable e) {
                vGank.onFailure();
            }

            @Override
            public void onNext(GanKResult<GanKData> obj) {
                if (obj.isError()) {
                    vGank.onFailure();
                } else{
                    List<GanKData> data = obj.getResults();
                    if(data != null && data.size() > 0){
                        vGank.onSuccess(data);
                        vGank.onCompleted(pageNo);
                    } else {
                        vGank.onFinish();
                    }
                }
            }
        });

        addSubscription(subscription);
    }

}
