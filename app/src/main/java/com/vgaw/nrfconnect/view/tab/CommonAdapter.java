package com.vgaw.nrfconnect.view.tab;

import android.content.Context;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;

/**
 * @author caojin
 * @date 2018/3/4
 */

public abstract class CommonAdapter<T> {
    private List<T> dataList;
    protected Context context;

    public CommonAdapter(Context context, List<T> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    protected int getCount() {
        return (dataList == null ? 0 : dataList.size());
    }

    protected T getItem(int position) {
        return (dataList == null ? null : dataList.get(position));
    }

    protected int getItemViewType(int position) {
        return 0;
    }

    /**
     * 该方法（自定义布局）与{@link FragmentPagerAdapter#getPageTitle(int)}（默认布局）二者取其一
     * @return
     */
    protected abstract View getView(int position, int type, T item);
}
