package com.github.application.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

/**
 * Created by ZhongXiaolong on 2019/3/11 17:14.
 * <p>
 * 可以动态改变的主题
 */
public class MultipleThemeActivity extends BaseSuperActivity {

    private final String KEY_THEME = "theme";
    private final String THEME_CHANGE_ACTION = "com.github.application.base.MultipleThemeActivity";
    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mThemeChangeBroadcast;

    @Override
    public void setTheme(int resid) {
        super.setTheme(resid);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = getSharedPrefs().getInt(KEY_THEME, 0);
        if (theme != 0) {
            setTheme(theme);
        }

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mThemeChangeBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int theme = intent.getIntExtra(KEY_THEME, 0);
                if (theme != 0) {
                    setTheme(theme);
                    getSharedPrefs().edit().putInt(KEY_THEME, theme).apply();
                    recreate();
                }
            }
        };
        mLocalBroadcastManager.registerReceiver(mThemeChangeBroadcast, new IntentFilter(THEME_CHANGE_ACTION));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mThemeChangeBroadcast);
    }

    /**
     * 发送主题改变广播
     *
     * @param resid
     */
    public void sendThemeChangeBroadcast(int resid) {
        if (resid != 0) {
            Intent intent = new Intent(THEME_CHANGE_ACTION);
            intent.putExtra(KEY_THEME, resid);
            mLocalBroadcastManager.sendBroadcast(intent);
        }
    }

}
