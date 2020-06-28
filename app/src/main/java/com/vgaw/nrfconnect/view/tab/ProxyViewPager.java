package com.vgaw.nrfconnect.view.tab;

import androidx.viewpager.widget.ViewPager;

/**
 * @author caojin
 * @date 2018/3/24
 */
public class ProxyViewPager implements InteractiveInterface {
    private ViewPager mViewPager;

    public ProxyViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
    }

    @Override
    public void setCurrentItem(int item) {
        mViewPager.setCurrentItem(item);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPager.setOnPageChangeListener(listener);
    }
}
