package com.horizon.gank.hgank.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.horizon.gank.hgank.util.BusEvent;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;

public class ColorTextView extends TextView {

    public ColorTextView(Context context) {
        super(context);
        registerChange();
    }

    public ColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        registerChange();
    }

    public ColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        registerChange();
    }

    protected void registerChange() {
        Bus.getDefault().register(this);
    }

    @BusReceiver
    public void onThemeColorEvent(BusEvent.ThemeColorEvent event){
        setTextColor(event.getColor());
    }

}
