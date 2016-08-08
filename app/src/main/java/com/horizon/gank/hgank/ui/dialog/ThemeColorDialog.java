package com.horizon.gank.hgank.ui.dialog;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.ui.adapter.ColorSelectAdapter;
import com.horizon.gank.hgank.util.BusEvent;
import com.horizon.gank.hgank.util.PreUtils;
import com.horizon.gank.hgank.util.SimpleAnimatorListener;
import com.horizon.gank.hgank.util.SystemStatusManager;
import com.horizon.gank.hgank.util.ThemeUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.AdapterViewItemClickEvent;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.mcxiaoke.bus.Bus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ThemeColorDialog {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    public ThemeColorDialog(final Activity aty){
        View view = aty.getLayoutInflater().inflate(R.layout.view_color_select, null, false);
        GridView gridView = (GridView) view.findViewById(R.id.gv_color_select);
        final TextView tvPositive = (TextView) view.findViewById(R.id.positive);
        TextView tvNegative = (TextView) view.findViewById(R.id.negative);

        final int color = ThemeUtils.getThemeColor(aty, R.attr.colorPrimary);
        List<Constants.Theme> themes = Constants.Theme.list();

        final ColorSelectAdapter adapter = new ColorSelectAdapter(aty, themes, themes.indexOf(Constants.Theme.byColor(color)));
        gridView.setAdapter(adapter);
        RxAdapterView.itemClickEvents(gridView).throttleFirst(400, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AdapterViewItemClickEvent>() {
                    @Override
                    public void call(AdapterViewItemClickEvent adapterViewItemClickEvent) {
                        int position = adapterViewItemClickEvent.position();
                        adapter.setCurrent(position);
                        Constants.Theme theme = adapter.getSelectTheme();
                        tvPositive.setTextColor(theme.getColor());
                    }
                });

        tvNegative.setTextColor(PreUtils.getInt(aty, Constants.BUNDLE_OLD_THEME_COLOR, aty.getResources().getColor(R.color.blue)));
        RxView.clicks(tvNegative).throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(dialog != null && aty != null & !aty.isFinishing()) {
                            dialog.dismiss();
                        }
                    }
                });
        RxView.clicks(tvPositive).throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Constants.Theme theme = adapter.getSelectTheme();
                        if(theme.getColor() != color){
                            changeAnimator(aty, theme);

                            PreUtils.putInt(aty, Constants.BUNDLE_OLD_THEME_COLOR, ThemeUtils.getThemeColor(aty, R.attr.colorPrimary));
                            aty.setTheme(theme.getTheme());
                            PreUtils.putInt(aty, Constants.BUNDLE_THEME, theme.getTheme());

                            BusEvent.ThemeColorEvent colorEvent = new BusEvent.ThemeColorEvent();
                            colorEvent.setColor(theme.getColor());
                            Bus.getDefault().post(colorEvent);
                        }
                        if(dialog != null && aty != null & !aty.isFinishing()) {
                            dialog.dismiss();
                        }
                    }
                });

        builder = new AlertDialog.Builder(aty);
        builder.setView(view);
    }

    public void show(){
        dialog = builder.show();
    }

    private void changeAnimator(final Activity aty, final Constants.Theme theme){
        final View decorView = aty.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache(true);
        Bitmap localBitmap = Bitmap.createBitmap(decorView.getDrawingCache());
        decorView.setDrawingCacheEnabled(false);
        final View tmpView = new View(aty);
        tmpView.setBackgroundDrawable(new BitmapDrawable(aty.getResources(), localBitmap));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ((ViewGroup) decorView).addView(tmpView, params);;

        ValueAnimator animator = new ValueAnimator().ofFloat(1, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private SystemStatusManager manager;
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpha = (float) valueAnimator.getAnimatedValue();

                int status = (int) (0xff * (alpha));
                int color = (status << 24) ^ theme.getColor();
                if(manager == null){
                    manager = new SystemStatusManager(aty);
                    manager.setStatusBarTintEnabled(true);
                }
                manager.setStatusBarTintColor(color);
                tmpView.setAlpha(alpha);
            }
        });
        animator.addListener(new SimpleAnimatorListener(){
            @Override
            public void onAnimationEnd(Animator animation) {
                ((ViewGroup) decorView).removeView(tmpView);
                SystemStatusManager.setTranslucentStatusColor(aty, theme.getColor());
            }
        });
        animator.setDuration(2000);
        animator.start();
    }
}
