package com.horizon.gank.hgank.ui.iview;

import com.horizon.gank.hgank.model.bean.GanKData;

import java.util.List;

public interface GanKFragmentViewListener {

    int getPageNo();
    String getType();

    void onFailure();
    void onSuccess(List<GanKData> data);
    void onFinish();
    void onCompleted(int pageNo);
}
