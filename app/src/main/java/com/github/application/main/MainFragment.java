package com.github.application.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.github.application.R;
import com.github.application.base.BaseHolder;
import com.github.application.base.ListFragment;
import com.github.application.data.Menu;
import com.github.application.ui.DialogDemoFragment;
import com.github.application.ui.PhoneInfoFragment;
import com.github.application.ui.SettingActivity;
import com.github.application.ui.UnitCastFragment;

/**
 * Created by ZhongXiaolong on 2019/3/11 10:21.
 *
 * 首页
 */
public class MainFragment extends ListFragment<Menu> implements BaseHolder.OnClickListener {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        add(new Menu("弹出框", new DialogDemoFragment()));
        add(new Menu("图片相关", SettingActivity.class));
        add(new Menu("单位换算", new UnitCastFragment()));
        add(new Menu("四大组件", SettingActivity.class));
        add(new Menu("获取设备信息", new PhoneInfoFragment()));
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
        holder.setOnClickListener(this);
    }

    @Override
    public void onClick(View item, int position) {
        Menu menu = get(position);
        MainApplication.toast(menu.getName());
//        Fragment fragment = menu.getFragment();
//        if (menu.getName().equals("图片相关")) {
//            MainApplication.toast("你好啊");
//            return;
//        }
//        if (fragment != null) {
//            ContainerActivity.start(getContext(),fragment.getClass(), menu.getName());
//            return;
//        }
//
//        if (menu.getActivityClass() != null) {
//            startActivity(menu.getActivityClass());
//            return;
//        }

    }
}
