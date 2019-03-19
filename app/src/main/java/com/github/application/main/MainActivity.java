package com.github.application.main;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.widget.ImageButton;

import com.github.application.R;
import com.github.application.base.BaseHolder;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.ui.HomeFragment;
import com.github.application.ui.SettingActivity;
import com.github.application.view.ActionBarView;

public class MainActivity extends MultipleThemeActivity
        implements BaseHolder.OnClickListener,
        ActionBarView.MenuItemClickListener,
        ActionBarView.NavigationClickListener {

    private HomeFragment mFragment;
    private SlidingPaneLayout mSlidingPaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBarView actionBarView = findViewById(R.id.action_bar_view);
        actionBarView.setNavigationClickListener(this);
        actionBarView.addMenuItem(0,R.drawable.ic_setting,this);
        mSlidingPaneLayout = findViewById(R.id.sliding_pane_layout);
        mFragment = new HomeFragment();
        mFragment.setOnClickListener(this);
        getFragmentTransaction().add(R.id.fm_container, mFragment).commitAllowingStateLoss();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }


    @Override
    public void onClick(View item, int position) {
        startActivity(mFragment.get(position).getActivityClass());
    }

    @Override
    public void onMenuItemClick(int id) {
        startActivity(SettingActivity.class);
    }

    @Override
    public void onNavigationClick(ImageButton button) {
        mSlidingPaneLayout.openPane();
    }
}
