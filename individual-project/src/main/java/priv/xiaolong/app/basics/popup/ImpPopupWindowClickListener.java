package priv.xiaolong.app.basics.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import indi.dependency.packet.util.ViewUtils;
import priv.xiaolong.app.R;

/**
 * PopupWindow
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/6/1 9:41.
 */
class ImpPopupWindowClickListener implements View.OnClickListener {

    @IdRes public final int POPUPWINDOW_COMM = 0XFFAB001;
    @IdRes public final int POPUPWINDOW_BOTTOM_SHEET = 0XFFAB002;

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        if (v.getId() == POPUPWINDOW_COMM) comm(context);
        if (v.getId() == POPUPWINDOW_BOTTOM_SHEET) bottomSheet(context);
    }

    private void comm(Context context) {
        PopupWindow pop = new PopupWindow(new ProgressBar(context), ViewUtils.WRAP_CONTENT, ViewUtils.WRAP_CONTENT);
        pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00123456")));
        //设置返回键可以点击
        pop.setFocusable(true);
        //设置外部可以点击
        pop.setOutsideTouchable(true);
        pop.showAtLocation(pop.getContentView(), Gravity.CENTER, 0, 0);
    }

    private void bottomSheet(final Context context) {
        /*
         * PopupWindow设置弹出框外的阴影背景只能手动设置屏幕变暗
         * WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
         * lp.alpha = 0.4f;//显示时为1,不显示时为0
         * activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
         * activity.getWindow().setAttributes(lp);
         */

        //设置布局文件和宽高
        final View contentView = LayoutInflater.from(context).inflate(R.layout.pop_window, null);
        final PopupWindow pop = new PopupWindow(contentView, ViewUtils.MATCH_PARENT, ViewUtils.WRAP_CONTENT);

        //初始化布局文件,并设置Button点击事件
        ((TextView) contentView.findViewById(R.id.tv_hint)).setText("PopupWindow底部弹出");
        final EditText edInput = (EditText) contentView.findViewById(R.id.et_input);
        contentView.findViewById(R.id.btn_confirm).setOnClickListener(v ->
                Toast.makeText(context, edInput.getText().toString().trim(), Toast.LENGTH_SHORT).show());

        contentView.findViewById(R.id.btn_cancel).setOnClickListener(v -> pop.dismiss());

        //设置显示动画
        pop.setAnimationStyle(R.style.PopAnimation);

        //设置背景颜色(不设置就不能使屏幕消失),边框颜色,或者在布局文件设置颜色
        pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#60FF4081")));

        //设置返回键可以点击
        pop.setFocusable(true);

        //设置外部可以点击
        pop.setOutsideTouchable(true);

        //显示
        pop.showAtLocation(contentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
