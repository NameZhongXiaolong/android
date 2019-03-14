package com.svnchina.application.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ZhongXiaolong on 2018/2/12 10:40.
 * <p>
 * 布局是RecyclerView的Fragment基类
 */
public class RecyclerViewFragment extends BaseSuperFragment {

    private RecyclerView mRecyclerView;
    private final int ID = 0XFBBAE01;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recyclerView.setId(ID);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(ID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adp) {
        getRecyclerView().setAdapter(adp);
    }

    public void setRecyclerViewBackgroundResource(@DrawableRes int drawable) {
        getRecyclerView().setBackgroundResource(drawable);
    }

    public void setRecyclerViewBackgroundColor(@ColorInt int color) {
        getRecyclerView().setBackgroundColor(color);
    }

    public void setRecyclerViewBackground(Drawable background) {
        getRecyclerView().setBackground(background);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decoration, int index) {
        getRecyclerView().addItemDecoration(decoration, index);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        getRecyclerView().addItemDecoration(decoration);
    }
}
