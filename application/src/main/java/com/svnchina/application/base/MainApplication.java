package com.svnchina.application.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by ZhongXiaolong on 2018/5/28 17:45.
 */
public class MainApplication extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
