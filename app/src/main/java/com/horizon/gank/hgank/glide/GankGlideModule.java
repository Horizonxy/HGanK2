package com.horizon.gank.hgank.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.util.FileUtils;

import java.io.File;

public class GankGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        final String IMG_CACHE_PATH = FileUtils.getEnvPath(context, true, Constants.IMG_CACHE_DIR);
        File imgFile = new File(IMG_CACHE_PATH);
        if(!imgFile.exists()){
            imgFile.mkdirs();
        }
        builder.setDiskCache(new DiskLruCacheFactory(IMG_CACHE_PATH, 100*1023*1024));

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        int customMemoryCacheSize = (int) (1.5 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.5 * defaultBitmapPoolSize);
        builder.setMemoryCache( new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool( new LruBitmapPool(customBitmapPoolSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}
