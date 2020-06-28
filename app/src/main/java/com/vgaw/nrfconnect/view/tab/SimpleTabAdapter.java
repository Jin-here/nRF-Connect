package com.vgaw.nrfconnect.view.tab;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * @author caojin
 * @date 2018/3/24
 */
public class SimpleTabAdapter extends TabAdapter<String> {
    public SimpleTabAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    public boolean useDefaultItemView() {
        return true;
    }

    @Override
    protected View getView(int position, int type, String item) {
        return null;
    }
}
