package indi.dependency.packet.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * 动画工具类
 *
 * @Creater ZhongXiaolong
 * @CreationTime 2017/1/12 11:11.
 */
public class AnimUtils {

    /**
     * 从控件的底部移动到控件所在位置(底部弹出)
     *
     * @return
     */
    public static TranslateAnimation bottomShow(long time) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(time);
        return mHiddenAction;
    }

    /**
     * 从控件所在位置移动到控件的底部(弹回底部隐藏)
     *
     * @return
     */
    public static TranslateAnimation bottomDismiss(long time) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(time);
        return mHiddenAction;
    }

    /**
     * 从上往下显示动画
     */
    public static TranslateAnimation topShow(long time) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(time);
        return mHiddenAction;
    }

    /**
     * 从上往下收回动画
     */
    // TODO: 2017/1/12 待完成
    public static TranslateAnimation topDismiss(long time) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(time);
        return mHiddenAction;
    }

    /**
     * 渐变(模糊-->清晰)
     */
    public static AlphaAnimation alphaShow(long time) {
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(time);// 动画时间
        alpha.setFillAfter(true);// 保持动画状态
        return alpha;
    }

    /**
     * 渐变(清晰-->模糊)
     */
    public static AlphaAnimation alphaDismiss(long time) {
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        alpha.setDuration(time);// 动画时间
        alpha.setFillAfter(true);// 保持动画状态
        return alpha;
    }
}
