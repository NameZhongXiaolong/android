package priv.xiaolong.app.basics.popup;

import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * DialogFragment相关
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/5/31 17:50.
 */
class ImpDialogFragmentClickListener implements View.OnClickListener {

    private final String TAG = "DialogFragment";

    @IdRes public final int DialogFragment_COMM = 0XABCE011;
    @IdRes public final int DialogFragment_NO_TITLE = 0XABCE016;
    @IdRes public final int DIALOGFRAGMENT_FULL_SCREEN = 0XABCE017;
    @IdRes public final int DIALOGFRAGMENT_WIDTH_HEIGHT = 0XABCE013;
    @IdRes public final int DialogFragment_TRANSPARENT = 0XABCE015;
    @IdRes public final int DialogFragment_BOTTOM = 0XABCE014;

    @Override
    public void onClick(View v) {
        FragmentManager fm = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
        if (v.getId() == DialogFragment_COMM) comm(fm);
        if (v.getId() == DialogFragment_NO_TITLE) noTitle(fm);
        if (v.getId() == DIALOGFRAGMENT_FULL_SCREEN) fullScreen(fm);
        if (v.getId() == DIALOGFRAGMENT_WIDTH_HEIGHT) setWidthHeight(fm);
        if (v.getId() == DialogFragment_TRANSPARENT) backgroundTransparent(fm);
        if (v.getId() == DialogFragment_BOTTOM) BottomSheetShow(fm);
    }

    /**
     * 普通
     *
     * @param fragmentManager
     */
    private void comm(FragmentManager fragmentManager) {
        DialogFragmentDm.getInstance().show(fragmentManager, TAG);
    }

    /**
     * 无标题
     *
     * @param fragmentManager
     */
    private void noTitle(FragmentManager fragmentManager) {
        DialogFragmentDm.getInstance().setNoTitle().show(fragmentManager, TAG);
    }

    /**
     * 全屏
     *
     * @param fragmentManager
     */
    private void fullScreen(FragmentManager fragmentManager) {
        DialogFragmentDm.getInstance().setFullScreen().show(fragmentManager, TAG);
    }

    /**
     * 自定义宽高
     *
     * @param fragmentManager
     */
    private void setWidthHeight(FragmentManager fragmentManager) {
        DialogFragmentDm.getInstance().setWidthHeight().show(fragmentManager, TAG);
    }

    /**
     * 设置透明背景
     *
     * @param fragmentManager
     */
    private void backgroundTransparent(FragmentManager fragmentManager) {
        DialogFragmentDm.getInstance().setBackgroundColor(Color.parseColor("#00123456")).show(fragmentManager, TAG);
    }

    /**
     * 底部弹出
     *
     * @param fragmentManager
     */
    private void BottomSheetShow(FragmentManager fragmentManager) {
        DialogFragmentDm.getInstance().setBottomSheet().show(fragmentManager, TAG);
    }

}
