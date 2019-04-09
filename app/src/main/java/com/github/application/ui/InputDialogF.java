package com.github.application.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.application.R;
import com.github.application.main.Constants;
import com.github.application.main.MainApplication;
import com.github.application.utils.UnitUtils;

/**
 * Created by ZhongXiaolong on 2019/3/23 21:07.
 * <p>
 * Dialog和输入框的交互
 */
public class InputDialogF extends DialogFragment implements View.OnClickListener, Runnable {

    private String TAG = "InputDialogF";
    private ProgressBar mProgressBar;
    private LinearLayout mParent;
    private EditText mEtUser;
    private EditText mEtPwd;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        //去掉标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup root, @Nullable Bundle state) {
        return inf.inflate(R.layout.dialog_input, root, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mParent = view.findViewById(R.id.linear_layout);
        mProgressBar = view.findViewById(R.id.progress);
        mEtUser = view.findViewById(R.id.edit_name);
        mEtPwd = view.findViewById(R.id.edit_pwd);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        final Button btnComfirm = view.findViewById(R.id.btn_confirm);
        btnComfirm.setOnClickListener(this);
        mEtPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnComfirm.performClick();
                    return true;
                }
                return false;
            }
        });

        mEtUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("InputDialogF", "user hasFocus:" + hasFocus);
            }
        });

        mEtPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("InputDialogF", "pwd hasFocus:" + hasFocus);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        int i = UnitUtils.displayHeight(getContext()) / 2;
        getDialog().getWindow().setLayout((int) (UnitUtils.displayWidth(getContext()) * 0.9), Constants.WRAP_CONTENT);
//        getDialog().getWindow().setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
//        attributes.y = i;
//        getDialog().getWindow().setAttributes(attributes);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_cancel) {
            dismiss();
        }
        if (v.getId() == R.id.btn_confirm) {
            String user = mEtUser.getText().toString().trim();
            String pwd = mEtPwd.getText().toString().trim();
            if (TextUtils.isEmpty(user)) {

                MainApplication.errToast("用户名不能为空!");

                return;
            }
            if (TextUtils.isEmpty(pwd)) {
                MainApplication.errToast("密码不能为空!");
                return;
            }

            //关闭软键盘
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            //重新设置窗口位置
//            getDialog().getWindow().setGravity(Gravity.CENTER);
//            WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
//            attributes.y = 0;
//            getDialog().getWindow().setAttributes(attributes);

            //显示加载进度
            mParent.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);

            //登录成功
            v.postDelayed(this, 2000);
        }
    }

    @Override
    public void run() {
        dismiss();
        if (getContext() != null) {
            MainApplication.outToast("登录成功");
        }
    }
}
