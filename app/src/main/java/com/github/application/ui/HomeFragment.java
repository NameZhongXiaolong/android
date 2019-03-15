package com.github.application.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.github.application.R;
import com.github.application.base.BaseHolder;
import com.github.application.base.ListFragment;
import com.github.application.data.Menu;

/**
 * Created by ZhongXiaolong on 2019/3/11 10:21.
 */
public class HomeFragment extends ListFragment<Menu>  {

    private BaseHolder.OnClickListener mOnClickListener;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        add(new Menu("设置", SettingActivity.class));
    }

    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseHolder.instance(parent, R.layout.item_menu_1);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        holder.text(R.id.text, get(position).getName());
        holder.setOnClickListener(mOnClickListener);

    }

    public HomeFragment setOnClickListener(BaseHolder.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        return this;
    }
}
