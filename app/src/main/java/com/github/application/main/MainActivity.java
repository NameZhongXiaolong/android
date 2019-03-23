package com.github.application.main;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.widget.ImageButton;

import com.github.application.R;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.ui.SettingActivity;
import com.github.application.view.ActionBarView;

public class MainActivity extends MultipleThemeActivity
        implements
        ActionBarView.MenuItemClickListener,
        ActionBarView.NavigationClickListener {

    private SlidingPaneLayout mSlidingPaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBarView actionBarView = findViewById(R.id.action_bar_view);
        actionBarView.setNavigationClickListener(this);
        actionBarView.addMenuItem(0,R.drawable.ic_setting,this);
        mSlidingPaneLayout = findViewById(R.id.sliding_pane_layout);
        MainFragment fragment = new MainFragment();
        getFragmentTransaction().add(R.id.fm_container, fragment).commitAllowingStateLoss();
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
