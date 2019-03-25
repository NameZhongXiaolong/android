package com.github.application.main;

import android.app.Application;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.application.R;
import com.github.application.utils.UnitUtils;

/**
 * Created by ZhongXiaolong on 2019/3/8 16:34.
 */
public class MainApplication extends Application {

    private static MainApplication mApplication;
    private static Toast sToast;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        if (sToast == null) {
            sToast = new Toast(this);
            TextView text = new TextView(this);
            text.setMinWidth(UnitUtils.displayWidth(this));
            text.setBackgroundColor(Color.YELLOW);
            text.setMinHeight((int) UnitUtils.getActionAndStatusBarSize(this));
            text.setPadding(0, UnitUtils.getStatusBarSize(this), 0, 0);
            text.setBackgroundColor(Color.parseColor("#FF7500"));
            text.setGravity(Gravity.CENTER);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            text.setTextColor(Color.WHITE);
            text.setId(R.id.text);
            sToast.setView(text);
            sToast.setGravity(Gravity.TOP, 0, 0);
            sToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//设置Toast可以布局到系统状态栏的下面
        }
    }

    public static MainApplication getApplication() {
        return mApplication;
    }

    public static void toast(CharSequence msg) {
//        Animation animation = AnimationUtils.loadAnimation(mApplication, R.anim.bottom_in);
        ((TextView) sToast.getView().findViewById(R.id.text)).setText(msg);
        sToast.show();
//        sToast.getView().startAnimation(animation);
    }
}
