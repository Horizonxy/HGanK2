package com.horizon.gank.hgank.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

public class DrawableUtils {

    public static void setImageDrawable(ImageView imageView, MaterialDesignIconic.Icon icon, int size){
        imageView.setImageDrawable(new IconicsDrawable(imageView.getContext()).color(Color.WHITE).icon(icon).sizeDp(size));
    }

    public static void setImageDrawable(ImageView imageView, MaterialDesignIconic.Icon icon){
        imageView.setImageDrawable(new IconicsDrawable(imageView.getContext()).color(Color.WHITE).icon(icon).sizeDp(30));
    }

    public static void setImageDrawable(ImageView imageView, MaterialDesignIconic.Icon icon, int size, int color){
        imageView.setImageDrawable(new IconicsDrawable(imageView.getContext()).color(color).icon(icon).sizeDp(size));
    }

    public static Drawable getDrawable(Context cxt, MaterialDesignIconic.Icon icon){
        return new IconicsDrawable(cxt).color(Color.WHITE).icon(icon).sizeDp(30);
    }
}
