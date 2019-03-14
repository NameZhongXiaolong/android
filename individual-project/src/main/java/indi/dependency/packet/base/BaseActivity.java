package indi.dependency.packet.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * @Creator ZhongXiaolong
 * @CreateTime 2017/4/28 10:02.
 */
public class BaseActivity extends AppCompatActivity{

    protected void setWindowStatusBarColor(int color,View...views){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
        for (int i = 0; i < views.length; i++) {
            View view = views[i];
            if (view!=null) view.setBackgroundColor(color);
        }
    }

    protected void setWindowThemeColor(@ColorInt int color){
        setWindowStatusBarColor(color);
        getWindow().setBackgroundDrawable(new ColorDrawable(color));
    }

    protected FragmentTransaction getFmTransaction() {
       return getSupportFragmentManager().beginTransaction();
    }

    protected void setFullScreen(boolean fullScreen){
        if (fullScreen && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
        ActivityCompat.startActivity(this,
                intent,
                optionsCompat.toBundle());
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hallo");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    protected void setSupportActionBar(@IdRes int toolbarId) {
        try {
            Toolbar toolbar = (Toolbar) findViewById(toolbarId);
            setSupportActionBar(toolbar);
        }catch (ClassCastException e){
            return;
        }
    }

    protected AppCompatActivity getActivity(){
        return this;
    }

    protected Context getContext(){
        return this;
    }


    protected FragmentTransaction getFragmentTransaction(){
        return getSupportFragmentManager().beginTransaction();
    }

    /**
     * 获取状态栏高度
     */
    protected int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

}
