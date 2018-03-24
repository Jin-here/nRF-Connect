package com.vgaw.nrfconnect.view.tab;

import android.support.v4.view.ViewPager;

/**
 * @author caojin
 * @date 2018/3/23
 */
public interface InteractiveInterface {
    /**
     * 获取tab初始化位置
     * @return
     */
    int getCurrentItem();

    /**
     * 当tab位置变化时，通知改变
     * @param item
     */
    void setCurrentItem(int item);

    /**
     * 监听以收到变化通知
     * @param listener
     */
    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);
}