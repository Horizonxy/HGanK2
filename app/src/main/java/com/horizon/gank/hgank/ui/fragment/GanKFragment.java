package com.horizon.gank.hgank.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.di.component.DaggerGanKFragmentComponent;
import com.horizon.gank.hgank.di.module.GanKFragmentModule;
import com.horizon.gank.hgank.model.bean.CommonCacheVo;
import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.model.db.CommonDaoImpl;
import com.horizon.gank.hgank.presenter.GanKPresenter;
import com.horizon.gank.hgank.ui.iview.GanKFragmentViewListener;
import com.horizon.gank.hgank.util.GsonUtils;
import com.horizon.gank.hgank.util.NetUtils;
import com.horizon.gank.hgank.util.SimpleSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class GanKFragment extends Fragment implements GanKFragmentViewListener {

    private String mType;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh_layout)
    PtrClassicFrameLayout mRefreshLayout;
    @Inject
    RecyclerAdapterWithHF mAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemDecoration mItemDecoration;
    @Inject
    GanKPresenter mPresenter;

    private List<GanKData> mData;
    private int mPageNo;
    private View mRootView;
    private CommonDaoImpl mCommonDao;
    private Map<String, Object> mCacheMap = new HashMap<String, Object>();
    private String ATY = "gank_type_list_";
    private String DATA_TYPE;
    private Handler mHandler;

    public static GanKFragment newInstance(String type) {
        GanKFragment fragment = new GanKFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_FRAGMENT_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(Constants.BUNDLE_FRAGMENT_TYPE);
        }
        mPageNo = 1;
        DaggerGanKFragmentComponent.builder().ganKFragmentModule(new GanKFragmentModule(getActivity(), this, mType, mData = new ArrayList<GanKData>())).build().inject(this);

        ATY = ATY.concat(mType);
        DATA_TYPE = mType;
        mCommonDao = new CommonDaoImpl(getContext());
        mCacheMap.put(CommonCacheVo.ATY, ATY);
        mCacheMap.put(CommonCacheVo.DATA_TYPE, DATA_TYPE);

        mHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_gank, container, false);
            ButterKnife.bind(this, mRootView);

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(mItemDecoration);
            mRecyclerView.clearAnimation();

            mRefreshLayout.setLastUpdateTimeRelateObject(this);
            mRefreshLayout.setPtrHandler(new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    mPageNo = 1;
                    loadGankData();
                }
            });
            mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void loadMore() {
                    loadGankData();
                }
            });
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.autoRefresh(true);
                }
            }, 150);
        }
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mRootView) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }
    }

    @Override
    public int getPageNo() {
        return mPageNo;
    }

    @Override
    public String getType() {
        return mType;
    }

    @Override
    public void onFailure() {
        Snackbar.make(mRefreshLayout, "加载出问题了~~", Snackbar.LENGTH_SHORT).show();
        mRefreshLayout.loadMoreComplete(false);
    }

    @Override
    public void onSuccess(List<GanKData> data) {
        if (mPageNo == 1) {
            mData.clear();
        }
        mData.addAll(data);
        mAdapter.notifyItemRangeChangedHF(mData.size() - data.size(), data.size());

        if (mPageNo == 1) {
            Map<String, Object> delMap = new HashMap<String, Object>();
            delMap.put(CommonCacheVo.ATY, ATY);
            delMap.put(CommonCacheVo.DATA_TYPE, DATA_TYPE);
            mCommonDao.deleteByColumns(delMap);
        }

        CommonCacheVo cache = new CommonCacheVo();
        cache.setAty(ATY);
        cache.setData(GsonUtils.getString(data));
        cache.setData_type(DATA_TYPE);
        cache.setData_page_no(mPageNo);
        mCommonDao.save(cache);

        mPageNo++;
    }

    @Override
    public void onFinish() {
        Snackbar.make(mRefreshLayout, "没有更多数据了", Snackbar.LENGTH_SHORT).show();
        mRefreshLayout.setNoMoreData();
    }

    @Override
    public void onCompleted(int pageNo) {
        if(pageNo == 1){
            mRefreshLayout.refreshComplete();
        } else {
            mRefreshLayout.loadMoreComplete(true);
        }
        mRefreshLayout.setLoadMoreEnable(true);
    }

    private void loadGankData() {
        if (NetUtils.isNetworkConnected(getContext())) {

            mPresenter.loadData();
        } else {

            loadCache();
        }
    }

    private void loadCache(){
        final int pageNo = mPageNo;
        rx.Observable.just(pageNo)
                .map(new Func1<Integer,  List<GanKData>>() {
                    @Override
                    public  List<GanKData> call(Integer integer) {
                        mCacheMap.put(CommonCacheVo.DATA_PAGE_NO, integer);
                        List<CommonCacheVo> commonlist = mCommonDao.findByColumns(mCacheMap);
                        if (commonlist != null && !commonlist.isEmpty()) {
                            List<GanKData> list = GsonUtils.getList(commonlist.get(0).getData(), GanKData.class);
                            return list;
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<List<GanKData>>(){
                    @Override
                    public void onNext(List<GanKData> list) {
                        if(list != null){
                            if (pageNo == 1) {
                                mData.clear();
                            }
                            mData.addAll(list);
                            mAdapter.notifyItemRangeChangedHF(mData.size() - list.size(), list.size());
                            mPageNo++;
                        }
                        GanKFragment.this.onCompleted(pageNo);
                    }
                    @Override
                    public void onError(Throwable e) {
                        GanKFragment.this.onCompleted(pageNo);
                    }
                });
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        super.onDestroy();
    }
}
