package com.github.application.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;

/**
 * Created by ZhongXiaolong on 2019/3/11 17:04.
 */
public class SettingActivity extends MultipleThemeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        LinearLayout menuContainer = findViewById(R.id.linear_layout);

        for (int i = 0; i < menuContainer.getChildCount(); i++) {
            View childView = menuContainer.getChildAt(i);
            if (childView instanceof Button) {
                childView.setOnClickListener(this::onClick);
            }
        }
    }

    private void onClick(View v) {
        CharSequence text = ((Button) v).getText();
        if ("主题".equals(text.toString())) {
            startActivity(SettingThemeActivity.class);
        }else if ("指纹加密".equals(text.toString())) {

        }else if ("启动页".equals(text.toString())) {

        }
    }
}
