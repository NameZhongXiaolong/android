package com.github.application.base;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public BaseAdapter add(T data) {
        mData.add(data);
        notifyDataSetChanged();
        return this;
    }

    public BaseAdapter addAll(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
        return this;
    }

    public boolean remove(T data) {
        boolean remove = mData.remove(data);
        if (remove) notifyDataSetChanged();
        return remove;
    }

    public T remove(int position) {
        return mData.remove(position);
    }

    public T remove(int position,T data) {
        return mData.set(position, data);
    }

    public T get(int position) {
        return mData.get(position);
    }

    public View inflater(ViewGroup parent, @LayoutRes int viewId){
        return LayoutInflater.from(parent.getContext()).inflate(viewId, parent, false);
    }
}