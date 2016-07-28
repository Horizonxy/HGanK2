package com.horizon.gank.hgank.ui.widget;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ProgressBar;

public class ColorHorizontalProgressBar extends ProgressBar {

    public ColorHorizontalProgressBar(Context context) {
        super(context);
    }

    public ColorHorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorHorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setProgressColor(int color){
        ClipDrawable drawable = new ClipDrawable(new ColorDrawable(color), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        setProgressDrawable(drawable);
    }
}
