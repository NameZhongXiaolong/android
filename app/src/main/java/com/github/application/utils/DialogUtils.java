package com.github.application.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.application.R;

/**
 * Created by ZhongXiaolong on 2019/3/11 10:22.
 */
public class DialogUtils {

    public interface OnDialogCallBack {
        void onButtonClick(Dialog dialog, int position, String menu);
    }

    public static Dialog bottomSheetMenu(Context context, OnDialogCallBack l, String... menus) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(R.layout.dialog_bottom_menu);
        //设置大背景颜色
        View container = dialog.findViewById(R.id.design_bottom_sheet);
        if (container != null) container.setBackgroundColor(Color.TRANSPARENT);

        LinearLayout menuContainer = dialog.findViewById(R.id.linear_layout);

        if (menuContainer == null) {
            return dialog;
        }

        int buttonHeight = UnitUtils.px(45);
        int textColor = Color.parseColor("#333333");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, buttonHeight);
        lp.setMargins(0, 1, 0, 0);
        for (String menu : menus) {
            Button button = new Button(context);
            button.setGravity(Gravity.CENTER);
            button.setBackgroundColor(Color.WHITE);
            button.setTextColor(textColor);
            button.setText(menu);
            final int position = menuContainer.getChildCount();
            button.setOnClickListener(v -> l.onButtonClick(dialog, position, menu));
            menuContainer.addView(button, lp);
        }
        View button = dialog.findViewById(R.id.button);
        if (button != null) button.setOnClickListener(v -> dialog.dismiss());
        return dialog;
    }
}
