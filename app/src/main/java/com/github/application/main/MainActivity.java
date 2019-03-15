package com.github.application.main;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.application.R;
import com.github.application.base.BaseHolder;
import com.github.application.base.MultipleThemeActivity;
import com.github.application.ui.HomeFragment;

public class MainActivity extends MultipleThemeActivity implements BaseHolder.OnClickListener {

    private HomeFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

}
