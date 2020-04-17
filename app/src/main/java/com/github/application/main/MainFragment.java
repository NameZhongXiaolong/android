package com.github.application.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.github.application.R;
import com.github.application.base.BaseHolder;
import com.github.application.base.ListFragment;
import com.github.application.base.choice.gallery.ChoiceGallery;
import com.github.application.data.Menu;
import com.github.application.ui.ContainerActivity;
import com.github.application.ui.DialogDemoFragment;
import com.github.application.ui.LoginFm;
import com.github.application.ui.NoteFm;
import com.github.application.ui.PhoneInfoFragment;
import com.github.application.ui.PlaneGraphActivity;
import com.github.application.ui.SettingActivity;
import com.github.application.ui.TestActivity;
import com.github.application.ui.UnitCastFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/3/11 10:21.
 * <p>
 * 首页
 */
public class MainFragment extends ListFragment<Menu> implements BaseHolder.OnClickListener {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        add(new Menu("进入开发者模式", TestActivity.class));
        add(new Menu("笔记", new NoteFm()));
        add(new Menu("拼图", PlaneGraphActivity.class));
        add(new Menu("测试", TestActivity.class));
        add(new Menu("弹出框", new DialogDemoFragment()));
        add(new Menu("图片相关", SettingActivity.class));
        add(new Menu("单位换算", new UnitCastFragment()));
        add(new Menu("四大组件", SettingActivity.class));
        add(new Menu("获取设备信息", new PhoneInfoFragment()));
        add(new Menu("自定义View--登录", new LoginFm()));
        add(new Menu("图片选择器", TestActivity.class));
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
        String title = get(position).getName();
        if (title.equalsIgnoreCase("图片选择器")) {
            final SharedPreferences sharedPrefs = getBaseSuperActivity().getSharedPrefs();
            final String key = "choice_gallery_cache";
            String json = sharedPrefs.getString(key, "");
            List<String> choiceList = new Gson().fromJson(json, new TypeToken<List<String>>() {}.getType());
            new ChoiceGallery(this)
                    .setMaxChoice(9)
                    .setChoiceList(choiceList)
                    .setCallback(photos -> sharedPrefs.edit().putString(key, new Gson().toJson(photos)).apply())
                    .start();
        } else if (title.equalsIgnoreCase("进入开发者模式")) {
            try {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                startActivity(intent);
            } catch (Exception e) {
                try {
                    ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.DevelopmentSettings");
                    Intent intent = new Intent();
                    intent.setComponent(componentName);
                    intent.setAction("android.intent.action.View");
                    startActivity(intent);
                } catch (Exception e1) {
                    try {
                        Intent intent = new Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS");
                        startActivity(intent);
                    } catch (Exception ignored) {

                    }
                }
            }
        } else {
            Menu menu = get(position);
            Fragment fragment = menu.getFragment();
            if (fragment != null) {
                ContainerActivity.start(getContext(), fragment.getClass(), menu.getName());
            } else if (menu.getActivityClass() != null) {
                startActivity(menu.getActivityClass());
            }
        }
    }
}
