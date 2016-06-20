package com.horizon.gank.hgank.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.ui.activity.WebViewActivity;
import com.horizon.gank.hgank.ui.adapter.recyclerview.BaseAdapterHelper;
import com.horizon.gank.hgank.ui.adapter.recyclerview.QuickAdapter;

import java.util.List;

public class GanKNormalAdapter extends QuickAdapter<GanKData> {

    private Context mCxt;
    private String mType;

    public GanKNormalAdapter(Context context, int layoutId, List<GanKData> data, String type) {
        super(context, layoutId, data);
        this.mCxt = context;
        this.mType = type;
    }

    @Override
    public void onBindData(BaseAdapterHelper holder, final GanKData item) {
        holder.setText(R.id.tv_title, item.getDesc())
                .setText(R.id.tv_user, TextUtils.isEmpty(item.getWho()) ? "" : "via. " + item.getWho())
        .setOnClickListener(R.id.ll_list_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCxt, WebViewActivity.class);
                intent.putExtra(Constants.BUNDLE_WEBVIEW_URL, item.getUrl());
                if("休息视频".equals(mType)){
                    intent.putExtra(Constants.BUNDLE_WEBVIEW_VEDIO, true);
                }
                mCxt.startActivity(intent);
            }
        });
    }


}
