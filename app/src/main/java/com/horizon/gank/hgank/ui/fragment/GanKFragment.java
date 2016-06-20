package com.horizon.gank.hgank.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.Constants;
import com.horizon.gank.hgank.R;
import com.horizon.gank.hgank.di.component.DaggerGanKFragmentComponent;
import com.horizon.gank.hgank.di.module.GanKFragmentModule;
import com.horizon.gank.hgank.model.bean.CommonCacheVo;
import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.model.db.CommonDaoImpl;
import com.horizon.gank.hgank.presenter.GanKPresenter;
import com.horizon.gank.hgank.ui.adapter.recyclerview.QuickAdapter;
import com.horizon.gank.hgank.ui.iview.GanKFragmentViewListener;
import com.horizon.gank.hgank.util.DrawableUtils;
import com.horizon.gank.hgank.util.GsonUtils;
import com.horizon.gank.hgank.util.NetUtils;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GanKFragment extends Fragment implements GanKFragmentViewListener {

    private String mType;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh)
    MaterialRefreshLayout mRefreshLayout;
    @Bind(R.id.btn_to_top)
    FloatingActionButton btnToTop;
    @Bind(R.id.load_progress)
    CircleProgressBar mLoading;
    @Inject
    QuickAdapter<GanKData> mAdapter;
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

            mRefreshLayout.setLoadMore(true);
            mRefreshLayout.setMaterialRefreshListener(new RefreshListener());
            mRefreshLayout.autoRefresh();

            btnToTop.setImageDrawable(DrawableUtils.getDrawable(getContext(), MaterialDesignIconic.Icon.gmi_long_arrow_up));

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                private int totalY;
                private final int  OFFSET_Y = (int) (Application.application.SCREENHEIGHT * 2.5);

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalY = mRecyclerView.computeVerticalScrollOffset();
                    if(totalY > OFFSET_Y){
                        btnToTop.setVisibility(View.VISIBLE);
                    } else {
                        btnToTop.setVisibility(View.GONE);
                    }
                }
            });
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

    @OnClick(R.id.btn_to_top)
    void toTop(){
        mRecyclerView.scrollToPosition(0);
        btnToTop.setVisibility(View.GONE);
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
        Snackbar.make(mRefreshLayout, "加载出问题了~~", Snackbar.LENGTH_SHORT).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGankData();
            }
        }).show();
        onCompleted(mPageNo);
    }

    @Override
    public void onSuccess(List<GanKData> data) {
        if (mPageNo == 1) {
            mData.clear();
        }
        mData.addAll(data);
//        mAdapter.notifyDataSetChanged();
        mAdapter.notifyItemRangeChanged(mData.size() - data.size(), data.size());

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
    }

    @Override
    public void onCompleted(int pageNo) {
        if (pageNo == 1) {
            mRefreshLayout.finishRefresh();
        } else {
            mRefreshLayout.finishRefreshLoadMore();
        }
        if(mLoading.getVisibility() == View.VISIBLE){
            mLoading.setVisibility(View.GONE);
        }
    }

    private void loadGankData() {
        if (NetUtils.isNetworkConnected(getContext())) {
            mPresenter.loadData();
        } else {
            int pageNo = mPageNo;
            mCacheMap.put(CommonCacheVo.DATA_PAGE_NO, pageNo);
            List<CommonCacheVo> commonlist = mCommonDao.findByColumns(mCacheMap);
            if (commonlist != null && !commonlist.isEmpty()) {
                List<GanKData> list = GsonUtils.getList(commonlist.get(0).getData(), GanKData.class);
                if (pageNo == 1) {
                    mData.clear();
                }
                mData.addAll(list);
                mAdapter.notifyItemRangeChanged(mData.size() - list.size(), list.size());
                mPageNo++;
            } else {
                onFinish();
            }
            onCompleted(pageNo);
        }
    }

    class RefreshListener extends MaterialRefreshListener {

        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            mPageNo = 1;
            loadGankData();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            loadGankData();
        }
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        super.onDestroy();
    }
}
