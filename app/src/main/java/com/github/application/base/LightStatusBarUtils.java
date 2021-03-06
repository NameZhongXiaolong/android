package com.github.application.base;

/**
 * Created by ZhongXiaolong on 2019/3/4 11:14.
 */

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by ZhongXiaolong on 2019/3/04 14:16.
 * <p>
 * 设置浅色状态栏 , 深色文字描述
 */
 class LightStatusBarUtils {

    public static void setLightStatusBar(Activity activity, boolean dark) {
        switch (RomUtils.getLightStatausBarAvailableRomType()) {
            case RomUtils.AvailableRomType.MIUI:
                setMIUILightStatusBar(activity, dark);
                break;

            case RomUtils.AvailableRomType.FLYME:
                setFlymeLightStatusBar(activity, dark);
                break;

            case RomUtils.AvailableRomType.ANDROID_NATIVE:
                setAndroidNativeLightStatusBar(activity, dark);
                break;

            case RomUtils.AvailableRomType.NA:
                // N/A do nothing
                break;
        }
    }

    private static boolean setMIUILightStatusBar(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean setFlymeLightStatusBar(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            // We want to change tint c olor to white again.
            // You can also record the flags in advance so that you can turn UI back completely if
            // you have set other flags before, such as translucent or full screen.
            decor.setSystemUiVisibility(0);
        }
    }

    public static class RomUtils {

        class AvailableRomType {
            public static final int MIUI = 1;
            public static final int FLYME = 2;
            public static final int ANDROID_NATIVE = 3;
            public static final int NA = 4;
        }

        public static boolean isLightStatusBarAvailable() {
            if (isMIUIV6OrAbove() || isFlymeV4OrAbove() || isAndroidMOrAbove()) {
                return true;
            }
            return false;
        }

        public static int getLightStatausBarAvailableRomType() {
            if (isMIUIV6OrAbove()) {
                return AvailableRomType.MIUI;
            }

            if (isFlymeV4OrAbove()) {
                return AvailableRomType.FLYME;
            }

            if (isAndroidMOrAbove()) {
                return AvailableRomType.ANDROID_NATIVE;
            }

            return AvailableRomType.NA;
        }

        //Flyme V4的displayId格式为 [Flyme OS 4.x.x.xA]
        //Flyme V5的displayId格式为 [Flyme 5.x.x.x beta]
        private static boolean isFlymeV4OrAbove() {
            String displayId = Build.DISPLAY;
            if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
                String[] displayIdArray = displayId.split(" ");
                for (String temp : displayIdArray) {
                    //版本号4以上，形如4.x.
                    if (temp.matches("^[4-9]\\.(\\d+\\.)+\\S*")) {
                        return true;
                    }
                }
            }
            return false;
        }

        //MIUI V6对应的versionCode是4
        //MIUI V7对应的versionCode是5
        private static boolean isMIUIV6OrAbove() {
            String miuiVersionCodeStr = getSystemProperty("ro.miui.ui.version.code");
            if (!TextUtils.isEmpty(miuiVersionCodeStr)) {
                try {
                    int miuiVersionCode = Integer.parseInt(miuiVersionCodeStr);
                    if (miuiVersionCode >= 4) {
                        return true;
                    }
                } catch (Exception e) {
                }
            }
            return false;
        }

        //Android Api 23以上
        private static boolean isAndroidMOrAbove() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return true;
            }
            return false;
        }

        private static String getSystemProperty(String propName) {
            String line;
            BufferedReader input = null;
            try {
                Process p = Runtime.getRuntime().exec("getprop " + propName);
                input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
                line = input.readLine();
                input.close();
            } catch (IOException ex) {
                return null;
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                    }
                }
            }
            return line;
        }
    }
}
