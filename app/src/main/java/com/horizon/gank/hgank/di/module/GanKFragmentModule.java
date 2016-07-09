package com.horizon.gank.hgank.di.module;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.horizon.gank.hgank.di.FragmentScope;
import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.model.data.GanKModel;
import com.horizon.gank.hgank.presenter.GanKPresenter;
import com.horizon.gank.hgank.ui.adapter.RecyclerAdapter;
import com.horizon.gank.hgank.ui.adapter.RecyclerNormalAdapter;
import com.horizon.gank.hgank.ui.adapter.recyclerview.DividerItemDecoration;
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
    public RecyclerAdapterWithHF provideRecyclerAdapterWithHF(){
        if(mType.equals("福利")){
            return new RecyclerAdapterWithHF(new RecyclerAdapter(mAty, mData));
        } else {
            return new RecyclerAdapterWithHF(new RecyclerNormalAdapter(mAty, mData, mType));
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
