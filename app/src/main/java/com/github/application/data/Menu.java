package com.github.application.data;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.support.v4.app.Fragment;

/**
 * Created by ZhongXiaolong on 2019/3/11 12:58.
 */
public class Menu extends BaseObservable{

    private String name;
    private Class<? extends Activity> activityClass;
    private Fragment fragment;

    public Menu(String name, Class<? extends Activity> startClass) {
        this.name = name;
        this.activityClass = startClass;
    }

    public Menu(String name, Fragment fragment) {
        this.name = name;
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public String getName() {
        return name;
    }

    public Class getActivityClass() {
        return activityClass;
    }
}
