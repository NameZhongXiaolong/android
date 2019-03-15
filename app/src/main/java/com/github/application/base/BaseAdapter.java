package com.github.application.base;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/3/11 11:01.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder> {

    private List<T> mData;

    public BaseAdapter() {
        mData = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public final BaseAdapter add(T data) {
        mData.add(data);
        notifyDataSetChanged();
        return this;
    }

    public final BaseAdapter add(int position, T data) {
        mData.add(position, data);
        notifyDataSetChanged();
        return this;
    }

    public final BaseAdapter addAll(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
        return this;
    }

    public final BaseAdapter addAll(int position, List<T> data) {
        mData.addAll(position, data);
        notifyDataSetChanged();
        return this;
    }

    public final boolean remove(T data) {
        boolean remove = mData.remove(data);
        if (remove) notifyDataSetChanged();
        return remove;
    }

    public final T remove(int position) {
        return mData.remove(position);
    }

    public final T remove(int position, T data) {
        return mData.set(position, data);
    }

    public final T get(int position) {
        return mData.get(position);
    }

}