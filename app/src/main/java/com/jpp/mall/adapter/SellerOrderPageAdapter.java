package com.jpp.mall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public class SellerOrderPageAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList = new ArrayList<>();
    public SellerOrderPageAdapter(FragmentManager fm ,List<Fragment> frgments) {
        super(fm);
        this.fragmentList = frgments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
