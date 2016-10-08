package com.horizon.gank.hgank.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class DisplayUtils {

    private static int mScreenWidth, mScreenHeight;

    public static int screenWidth(Context cxt){
        if(mScreenWidth == 0){
            DisplayMetrics displayMetrics = cxt.getResources().getDisplayMetrics();
            mScreenHeight = displayMetrics.heightPixels;
            mScreenWidth = displayMetrics.widthPixels;
        }
        return mScreenWidth;
    }

    public static int screenHeight(Context cxt){
        if(mScreenHeight == 0){
            screenWidth(cxt);
        }
        return mScreenHeight;
    }

    public static int getStatusBarHeight(Context context){
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 将sp值转换为px值
     */
    @SuppressWarnings("unused")
    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param context
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * @description 设置添加屏幕的背景透明度
     * @author 蒋先明
     * @date 2015年7月15日 上午10:25:33
     * @param bgAlpha
     * @return
     * @lastupdatetime 2015年7月15日 上午10:25:33
     */
    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}
