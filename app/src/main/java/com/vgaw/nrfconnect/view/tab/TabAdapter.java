package com.vgaw.nrfconnect.view.tab;

import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Created by dell on 2018/3/4.
 */

public abstract class TabAdapter<T> {
    private List<T> dataList;

    public TabAdapter(List<T> dataList) {
        this.dataList = dataList;
    }

    protected T getTabData(int position) {
        return (dataList == null ? null : dataList.get(position));
    }

    protected int getItemViewType(int position) {
        return 0;
    }

    /**
     * 该方法（自定义布局）与{@link FragmentPagerAdapter#getPageTitle(int)}（默认布局）二者取其一
     * @return
     */
    protected abstract View getTabView(Context context, int position, int type, T item);
}
