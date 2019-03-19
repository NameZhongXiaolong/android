package com.github.application.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.view.ActionBarView;

/**
 * Created by ZhongXiaolong on 2019/3/11 17:04.
 */
public class SettingActivity extends MultipleThemeActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findViewById(R.id.button).setOnClickListener(this);

        ActionBarView actionBarView = findViewById(R.id.action_bar_view);
    }

    @Override
    public void onClick(View v) {
        startActivity(SettingThemeActivity.class);
    }

}
