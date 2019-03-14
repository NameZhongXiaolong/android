package com.svnchina.application.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Switch;

import com.svnchina.application.R;
import com.svnchina.application.base.SideslipSignOutActivity;

import java.util.Objects;

/**
 * Created by ZhongXiaolong on 2018/5/28 16:05.
 * <p>
 * 设置
 */
public class SettingsActivity extends SideslipSignOutActivity {

    public static final String HOME_BACKGROUND = "home_background";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_setting);

        if (setSupportActionBar(R.id.action_bar)) Objects.requireNonNull(getSupportActionBar()).setTitle("设置");

        SharedPreferences sharedPrefs = getSharedPreferences("setting",MODE_PRIVATE);
        boolean homeBackground = sharedPrefs.getBoolean(HOME_BACKGROUND, false);
        Switch swhHomeBg = findViewById(R.id.swh_home_bg);
        swhHomeBg.setChecked(homeBackground);

        swhHomeBg.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPrefs.edit().putBoolean(HOME_BACKGROUND, isChecked).apply();
            setResult(RESULT_OK);
        });
    }

    public static boolean getShowBackground(Context context) {
        return context.getSharedPreferences("setting", MODE_PRIVATE).getBoolean(HOME_BACKGROUND, false);
    }

    public void getSettingPreferences(){

    }
}
