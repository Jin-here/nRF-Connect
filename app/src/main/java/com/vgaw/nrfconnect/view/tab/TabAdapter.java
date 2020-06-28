package com.vgaw.nrfconnect.view.tab;

import android.content.Context;

import java.util.List;

/**
 * @author caojin
 * @date 2018/3/23
 */
public abstract class TabAdapter<T> extends CommonAdapter<T> {

    public TabAdapter(Context context, List<T> dataList) {
        super(context, dataList);
    }

    public abstract boolean useDefaultItemView();
}
