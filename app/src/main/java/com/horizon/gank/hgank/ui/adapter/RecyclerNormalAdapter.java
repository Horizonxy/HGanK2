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

import java.util.List;

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
        holder.vItem.setOnClickListener(new ImageClickListener(item));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewHolder, int position) {
        View view = mInflater.inflate(R.layout.item_type_list, viewHolder, false);
        return new ChildViewHolder(view);
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.tv_user)
        TextView tvWho;
//        @Bind(R.id.tv_title)
        TextView tvDesc;
//        @Bind(R.id.ll_list_item)
        View vItem;

        public ChildViewHolder(View view) {
            super(view);
//            ButterKnife.bind(view);
            tvWho = (TextView) view.findViewById(R.id.tv_user);
            tvDesc = (TextView) view.findViewById(R.id.tv_title);
            vItem =  view.findViewById(R.id.ll_list_item);
        }

    }

    class ImageClickListener implements View.OnClickListener {

        private GanKData data;

        public ImageClickListener( GanKData data) {
            this.data = data;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mCxt, WebViewActivity.class);
            intent.putExtra(Constants.BUNDLE_WEBVIEW_URL, data.getUrl());
            if ("休息视频".equals(mType)) {
                intent.putExtra(Constants.BUNDLE_WEBVIEW_VEDIO, true);
            }
            mCxt.startActivity(intent);
        }
    }
}