package com.github.application.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.application.R;
import com.github.application.main.Constants;
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
    }


    @Override
    public void onStart() {
        super.onStart();
        int i = UnitUtils.displayHeight(getContext()) / 2;
        getDialog().getWindow().setLayout((int) (UnitUtils.displayWidth(getContext()) * 0.9), Constants.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.y = i;
        getDialog().getWindow().setAttributes(attributes);
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
                Toast.makeText(getContext(), "用户名不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(getContext(), "密码不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }

            mParent.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);

            v.postDelayed(this, 2000);
        }
    }

    @Override
    public void run() {
        dismiss();
        if (getContext() != null) {
            Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
        }
    }
}
