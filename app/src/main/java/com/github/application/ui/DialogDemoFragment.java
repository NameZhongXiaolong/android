package com.github.application.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.application.R;

/**
 * Created by ZhongXiaolong on 2019/3/22 20:40.
 * <p>
 * 弹出框
 */
public class DialogDemoFragment extends SimpleListFragment {

    private AlertDialog mAlertDialog;
    private Dialog mCustomDialog;
    private Dialog mCustomDialogRadius;
    private ProgressDialog mProgressDialog;
    private ProgressDialog mProgressMsgDialog;
    private ProgressDialog mProgressDialogRadius;
    private ProgressDialog mProgressMsgDialogRadius;
    private DialogFragmentDemo mDialogFragmentDemo;
    private BottomSheetDialog mBottomSheetDialog;
    private BottomSheetDialogFragment mBottomSheetDialogFragment;


    /**
     * dialog
     * 点击外部是否可以dismiss,默认是true(点击返回键可以dismiss)
     * dialog.setCanceledOnTouchOutside(false);
     * 点击外部和返回键都不可以dismiss
     * dialog.setCancelable(false);
     */
    @Override
    void onCreateList() {
        add("AlertDialog");
        add("Dialog自定义View");
        add("Dialog自定义View(圆角背景)");
        add("ProgressDialog");
        add("ProgressDialog & msg");
        add("ProgressDialog(圆角背景)");
        add("ProgressDialog & msg(圆角背景)");
        add("DialogFragment");
        add("BottomSheetDialog");
        add("BottomSheetDialogFragment");
        add("PopupWindow");

        mAlertDialog = createAlertDialog();
        mCustomDialog = createCustomDialog(false);
        mCustomDialogRadius = createCustomDialog(true);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressMsgDialog = new ProgressDialog(getContext()).setTipsMsg("请等待", true);
        mProgressDialogRadius = new ProgressDialog(getContext(), Color.WHITE, 10);
        mProgressMsgDialogRadius = new ProgressDialog(getContext(), Color.WHITE, 10).setTipsMsg("请等待", true);
        mDialogFragmentDemo = new DialogFragmentDemo();
        mBottomSheetDialog = createBottomSheetDialog();
        mBottomSheetDialogFragment = new BottomSheetDialogDemo();
    }

    @Override
    void onClick(View item, int position) {
        if (get(position).equals("AlertDialog")) {
            mAlertDialog.show();
        }
        if (get(position).equals("Dialog自定义View")) {
            mCustomDialog.show();
        }
        if (get(position).equals("Dialog自定义View(圆角背景)")) {
            mCustomDialogRadius.show();
        }
        if (get(position).equals("ProgressDialog")) {
            mProgressDialog.show();
        }
        if (get(position).equals("ProgressDialog & msg")) {
            mProgressMsgDialog.show();
        }
        if (get(position).equals("ProgressDialog(圆角背景)")) {
            mProgressDialogRadius.show();
        }
        if (get(position).equals("ProgressDialog & msg(圆角背景)")) {
            mProgressMsgDialogRadius.show();
        }
        if (get(position).equals("DialogFragment")) {
            mDialogFragmentDemo.show(getChildFragmentManager(), "");
        }
        if (get(position).equals("BottomSheetDialog")) {
            mBottomSheetDialog.show();
        }
        if (get(position).equals("BottomSheetDialogFragment")) {
            mBottomSheetDialogFragment.show(getChildFragmentManager(), "");
        }
        if (get(position).equals("PopupWindow")) {

        }
    }

    private AlertDialog createAlertDialog() {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String msg = "";
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    msg = "negative";
                }
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    msg = "positive";
                }
                if (which == DialogInterface.BUTTON_NEUTRAL) {
                    msg = "neutral";
                }
                Snackbar.make(getRecyclerView(), msg, Snackbar.LENGTH_LONG).show();
            }
        };


        return new AlertDialog.Builder(getContext())
                .setTitle("AlertDialog")
                .setMessage("this is AlertDialog!")
                .setPositiveButton("Positive", onClickListener)
                .setNegativeButton("Negative", onClickListener)
                .setNeutralButton("Neutral", onClickListener)
                .create();
    }

    private BottomSheetDialog createBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.dialog_bottom_demo);
        //设置大背景颜色
        View container = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        if (container != null) {
            //避免找不到
            container.setBackgroundColor(Color.TRANSPARENT);
        }
        LinearLayout layout = bottomSheetDialog.findViewById(R.id.linear_layout);
        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof TextView) {
                    bottomSheetDialog.dismiss();
                    String text = ((TextView) v).getText().toString().trim();
                    Snackbar.make(getRecyclerView(), text, Snackbar.LENGTH_SHORT).show();
                }
            }
        };
        for (int i = 0; i < layout.getChildCount(); i++) {
            layout.getChildAt(i).setOnClickListener(onClickListener);
        }
        return bottomSheetDialog;
    }

    /**
     * 自定义View的Dialog
     *
     * @param radius 是否设置圆角
     */
    private Dialog createCustomDialog(boolean radius) {
        //要设置圆角得设置主题
        final Dialog dialog = radius ? new Dialog(getContext(), R.style.DialogTheme) : new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_custom);
        LinearLayout layout = dialog.findViewById(R.id.linear_layout);
        for (int i = 0; i < layout.getChildCount(); i++) {
            final int index = i;
            layout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index == 1) dialog.dismiss();
                }
            });
        }

        return dialog;
    }

    public static class DialogFragmentDemo extends DialogFragment implements View.OnClickListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //返回一个Dialog样式,也可以是自定义的
            Dialog dialog = super.onCreateDialog(savedInstanceState);
//            dialog = new Dialog(getContext(), R.style.DialogTheme);
            return dialog;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup root, @Nullable Bundle state) {
            return inf.inflate(R.layout.dialog_custom, root, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            LinearLayout layout = view.findViewById(R.id.linear_layout);
            for (int i = 0; i < layout.getChildCount(); i++) {
                View childView = layout.getChildAt(i);
                if (i == 1) childView.setTag("dismiss");
                childView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getTag() != null) {
                dismiss();
            } else {
                Toast.makeText(getContext(), "否", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            //设置窗口
            Dialog dialog = getDialog();
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public static class BottomSheetDialogDemo extends BottomSheetDialogFragment implements View.OnClickListener {

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup root, @Nullable Bundle state) {
            //设置大背景颜色
            View container = getDialog().findViewById(R.id.design_bottom_sheet);
            if (container != null) {
                //避免找不到
                container.setBackgroundColor(Color.TRANSPARENT);
            }
            return inf.inflate(R.layout.dialog_bottom_demo, root, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //设置弹出框颜色
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LinearLayout layout = view.findViewById(R.id.linear_layout);
            for (int i = 0; i < layout.getChildCount(); i++) {
                layout.getChildAt(i).setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (v instanceof TextView) {
                String text = ((TextView) v).getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    dismiss();
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
