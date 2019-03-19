package com.github.application.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.application.R;
import com.github.application.base.BaseAdapter;
import com.github.application.base.BaseHolder;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.data.Theme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/3/12 11:29.
 */
public class SettingThemeActivity extends MultipleThemeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        List<Theme> list = new ArrayList<>();
        list.add(new Theme("BrightYarrow", R.style.AppTheme_BrightYarrow));
        list.add(new Theme("RobinisEGGBlue", R.style.AppTheme_RobinisEGGBlue));
        list.add(new Theme("LightGreenishBlue", R.style.AppTheme_LightGreenishBlue));
        list.add(new Theme("MintLeaf", R.style.AppTheme_MintLeaf));
        list.add(new Theme("SourLemon", R.style.AppTheme_SourLemon));
        list.add(new Theme("FirstDate", R.style.AppTheme_FirstDate));
        list.add(new Theme("ORangeVille", R.style.AppTheme_ORangeVille));
        list.add(new Theme("ElectroBlue", R.style.AppTheme_ElectroBlue));
        list.add(new Theme("ShyMoment", R.style.AppTheme_ShyMoment));
        list.add(new Theme("ExodusFruit", R.style.AppTheme_ExodusFruit));
        list.add(new Theme("PicoPink", R.style.AppTheme_PicoPink));
        list.add(new Theme("PrunusAvium", R.style.AppTheme_PrunusAvium));
        list.add(new Theme("LavenderTea", R.style.AppTheme_LavenderTea));
        list.add(new Theme("PixelatedGrass", R.style.AppTheme_PixelatedGrass));
        list.add(new Theme("MagentaPurple", R.style.AppTheme_MagentaPurple));
        list.add(new Theme("CircumorbitalRing", R.style.AppTheme_CircumorbitalRing));
        list.add(new Theme("BlueMartina", R.style.AppTheme_BlueMartina));
        if (hasNavigationBar())  list.add(new Theme(" ", 0));


        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BaseAdapter<Theme> adapter = new ThemeAdapter();
        recyclerView.setAdapter(adapter);
        adapter.addAll(list);
    }


    private class ThemeAdapter extends BaseAdapter<Theme> implements BaseHolder.OnClickListener {
        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return BaseHolder.instance(parent, R.layout.item_menu_2);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
            holder.text(R.id.text, get(position).getTheme());
            holder.setOnClickListener(this);
        }

        @Override
        public void onClick(View item, int position) {
            sendThemeChangeBroadcast(get(position).getThemeRes());
        }
    }

}
