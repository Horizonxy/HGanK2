package com.horizon.gank.hgank.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.horizon.gank.hgank.Application;

/**
 * Created by Administrator on 2016/10/8.
 */
public class TypefaceTextView extends TextView {

    private final static Typeface TYPE_FACE = Typeface.createFromAsset(Application.application.getAssets(), "fonts/yooyoico.ttf");

    public TypefaceTextView(Context context) {
        super(context);
        setTypeface(TYPE_FACE);
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(TYPE_FACE);
    }

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(TYPE_FACE);
    }

}
