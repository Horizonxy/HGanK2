package com.horizon.gank.hgank.ui.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class QuickAdapter<T> extends RecyclerView.Adapter<BaseAdapterHelper> {

    private List<T> data;
    private int layoutId;
    private Context context;

    public QuickAdapter(Context context, int layoutId, List<T> data) {
        this.context =  context;
        this.layoutId = layoutId;
        this.data = data;
    }

    @Override
    public BaseAdapterHelper onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new BaseAdapterHelper(itemView);
    }

    @Override
    public void onBindViewHolder(BaseAdapterHelper holder, int position) {
        onBindData(holder, data.get(position));
    }

    public abstract void onBindData(BaseAdapterHelper holder, T item);

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
