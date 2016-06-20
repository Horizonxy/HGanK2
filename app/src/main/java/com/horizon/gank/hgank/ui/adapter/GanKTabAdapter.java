package com.horizon.gank.hgank.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.horizon.gank.hgank.ui.fragment.GanKFragment;

import java.util.List;

public class GanKTabAdapter extends FragmentPagerAdapter {

    private List<String> mTitles;

    public GanKTabAdapter(List<String> mTitles, FragmentManager fm) {
        super(fm);
        this.mTitles = mTitles;
    }

    @Override
    public int getCount() {
        return mTitles == null ? 0 : mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return new GanKFragment().newInstance(mTitles.get(position));
    }
}
