package com.svnchina.application.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.svnchina.application.R;
import com.svnchina.application.base.BaseSuperActivity;
import com.svnchina.application.base.SideslipSignOutActivity;
import com.svnchina.application.base.BaseSuperFragment;

/**
 * Created by ZhongXiaolong on 2017/12/13 11:11.
 * <p>
 * Fragment容器Activity
 */
public class ContainerActivity extends SideslipSignOutActivity {

    private static final String KEY_FRAGMENT_NAME = "fragment_name";
    private static final String KEY_NAME = "name";

    @IdRes private final int parent_id = 0XFF08211;

    public static void start(Context context,String canonicalName,String name) {
        Intent starter = new Intent(context, ContainerActivity.class);
        starter.putExtra(KEY_FRAGMENT_NAME, canonicalName);
        starter.putExtra(KEY_NAME, name);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout parent = new LinearLayout(this);
        parent.setBackgroundResource(R.color.white);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setId(parent_id);

        parent.addView(LayoutInflater.from(this).inflate(R.layout.action_bar, parent, false));

        setContentView(parent);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        final String fragmentCanonicalName = getIntent().getStringExtra(KEY_FRAGMENT_NAME);
        final String name = getIntent().getStringExtra(KEY_NAME);
        try {
            Class<?> clazz = Class.forName(fragmentCanonicalName);
            Object fm = clazz.newInstance();
            if (fm instanceof BaseSuperFragment) {
                getSupportActionBar().setTitle(name);
                getFragmentTransaction().add(parent_id, (BaseSuperFragment) fm).commitAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}