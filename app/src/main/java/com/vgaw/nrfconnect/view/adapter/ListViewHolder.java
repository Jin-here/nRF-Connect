package com.vgaw.nrfconnect.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by caojin on 2017/12/11.
 */

public abstract class ListViewHolder {
    protected View view;
    protected Context context;

    public void init(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(getLayout(), null);
    }

    public abstract @LayoutRes int getLayout();
}
