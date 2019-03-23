package com.github.application.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.github.application.R;
import com.github.application.base.BaseHolder;
import com.github.application.base.ListFragment;

/**
 * Created by ZhongXiaolong on 2019/3/23 1:10.
 */
public abstract class SimpleListFragment extends ListFragment<String>  {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onCreateList();
    }

    abstract void onCreateList();

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        holder.text(R.id.text, get(position));
        holder.setOnClickListener(new BaseHolder.OnClickListener() {
            @Override
            public void onClick(View item, int position) {
                SimpleListFragment.this.onClick(item,position);
            }
        });
    }

    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseHolder.instance(parent, R.layout.item_menu_1);
    }

    abstract void onClick(View item, int position);
}
