package com.github.application.ui;

import com.github.application.base.BaseSuperActivity;
import com.github.application.main.MainActivity;

/**
 * Created by ZhongXiaolong on 2019/3/19 0:24.
 */
public class LauncherActivity extends BaseSuperActivity {

    @Override
    protected void onStart() {
        super.onStart();
        startActivity(MainActivity.class);
        finish();
    }
}
