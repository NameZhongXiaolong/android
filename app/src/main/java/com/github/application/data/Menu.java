package com.github.application.data;

import android.databinding.BaseObservable;

import com.github.application.base.BaseSuperActivity;

/**
 * Created by ZhongXiaolong on 2019/3/11 12:58.
 */
public class Menu extends BaseObservable{

    private String name;
    private Class<? extends BaseSuperActivity> activityClass;

    public Menu(String name, Class<? extends BaseSuperActivity> activityClass) {
        this.name = name;
        this.activityClass = activityClass;
    }

    public String getName() {
        return name;
    }

    public Class<? extends BaseSuperActivity> getActivityClass() {
        return activityClass;
    }
}
