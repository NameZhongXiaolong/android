package com.github.application.utils;

import android.content.Context;

import com.github.application.R;

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
    public static int dp2px(Context context,float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context,float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转换成px
     */
    public static int sp2px(Context context,float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转换成sp
     */
    public static int px2sp(Context context,float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int displayWidth(Context context){
        if (sWidthPixels==0){
            sWidthPixels = context.getResources().getDisplayMetrics().widthPixels;
        }
        return sWidthPixels;
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int displayHeight(Context context){
        if (sHeightPixels ==0){
            sHeightPixels = context.getResources().getDisplayMetrics().heightPixels;
        }
        return sHeightPixels;
    }

    public static int getStatusBarSize(Context context) {
        if (sStatusBarSize == 0) {
            sStatusBarSize = (int) context.getResources().getDimension(R.dimen.statusBarSize);
        }
        return sStatusBarSize;
    }

    public static int getActionBarSize(Context context) {
        if (sActionBarSize == 0) {
            sActionBarSize = (int) context.getResources().getDimension(R.dimen.actionBarSize);
        }
        return sActionBarSize;
    }

    public static int getActionAndStatusBarSize(Context context) {
        return getActionBarSize(context) + getStatusBarSize(context);
    }
}
