package com.horizon.gank.hgank.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.ui.activity.WebViewActivity;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class RecyclerNormalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GanKData> mDatas;
    private LayoutInflater mInflater;
    private Context mCxt;
    private String mType;

    public RecyclerNormalAdapter(Context context, List<GanKData> data, String type) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = data;
        this.mType = type;
        this.mCxt = context;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ChildViewHolder holder = (ChildViewHolder) viewHolder;
        final GanKData item = mDatas.get(position);

        holder.tvDesc.setText(item.getDesc() == null ? "" : item.getDesc());
        holder.tvWho.setText(item.getWho() == null ? "" : "via. "+item.getWho());

        RxView.clicks(holder.vItem)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(mCxt, WebViewActivity.class);
                        intent.putExtra(Constants.BUNDLE_WEBVIEW_URL, item.getUrl());
                        if ("休息视频".equals(mType)) {
                            intent.putExtra(Constants.BUNDLE_WEBVIEW_VEDIO, true);
                        }
                        mCxt.startActivity(intent);
                    }
                });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewHolder, int position) {
        View view = mInflater.inflate(R.layout.item_type_list, viewHolder, false);
        return new ChildViewHolder(view);
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_user)
        TextView tvWho;
        @Bind(R.id.tv_title)
        TextView tvDesc;
        @Bind(R.id.ll_list_item)
        View vItem;

        public ChildViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}