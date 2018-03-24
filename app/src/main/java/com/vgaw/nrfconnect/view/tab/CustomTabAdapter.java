package com.vgaw.nrfconnect.view.tab;

import android.content.Context;

import java.util.List;

/**
 * @author caojin
 * @date 2018/3/23
 */
public abstract class CustomTabAdapter<T> extends TabAdapter<T> {
    public CustomTabAdapter(Context context, List<T> dataList) {
        super(context, dataList);
    }

    @Override
    public boolean useDefaultItemView() {
        return false;
    }
}
