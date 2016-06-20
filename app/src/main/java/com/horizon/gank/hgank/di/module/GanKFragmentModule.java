package com.horizon.gank.hgank.di.module;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.di.FragmentScope;
import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.model.data.GanKModel;
import com.horizon.gank.hgank.presenter.GanKPresenter;
import com.horizon.gank.hgank.ui.adapter.GanKAdapter;
import com.horizon.gank.hgank.ui.adapter.GanKNormalAdapter;
import com.horizon.gank.hgank.ui.adapter.recyclerview.DividerItemDecoration;
import com.horizon.gank.hgank.ui.adapter.recyclerview.QuickAdapter;
import com.horizon.gank.hgank.ui.adapter.recyclerview.SpacesItemDecoration;
import com.horizon.gank.hgank.ui.iview.GanKFragmentViewListener;
import com.horizon.gank.hgank.util.DisplayUtils;

import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class GanKFragmentModule {

    private Activity mAty;
    private GanKFragmentViewListener mView;
    private String mType;
    private List<GanKData> mData;

    public GanKFragmentModule(Activity mAty,  GanKFragmentViewListener mView, String mType, List<GanKData> mData){
        this.mAty = mAty;
        this.mView = mView;
        this.mType = mType;
        this.mData = mData;
    }

    @FragmentScope
    @Provides
    public GanKPresenter provideGanKPresenter(){
        return new GanKPresenter(new GanKModel(), mView);
    }

    @FragmentScope
    @Provides
    public QuickAdapter<GanKData> provideGanKAdapter(){
        if(mType.equals("福利")){
            return new GanKAdapter(mAty, R.layout.item_welfare, mData);
        } else {
            return new GanKNormalAdapter(mAty, R.layout.item_type_list, mData, mType);
        }
    }

    @FragmentScope
    @Provides
    public RecyclerView.LayoutManager provideLayoutManager(){
        if(mType.equals("福利")){
            return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            return  new LinearLayoutManager(mAty);
        }
    }

    @FragmentScope
    @Provides
    public RecyclerView.ItemDecoration provideItemDecoration(){
        if(mType.equals("福利")){
            return new SpacesItemDecoration(DisplayUtils.dip2px(mAty, 4));
        } else {
            return new DividerItemDecoration(mAty, LinearLayoutManager.VERTICAL);
        }
    }

}
