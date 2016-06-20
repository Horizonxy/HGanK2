package com.horizon.gank.hgank.ui.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import android.widget.ImageView;

import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.ui.activity.PictureDetailActivity;
import com.horizon.gank.hgank.ui.adapter.recyclerview.BaseAdapterHelper;
import com.horizon.gank.hgank.ui.adapter.recyclerview.QuickAdapter;
import com.horizon.gank.hgank.util.SmallPicInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

public class GanKAdapter extends QuickAdapter<GanKData> {

    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private Context mCxt;

    public GanKAdapter(Context context, int layoutId, List<GanKData> data) {
        super(context, layoutId, data);
        this.mCxt = context;

        mImageLoader = Application.application.getImageLoader();
        mOptions = Application.application.getDefaultOptions();
    }

    @Override
    public void onBindData(final BaseAdapterHelper holder, GanKData item) {
        final ImageView imageView = holder.getView(R.id.item_iv_welfare);
        mImageLoader.displayImage(item.getUrl(), imageView, mOptions, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                int width = loadedImage.getWidth();
                int height = loadedImage.getHeight();
                float scale = Application.application.SCREENWIDTH/2 * 1f / width;

                Matrix matrix = new Matrix();
                matrix.setScale(scale,scale);
                Bitmap bitmap = Bitmap.createBitmap(loadedImage, 0, 0, width, height, matrix, false);
                imageView.setImageBitmap(bitmap);
            }
        });

        holder.setText(R.id.item_tv_desc, item.getDesc()).setText(R.id.item_tv_user, item.getWho())
        .setOnClickListener(R.id.card_view, new ImageClickListener(imageView, item));
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
            ((Activity)mCxt).overridePendingTransition(0, 0);

            imageView.setDrawingCacheEnabled(false);
        }
    }

}
