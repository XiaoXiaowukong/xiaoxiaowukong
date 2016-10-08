package com.wukong.xiaoxiao.viewpagerwithdotsdemo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 */
public class FragmentViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> listFragments;

    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> al) {
        super(fm);
        listFragments = al;
    }

    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
