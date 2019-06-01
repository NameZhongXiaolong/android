package com.github.application.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by ZhongXiaolong on 2019/3/18 23:18.
 * <p>
 * 单位换算Utils
 */
public class UnitUtils {


    private UnitUtils() { /*私有*/}

    private static int sWidthPixels;
    private static int sHeightPixels;
    private static int sStatusBarSize;
    private static int sActionBarSize;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int dp(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转换成px
     */
    public static int sp2px(float spValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转换成sp
     */
    public static int px2sp(float pxValue) {
        float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int displayWidth() {
        if (sWidthPixels == 0) {
            Activity context = null;
            sWidthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        }
        return sWidthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int displayHeight(Activity activity) {
        if (sHeightPixels == 0) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            sHeightPixels = metrics.heightPixels;
        }
        return sHeightPixels;
    }

    public static int displayHeight(){
        if (sHeightPixels == 0) {
            sHeightPixels = Resources.getSystem().getDisplayMetrics().heightPixels;
        }
        return sHeightPixels;
    }

    public static int getStatusBarSize() {
        if (sStatusBarSize == 0) {
            sStatusBarSize = px(24);
        }
        return sStatusBarSize;
    }

    public static int getActionBarSize() {
        if (sActionBarSize == 0) {
            sActionBarSize = px(48);
        }
        return sActionBarSize;
    }

    public static int getActionAndStatusBarSize() {
        return getActionBarSize() + getStatusBarSize();
    }
}
