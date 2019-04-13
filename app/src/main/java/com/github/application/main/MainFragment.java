package com.github.application.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.github.application.R;
import com.github.application.base.BaseHolder;
import com.github.application.base.ListFragment;
import com.github.application.data.Menu;
import com.github.application.ui.ContainerActivity;
import com.github.application.ui.DatabaseTestFm;
import com.github.application.ui.DialogDemoFragment;
import com.github.application.ui.LoginFm;
import com.github.application.ui.NoteActivity;
import com.github.application.ui.PhoneInfoFragment;
import com.github.application.ui.RecyclerViewDemoFm;
import com.github.application.ui.SettingActivity;
import com.github.application.ui.TestActivity;
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

        add(new Menu("测试", TestActivity.class));
        add(new Menu("Database", new DatabaseTestFm()));
        add(new Menu("弹出框", new DialogDemoFragment()));
        add(new Menu("图片相关", SettingActivity.class));
        add(new Menu("单位换算", new UnitCastFragment()));
        add(new Menu("四大组件", SettingActivity.class));
        add(new Menu("获取设备信息", new PhoneInfoFragment()));
        add(new Menu("RecyclerView", new RecyclerViewDemoFm()));
        add(new Menu("note", NoteActivity.class));
        add(new Menu("自定义View--登录", new LoginFm()));
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
        Fragment fragment = menu.getFragment();
        if (fragment != null) {
            ContainerActivity.start(getContext(),fragment.getClass(), menu.getName());
            return;
        }

        if (menu.getActivityClass() != null) {
            startActivity(menu.getActivityClass());
            return;
        }

    }
}
