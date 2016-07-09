package com.horizon.gank.hgank.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import com.horizon.gank.hgank.util.SmallPicInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

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
                float scale = Application.application.SCREENWIDTH / 2 * 1f / width;

                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);
                Bitmap bitmap = Bitmap.createBitmap(loadedImage, 0, 0, width, height, matrix, false);
                imageView.setImageBitmap(bitmap);
            }
        });
        holder.tvWho.setText(item.getWho() == null ? "" : "via. "+item.getWho());
        holder.tvDesc.setText(item.getDesc() == null ? "" : item.getDesc());
        holder.vCard.setOnClickListener(new ImageClickListener(imageView, item));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewHolder, int position) {
        View view = mInflater.inflate(R.layout.item_welfare, viewHolder, false);
        return new ChildViewHolder(view);
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.item_iv_welfare)
        ImageView ivWelfare;
//        @Bind(R.id.tv_desc)
        TextView tvWho;
        TextView tvDesc;
//        @Bind(R.id.card_view)
        View vCard;

        public ChildViewHolder(View view) {
            super(view);
//            ButterKnife.bind(view);
            ivWelfare = (ImageView) view.findViewById(R.id.item_iv_welfare);
            tvDesc = (TextView) view.findViewById(R.id.item_tv_desc);
            vCard = view.findViewById(R.id.card_view);
            tvWho = (TextView) view.findViewById(R.id.item_tv_user);
        }
    }

    class ImageClickListener implements View.OnClickListener {

        private ImageView imageView;
        private GanKData data;

        public ImageClickListener(ImageView imageView, GanKData data) {
            this.imageView = imageView;
            this.data = data;
        }

        @Override
        public void onClick(View view) {
            imageView.setDrawingCacheEnabled(true);
            Bitmap bitmap = imageView.getDrawingCache();

            int[] screenLocation = new int[2];
            imageView.getLocationOnScreen(screenLocation);

            SmallPicInfo info = new SmallPicInfo(data, screenLocation[0], screenLocation[1], imageView.getWidth(), imageView.getHeight(), 0, Bitmap.createBitmap(bitmap));

            Intent intent = new Intent(mCxt, PictureDetailActivity.class);
            intent.putExtra(Constants.BUNDLE_PIC_INFOS, info);
            mCxt.startActivity(intent);
            ((Activity) mCxt).overridePendingTransition(0, 0);

            imageView.setDrawingCacheEnabled(false);
        }
    }
}