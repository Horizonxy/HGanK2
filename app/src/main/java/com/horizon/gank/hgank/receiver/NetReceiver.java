package com.horizon.gank.hgank.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.horizon.gank.hgank.util.LogUtils;
import com.horizon.gank.hgank.util.NetUtils;
import com.horizon.gank.hgank.util.SimpleSubscriber;

import rx.Observable;
import rx.functions.Func1;

public class NetReceiver extends BroadcastReceiver {

    private OnNetListener mListener;

    public void setListener(OnNetListener mListener){
        this.mListener = mListener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Observable.just(intent)
                .filter(new Func1<Intent, Boolean>() {
                    @Override
                    public Boolean call(Intent intent) {
                        return intent != null;
                    }
                })
                .map(new Func1<Intent, String>() {
                    @Override
                    public String call(Intent intent) {
                        return intent.getAction();
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return ConnectivityManager.CONNECTIVITY_ACTION.equals(s);
                    }
                }).subscribe(new SimpleSubscriber<String>(){
            @Override
            public void onNext(String obj) {
                LogUtils.e("mListener == null? "+(mListener == null)+" obj: "+obj);
                if(!NetUtils.isNetworkConnected(mListener.getCxt())){
                    mListener.hasNet();
                } else {
                    mListener.noNet();
                }
            }
        });
    }

    public interface OnNetListener {
        Context getCxt();
        void hasNet();
        void noNet();
    }
}
