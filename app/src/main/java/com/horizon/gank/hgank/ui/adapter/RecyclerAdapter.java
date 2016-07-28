package com.horizon.gank.hgank.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.ui.activity.PictureDetailActivity;
import com.horizon.gank.hgank.util.DisplayUtils;
import com.horizon.gank.hgank.util.SmallPicInfo;
import com.jakewharton.rxbinding.view.RxView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.functions.Func1;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GanKData> mDatas;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private Context mCxt;

    public RecyclerAdapter(Context context, List<GanKData> data) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = data;
        this.mCxt = context;

        mImageLoader = Application.application.getImageLoader();
        mOptions = Application.application.getDefaultOptions();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ChildViewHolder holder = (ChildViewHolder) viewHolder;
        final GanKData item = mDatas.get(position);

        final ImageView imageView = holder.ivWelfare;
        mImageLoader.displayImage(item.getUrl(), holder.ivWelfare, mOptions, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                int width = loadedImage.getWidth();
                int height = loadedImage.getHeight();
                float scale = DisplayUtils.screenWidth(mCxt) / 2 * 1f / width;

                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);
                Bitmap bitmap = Bitmap.createBitmap(loadedImage, 0, 0, width, height, matrix, false);
                imageView.setImageBitmap(bitmap);
            }
        });
        holder.tvWho.setText(item.getWho() == null ? "" : "via. "+item.getWho());
        holder.tvDesc.setText(item.getDesc() == null ? "" : item.getDesc());

        RxView.clicks(holder.vCard)
                .throttleFirst(1, TimeUnit.SECONDS)
                .map(new Func1<Void, SmallPicInfo>() {
                    @Override
                    public SmallPicInfo call(Void aVoid) {
                        int[] screenLocation = new int[2];
                        imageView.getLocationOnScreen(screenLocation);
                        SmallPicInfo info = new SmallPicInfo(item, screenLocation[0], screenLocation[1], imageView.getWidth(), imageView.getHeight(), 0);

                        PictureDetailActivity.bmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                        return info;
                    }
                })
                .filter(new Func1<SmallPicInfo, Boolean>() {
                    @Override
                    public Boolean call(SmallPicInfo smallPicInfo) {
                        return smallPicInfo != null;
                    }
                })
                .subscribe(new Action1<SmallPicInfo>() {
                    @Override
                    public void call(SmallPicInfo smallPicInfo) {
                        Intent intent = new Intent(mCxt, PictureDetailActivity.class);
                        intent.putExtra(Constants.BUNDLE_PIC_INFOS, smallPicInfo);
                        mCxt.startActivity(intent);
                        ((Activity) mCxt).overridePendingTransition(0, 0);
                    }
                });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewHolder, int position) {
        View view = mInflater.inflate(R.layout.item_welfare, viewHolder, false);
        return new ChildViewHolder(view);
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_iv_welfare)
        ImageView ivWelfare;
        @Bind(R.id.item_tv_user)
        TextView tvWho;
        @Bind(R.id.item_tv_desc)
        TextView tvDesc;
        @Bind(R.id.card_view)
        View vCard;

        public ChildViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}