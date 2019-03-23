package com.github.application.main;

import android.app.Application;

/**
 * Created by ZhongXiaolong on 2019/3/8 16:34.
 */
public class MainApplication extends Application {

    private static MainApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static MainApplication getApplication(){
        return mApplication;
    }
}
