package com.github.application.main;

import android.app.Application;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.application.R;
import com.github.application.utils.UnitUtils;

import java.lang.reflect.Field;

/**
 * Created by ZhongXiaolong on 2019/3/8 16:34.
 */
public class MainApplication extends Application {

    private static MainApplication mApplication;
    private static Toast sToast;
    private static long sToastStartTimeMillis;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
         sToast = new Toast(mApplication);
        try {
            Field mTN = sToast.getClass().getDeclaredField("mTN");
            mTN.setAccessible(true);
            Object mTNObj = mTN.get(sToast);

            Field mParams = mTNObj.getClass().getDeclaredField("mParams");
            mParams.setAccessible(true);
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams.get(mTNObj);
            params.width = UnitUtils.displayWidth(mApplication);//-1表示全屏, 你也可以设置任意宽度.
            params.height =  UnitUtils.getActionAndStatusBarSize(mApplication);
            params.windowAnimations = R.style.TopAnimation;//设置动画, 需要是style类型
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView text = new TextView(mApplication);
        text.setMinWidth(UnitUtils.displayWidth(mApplication));
        text.setMinHeight((int) UnitUtils.getActionAndStatusBarSize(mApplication));
        text.setPadding(0, UnitUtils.getStatusBarSize(mApplication), 0, 0);
        text.setBackgroundColor(Color.parseColor("#FF5C5C"));
        text.setGravity(Gravity.CENTER);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        text.setTextColor(Color.WHITE);
        text.setId(R.id.text);
        sToast.setView(text);
        sToast.setGravity(Gravity.TOP, 0, 0);
        sToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//设置Toast可以布局到系统状态栏的下面
        sToast.setDuration(Toast.LENGTH_SHORT);
    }

    //全屏和动画的设置方法
    private void initToast(Toast toast) {
        try {
            Field mTN = toast.getClass().getDeclaredField("mTN");
            mTN.setAccessible(true);
            Object mTNObj = mTN.get(toast);

            Field mParams = mTNObj.getClass().getDeclaredField("mParams");
            mParams.setAccessible(true);
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams.get(mTNObj);
            params.width = UnitUtils.displayWidth(this);//-1表示全屏, 你也可以设置任意宽度.
            params.height = UnitUtils.getStatusBarSize(this) + UnitUtils.getActionBarSize(this);
            params.windowAnimations = R.style.TopAnimation;//设置动画, 需要是style类型

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MainApplication getApplication() {
        return mApplication;
    }

    public static void toast(final CharSequence msg) {
        Toast sToast= new Toast(mApplication);
        try {
            Field mTN = sToast.getClass().getDeclaredField("mTN");
            mTN.setAccessible(true);
            Object mTNObj = mTN.get(sToast);

            Field mParams = mTNObj.getClass().getDeclaredField("mParams");
            mParams.setAccessible(true);
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams.get(mTNObj);
            params.width = UnitUtils.displayWidth(mApplication);//-1表示全屏, 你也可以设置任意宽度.
            params.height =  UnitUtils.getActionAndStatusBarSize(mApplication);
            params.windowAnimations = R.style.TopAnimation;//设置动画, 需要是style类型
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView text = new TextView(mApplication);
        text.setMinWidth(UnitUtils.displayWidth(mApplication));
        text.setMinHeight((int) UnitUtils.getActionAndStatusBarSize(mApplication));
        text.setPadding(0, UnitUtils.getStatusBarSize(mApplication), 0, 0);
        text.setBackgroundColor(Color.parseColor("#FF5C5C"));
        text.setGravity(Gravity.CENTER);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        text.setTextColor(Color.WHITE);
        text.setId(R.id.text);
        text.setText(msg);
        sToast.setView(text);
        sToast.setGravity(Gravity.TOP, 0, 0);
        sToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//设置Toast可以布局到系统状态栏的下面
        sToast.setDuration(Toast.LENGTH_SHORT);
        sToast.show();

        ((TextView) sToast.getView().findViewById(R.id.text)).setText(msg);
        if (System.currentTimeMillis()-sToastStartTimeMillis>2000) {
            Log.d("MainApplication", "show");
//            sToast.show();
            sToastStartTimeMillis = System.currentTimeMillis();
        }else{
//            sToast.cancel();
//            sToast.show();
//            sToastStartTimeMillis = System.currentTimeMillis();
            Log.d("MainApplication", "no show");
        }
    }

    private static Handler handler = new Handler(Looper.getMainLooper());


    private static Object synObj = new Object();





}
