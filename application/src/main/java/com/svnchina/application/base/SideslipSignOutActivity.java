package com.svnchina.application.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import com.svnchina.application.view.SlidingPaneLayout;

import java.lang.reflect.Field;

/**
 * Created by ZhongXiaolong on 2018/6/4 14:47.
 *
 * 侧滑退出的Activity
 */
public class SideslipSignOutActivity extends BaseSuperActivity {

    private SlidingPaneLayout mSlidingPaneLayout;

    /**
     * 设置侧滑退出,默认为true
     *
     * @param b
     */
    public void setSlideBackFinish(boolean b) {
        mSlidingPaneLayout.setCanSlide(b);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSlidingPaneLayout = new SlidingPaneLayout(this);

        // 通过反射改变mOverhangSize的值为0，
        // 这个mOverhangSize值为菜单到右边屏幕的最短距离，
        // 默认是32dp，现在给它改成0
        try {
            Field overhangSize = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
            overhangSize.setAccessible(true);
            overhangSize.set(mSlidingPaneLayout, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSlidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.SimplePanelSlideListener() {
            @Override
            public void onPanelOpened(View panel) {
                finish();
            }
        });

        mSlidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));

        // 左侧的透明视图
        View leftView = new View(this);
        leftView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT));
        mSlidingPaneLayout.addView(leftView, 0);

        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();


        // 右侧的内容视图
        ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);
        decorChild.setBackgroundColor(getResources().getColor(android.R.color.white));
        decorView.removeView(decorChild);
        decorView.addView(mSlidingPaneLayout);

        // 为 SlidingPaneLayout 添加内容视图
        mSlidingPaneLayout.addView(decorChild, 1);
    }

}
