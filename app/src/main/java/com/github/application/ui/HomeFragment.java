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
 *
 * 首页
 */
public class HomeFragment extends ListFragment<Menu>  {

    private BaseHolder.OnClickListener mOnClickListener;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundResource(R.color.paleGrey);
        add(new Menu("弹出框", SettingActivity.class));
        add(new Menu("图片相关", SettingActivity.class));
        add(new Menu("单位换算", SettingActivity.class));
        add(new Menu("四大组件", SettingActivity.class));
        add(new Menu("获取设备信息", SettingActivity.class));
        add(new Menu("RecyclerView", SettingActivity.class));
        add(new Menu("note", SettingActivity.class));
        add(new Menu("自定义View--登录", SettingActivity.class));
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
