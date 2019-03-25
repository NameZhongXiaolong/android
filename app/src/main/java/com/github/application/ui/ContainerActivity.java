package com.github.application.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.view.ActionBarView;

/**
 * Created by ZhongXiaolong on 2019/3/22 1:05.
 *
 */
public class ContainerActivity extends MultipleThemeActivity {

    public static void start(Context context,Class<? extends Fragment> fragmentClass,String title) {
        Intent starter = new Intent(context, ContainerActivity.class);
        starter.putExtra(KEY, title);
        starter.putExtra(KEY_STRING, fragmentClass.getCanonicalName());
        context.startActivity(starter);
    }

    public static Intent starter(Context context,Class<? extends Fragment> fragmentClass,String title) {
        Intent starter = new Intent(context, ContainerActivity.class);
        starter.putExtra(KEY, title);
        starter.putExtra(KEY_STRING, fragmentClass.getCanonicalName());
        return starter;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        final ActionBarView actionBarView = findViewById(R.id.action_bar_view);
        final String title = getIntent().getStringExtra(KEY);
        final String fragmentClassName = getIntent().getStringExtra(KEY_STRING);

        if (!TextUtils.isEmpty(title)) {
            actionBarView.setTitle(title);
        }

        try {
            Fragment fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
            getFragmentTransaction().add(R.id.fm_container, fragment, fragmentClassName).commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
