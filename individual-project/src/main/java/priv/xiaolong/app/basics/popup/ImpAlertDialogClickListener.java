package priv.xiaolong.app.basics.popup;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import priv.xiaolong.app.R;

/**
 * AlertDialog
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/5/31 17:37.
 */
class ImpAlertDialogClickListener implements View.OnClickListener, DialogInterface.OnClickListener {

    @IdRes public final int ALERTDIALOG_SHOW = 0XABCE001;
    @IdRes public final int ALERTDIALOG_CREATE = 0XABCE002;
    @IdRes public final int ALERTDIALOG_CUSTOM = 0XABCE003;
    @IdRes public final int ALERTDIALOG_BOTTOM = 0XABCE004;
    @IdRes public final int ALERTDIALOG_TRANSPARENT = 0XABCE005;

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Context context = ((AlertDialog) dialog).getContext();
        if (which == dialog.BUTTON_POSITIVE) Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
        if (which == dialog.BUTTON_NEGATIVE) Toast.makeText(context, "确定", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        if (v.getId() == ALERTDIALOG_SHOW) show(context);
        if (v.getId() == ALERTDIALOG_CREATE) create(context);
        if (v.getId() == ALERTDIALOG_CUSTOM) customView(context);
        if (v.getId() == ALERTDIALOG_TRANSPARENT) backgroundTransparent(context);
        if (v.getId() == ALERTDIALOG_BOTTOM) BottomSheetShow(context);
    }

    /**
     * show方法--最简单写法
     *
     * @param context
     */
    private void show(Context context) {
        new AlertDialog.Builder(context).setTitle("标题")
                .setMessage("主要内容").setNegativeButton("确定", this).setPositiveButton("取消", this).show();
    }

    /**
     * create再show
     *
     * @param context
     */
    private void create(Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle("标题")
                .setMessage("主要内容").setNegativeButton("确定", this).setPositiveButton("取消", this).create();
        alertDialog.show();
    }

    /**
     * 自定义布局
     *
     * @param context
     */
    private void customView(Context context) {
        new AlertDialog.Builder(context).setView(new ProgressBar(context)).show();
    }

    /**
     * 自定义布局+透明背景
     *
     * @param context
     */
    private void backgroundTransparent(Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setView(new ProgressBar(context)).create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);//不添加, 低版本会显示标题
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        //点击外部是否可以dismiss,默认为true
        //alertDialog.setCanceledOnTouchOutside(false);

        //点击返回键是否可以dismiss,默认为true
        //alertDialog.setCancelable(false);

        alertDialog.show();
    }

    /**
     * 底部弹出
     *
     * @param context
     */
    private void BottomSheetShow(Context context) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.pop_window, null);
        AlertDialog alertDialog = new AlertDialog.Builder(context).setView(dialogView).create();
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.windowAnimations = R.style.PopAnimation;
        alertDialog.getWindow().setAttributes(attributes);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.show();
    }
}
