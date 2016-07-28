package com.horizon.gank.hgank.presenter;

import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.model.bean.GanKResult;
import com.horizon.gank.hgank.model.data.GanKModel;
import com.horizon.gank.hgank.ui.iview.GanKFragmentViewListener;
import com.horizon.gank.hgank.util.SimpleSubscriber;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

public class GanKPresenter extends BasePresenter{

    private GanKModel mGank;
    private GanKFragmentViewListener vGank;

    public GanKPresenter(GanKModel mGank, GanKFragmentViewListener vGank){
        this.mGank = mGank;
        this.vGank = vGank;
    }

    public void loadData(){
        final int pageNo = vGank.getPageNo();
        Subscription subscription = mGank.getGanKList(
                vGank.getType(),
                pageNo,
                new Action1<GanKResult<GanKData>>() {
                    @Override
                    public void call(GanKResult<GanKData> ganKDataGanKResult) {
                        vGank.onCompleted(pageNo);
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        vGank.onCompleted(pageNo);
                    }
                },
                new SimpleSubscriber<GanKResult<GanKData>>() {

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        vGank.onFailure();
                    }

                    @Override
                    public void onNextRx(GanKResult<GanKData> obj) {
                        if(obj.isError()){
                            vGank.onFailure();
                        } else {
                            dealData(obj.getResults());
                        }
                    }
                });

        addSubscription(subscription);
    }

    private void dealData(List<GanKData> data){
        if (data == null) {
            vGank.onFailure();
            return;
        }
        if (data.size() > 0) {
            vGank.onSuccess(data);
        } else {
            vGank.onFinish();
        }
    }
}
