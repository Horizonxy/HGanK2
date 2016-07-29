package com.horizon.gank.hgank.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.ui.widget.ColorSelectView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ColorSelectAdapter extends BaseAdapter {

    private List<Constants.Theme> themes;
    private int current;
    private Context cxt;

    public ColorSelectAdapter(Context cxt, List<Constants.Theme> themes, int current){
        this.themes = themes;
        this.current = current;
        this.cxt = cxt;
    }

    @Override
    public int getCount() {
        return themes.size();
    }

    @Override
    public Object getItem(int i) {
        return themes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setCurrent(int current){
        this.current = current;
        notifyDataSetChanged();
    }

    public Constants.Theme getSelectTheme(){
        return themes.get(current);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view == null){
            view = LayoutInflater.from(cxt).inflate(R.layout.item_color_select, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.selectView.setSelected(i == current);
        holder.selectView.setColor(themes.get(i).getColor());

        return view;
    }

    class Holder{
        @Bind(R.id.item_color)
        ColorSelectView selectView;

        public Holder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
