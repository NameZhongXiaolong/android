package com.github.application.view.listener;

/**
 * Created by ZhongXiaolong on 2018/12/3 17:57.
 */
public interface OnLoadButtonClickListener {

    /**
     * 动画开始
     */
    void onAnimationStart();

    /**
     * 动画完成
     */
    void onAnimationFinish();

    /**
     * 动画取消
     */
    void onAnimationCancel();
}
