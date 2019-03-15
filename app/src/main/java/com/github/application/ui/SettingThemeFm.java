package com.github.application.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.github.application.R;
import com.github.application.base.BaseHolder;
import com.github.application.base.ListFragment;
import com.github.application.data.Theme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/3/12 13:26.
 */
public class SettingThemeFm extends ListFragment<Theme> {

    private BaseHolder.OnClickListener mOnClickListener;

    public static SettingThemeFm newInstance(List<Theme> themes) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY, new ArrayList<Parcelable>(themes));
        SettingThemeFm fragment = new SettingThemeFm();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Theme> themes = getArguments().getParcelableArrayList(KEY);
        addAll(themes);
    }

    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseHolder.instance(parent, getItemViewId());
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        holder.text(R.id.text, get(position).getTheme());
        if (mOnClickListener != null) holder.setOnClickListener(mOnClickListener);
    }

    public SettingThemeFm setOnClickListener(BaseHolder.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        return this;
    }

    @LayoutRes
    public int getItemViewId(){
        return R.layout.item_menu_2;
    }
}
