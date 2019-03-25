package com.github.application.utils;

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * Created by ZhongXiaolong on 2019/3/25 14:56.
 */
public class ColorUtils {

    private ColorUtils(){/*私有*/}
    
    /**
     * 是否是深色
     *
     * @param color
     *
     * @return true深色
     */
    public static boolean isDarkColor(@ColorInt int color) {
        //透明度
        final int alpha = Color.alpha(color);

        boolean light = alpha <= 48;

        //透明度>0.2就看rgb,透明度<=0.2就设置黑色
        if (!light) {
            final double darkness = 1 -
                    (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
            light = darkness < 0.2;
        }
        return !light;
    }

}
