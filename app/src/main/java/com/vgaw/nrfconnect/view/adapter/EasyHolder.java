package com.vgaw.nrfconnect.view.adapter;

import android.view.View;

import java.util.List;

/**
 * from : Volodymyr
 * to : caojinmail@163.com
 * me : github.com/VolodymyrCj/
 */
public abstract class EasyHolder<T> extends ListViewHolder {
    protected List<T> dataList;

    protected void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public abstract View createView(int position);

    public abstract void  refreshView(int position, T item);
}
