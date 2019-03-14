package com.svnchina.application.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * Created by ZhongXiaolong on 2017/12/13 10:54.
 * <p>
 * Activity基类
 */
public class BaseSuperActivity extends AppCompatActivity {

    public static final int START_REQUEST_CODE = 6;
    public static final int START_RESULT_CODE = 8;
    public static final String KEY = "key";
    public static final String KEY_STRING = "String";
    public static final String KEY_STRINGS = "String[]";
    public static final String KEY_INTEGER = "Integer";
    public static final String KEY_LONG = "Long";

    protected FragmentTransaction getFragmentTransaction() {
        return getSupportFragmentManager().beginTransaction();
    }

    @ColorInt
    protected int getColorResource(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    protected Context getContext() {
        return this;
    }

    public SharedPreferences getSharedPrefs() {
        return super.getSharedPreferences(this.getClass().getName(), MODE_PRIVATE);
    }

    public boolean setSupportActionBar(@IdRes int toolbar) {
        View view = findViewById(toolbar);
        if (view instanceof Toolbar) {
            setSupportActionBar((Toolbar) view);
            ((Toolbar) view).setNavigationOnClickListener(v -> onBackPressed());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            return true;
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dp(float pxValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转换成px
     */
    public int sp2px(float spValue) {
        float fontScale = this.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转换成sp
     */
    public int px2sp(float pxValue) {
        float fontScale = this.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 是否有NavigationBar（导航栏）
     *
     * @return
     */
    public boolean hasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = getContext().getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    protected void startActivity(Class<? extends Activity> activityClass) {
        startActivity(new Intent(getContext(), activityClass));
    }

    protected String getEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    @ColorInt
    protected int getDrawableColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }

    public void finish(View view) {
        finish();
    }
}
