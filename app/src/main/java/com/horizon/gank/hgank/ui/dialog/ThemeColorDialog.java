package com.horizon.gank.hgank.ui.dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.ui.adapter.ColorSelectAdapter;
import com.horizon.gank.hgank.util.BusEvent;
import com.horizon.gank.hgank.util.PreUtils;
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
                        if(dialog != null) {
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
                            PreUtils.putInt(aty, Constants.BUNDLE_OLD_THEME_COLOR, ThemeUtils.getThemeColor(aty, R.attr.colorPrimary));
                            aty.setTheme(theme.getTheme());
                            PreUtils.putInt(aty, Constants.BUNDLE_THEME, theme.getTheme());

                            BusEvent.ThemeColorEvent colorEvent = new BusEvent.ThemeColorEvent();
                            colorEvent.setColor(theme.getColor());
                            Bus.getDefault().post(colorEvent);
                        }
                        if(dialog != null) {
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



}
