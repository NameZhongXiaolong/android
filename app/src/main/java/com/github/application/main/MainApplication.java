package com.github.application.main;

import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.application.R;
import com.github.application.utils.UnitUtils;

import java.io.File;

/**
 * Created by ZhongXiaolong on 2019/3/8 16:34.
 */
public class MainApplication extends Application {

    private static MainApplication mApplication;
    private static long sToastStartTimeMillis;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

    }

    public static MainApplication getApplication() {
        return mApplication;
    }

    private static CharSequence sToastMsg;

    public static void errToast(final CharSequence msg){
        toast(msg, Color.parseColor("#FF5C5C"));
    }

    public static void outToast(final CharSequence msg){
        toast(msg, Color.parseColor("#0984E3"));
    }

    private static void toast(final CharSequence msg, @ColorInt int color) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (System.currentTimeMillis() - sToastStartTimeMillis < 2000 && msg.equals(sToastMsg)) {
            return;
        }

        Toast sToast = new Toast(mApplication);
        sToastMsg = msg;
//        try {
//            Field mTN = sToast.getClass().getDeclaredField("mTN");
//            mTN.setAccessible(true);
//            Object mTNObj = mTN.get(sToast);
//
//            Field mParams = mTNObj.getClass().getDeclaredField("mParams");
//            mParams.setAccessible(true);
//            WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams.get(mTNObj);
//            params.width = UnitUtils.displayWidth();//-1表示全屏, 你也可以设置任意宽度.
//            params.height = UnitUtils.getActionAndStatusBarSize();
//            params.windowAnimations = R.style.TopAnimation;//设置动画, 需要是style类型
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        FrameLayout toastView = new FrameLayout(mApplication);
        toastView.setMinimumWidth(UnitUtils.displayWidth());
        toastView.setMinimumHeight(UnitUtils.getActionBarSize());
        TextView text = new TextView(mApplication);
//        text.setMinWidth(UnitUtils.displayWidth());
//        text.setMinHeight((int) UnitUtils.getActionAndStatusBarSize());
//        text.setPadding(0, UnitUtils.getStatusBarSize(), 0, 0);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(UnitUtils.px(3));

        text.setBackground(gradientDrawable);
        text.setGravity(Gravity.CENTER);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        text.setTextColor(Color.WHITE);
        text.setId(R.id.text);
        text.setText(msg);
        int width = UnitUtils.displayWidth() - UnitUtils.px(10);
        int height = UnitUtils.getActionBarSize()-UnitUtils.px(12);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height, Gravity.CENTER);
        toastView.addView(text, lp);
        sToast.setView(toastView);
        sToast.setGravity(Gravity.TOP, 0, 0);
//        sToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//设置Toast可以布局到系统状态栏的下面
        sToast.setDuration(Toast.LENGTH_SHORT);
        sToast.show();

        sToastStartTimeMillis = System.currentTimeMillis();

    }

    /**
     * 获取本app专属目录
     */
    public static String getFileRoot(){
        String dir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStorageDirectory().getPath();
        } else {
            dir = Environment.getDataDirectory().getPath();
        }
        File file = new File(dir,"abc_application");
        if(!file.exists()){
            return file.mkdirs() ? file.getAbsolutePath() : dir;
        }else{
            return file.getAbsolutePath();
        }
    }
}
