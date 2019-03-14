package priv.xiaolong.app.basics.popup;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import priv.xiaolong.app.R;

/**
 * MaterialDesign包下的底部弹出框
 *
 * @Creator ZhongXiaolong
 * @CreateTime 2017/6/1 10:29.
 */
class ImpMaterialDesignClickListener implements View.OnClickListener {

    @IdRes public final int BOTTOM_SHEET_DIALOG = 0XADE9801;
    @IdRes public final int BOTTOM_SHEET_DIALOG_FRAGMENT = 0XADE9802;

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        if (v.getId() == BOTTOM_SHEET_DIALOG) bottomSheetDialog(context);
        if (v.getId() == BOTTOM_SHEET_DIALOG_FRAGMENT)
            bottomSheetDialogFragment(((AppCompatActivity) context).getSupportFragmentManager());
    }

    /**
     * bottomSheetDialog
     * @param context
     */
    private void bottomSheetDialog(final Context context) {
        final BottomSheetDialog dialog = new BottomSheetDialog(context);

        //设置布局文件
        dialog.setContentView(R.layout.pop_window);

        //设置背景边框颜色,或者在大布局设置颜色
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.parseColor("#00FFF000"));

        //版本判断, 5.0以上版本去除状态栏黑边
        boolean version5 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        if (version5) dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //初始化布局文件,并设置Button点击事件
        ((TextView) dialog.findViewById(R.id.tv_hint)).setText("BottomSheetDialog底部弹出(Material Design)");
        final EditText edInput = dialog.findViewById(R.id.et_input);

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(v ->
                Toast.makeText(context, edInput.getText().toString(), Toast.LENGTH_SHORT).show());

        dialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());

        //显示
        dialog.show();
    }

    /**
     * bottomSheetDialogFragment
     * @param fragmentManager
     */
    private void bottomSheetDialogFragment(FragmentManager fragmentManager) {
        String fmTag = BottomSheetDialogFragmentBm.class.getSimpleName();
        BottomSheetDialogFragmentBm.newInstance("BottomSheetDialogFragment").show(fragmentManager, fmTag);
    }

}
