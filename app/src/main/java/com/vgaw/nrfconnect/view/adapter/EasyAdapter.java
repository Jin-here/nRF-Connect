package com.vgaw.nrfconnect.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class EasyAdapter<T> extends BaseAdapter {
    protected Context context;
    private List<T> dataList;

    public EasyAdapter(Context context) {
        this(context, null);
    }

    /**
     * 如果dataList为空，需要重写{@link #getCount()}和{@link #getItem(int)}
     * @param context
     * @param dataList
     */
    public EasyAdapter(Context context, List<T> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public T getItem(int position) {
        return dataList == null ? null : dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EasyHolder holder = null;
        if (convertView == null){
            holder = getHolder(getItemViewType(position));
            holder.setDataList(this.dataList);
            holder.init(context);
            convertView = holder.createView(position);
            convertView.setTag(holder);
        }else {
            holder = (EasyHolder) convertView.getTag();
        }
        holder.refreshView(position, getItem(position));
        return convertView;
    }

    public abstract EasyHolder<T> getHolder(int type);
}
