package com.github.application.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/3/12 12:39.
 */
public class BasePagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> mFragments;

    public BasePagerAdapter(FragmentManager fm,Fragment... fragment) {
        super(fm);
        List<Fragment> fragments = Arrays.asList(fragment);
        mFragments = new ArrayList<>();
        mFragments.addAll(fragments);
    }

    public BasePagerAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        mFragments = new ArrayList<>();
        mFragments.addAll(fragments);
    }

    public BasePagerAdapter(FragmentActivity activity, Fragment... fragment) {
        super(activity.getSupportFragmentManager());
        List<Fragment> fragments = Arrays.asList(fragment);
        mFragments = new ArrayList<>();
        mFragments.addAll(fragments);
    }

    public BasePagerAdapter(FragmentActivity activity,List<Fragment> fragments) {
        super(activity.getSupportFragmentManager());
        mFragments = new ArrayList<>();
        mFragments.addAll(fragments);
    }

    public BasePagerAdapter(Fragment fm, Fragment... fragment) {
        super(fm.getChildFragmentManager());
        List<Fragment> fragments = Arrays.asList(fragment);
        mFragments = new ArrayList<>();
        mFragments.addAll(fragments);
    }

    public BasePagerAdapter(Fragment fm,List<Fragment> fragments) {
        super(fm.getChildFragmentManager());
        mFragments = new ArrayList<>();
        mFragments.addAll(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
