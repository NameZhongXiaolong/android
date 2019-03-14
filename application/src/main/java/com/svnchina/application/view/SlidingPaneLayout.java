package com.svnchina.application.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ZhongXiaolong on 2018/1/9 17:34.
 *
 * 对SlidingPaneLayout优化: 1.在边缘才可以滑动; 2.添加不可滑动方法
 */
public class SlidingPaneLayout extends android.support.v4.widget.SlidingPaneLayout {

    private boolean mCanSlide;

    public SlidingPaneLayout(Context context) {
        super(context);
        mCanSlide = true;
    }

    public SlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        mCanSlide = true;
    }

    public SlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCanSlide = true;
    }

    /**
     * 设置是否可以滑动 , 默认为true
     *
     * @param canSlide
     *
     * @return
     */
    public SlidingPaneLayout setCanSlide(boolean canSlide) {
        mCanSlide = canSlide;
        return this;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (ev.getRawX() > 30 || !mCanSlide) {
                return false;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (ev.getRawX() > 30 || !mCanSlide) {
                return false;
            }
        }
        return super.onTouchEvent(ev);
    }

}

