package com.horizon.gank.hgank.ui.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.util.BusEvent;
import com.horizon.gank.hgank.util.PreUtils;
import com.horizon.gank.hgank.util.ThemeUtils;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;

public class ColorProgressBar extends ProgressBar {

    public ColorProgressBar(Context context) {
        super(context);
        registerChange();
    }

    public ColorProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        registerChange();
    }

    public ColorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        registerChange();
    }

    protected void registerChange() {
        Bus.getDefault().register(this);
        int theme = PreUtils.getInt(getContext(), Constants.BUNDLE_THEME, R.style.red_theme);
        int color = Constants.Theme.byTheme(theme).getColor();
        getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @BusReceiver
    public void onThemeColorEvent(BusEvent.ThemeColorEvent event){
        getIndeterminateDrawable().setColorFilter(event.getColor(), PorterDuff.Mode.SRC_IN);
    }
}
