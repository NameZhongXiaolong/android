package com.github.application.ui;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.application.R;
import com.github.application.interfaces.SimpleTextWatcher;
import com.github.application.main.Constants;
import com.github.application.utils.UnitUtils;

import static com.github.application.utils.ColorUtils.isDarkColor;

/**
 * Created by ZhongXiaolong on 2019/3/25 14:08.
 * <p>
 * 单位转换
 */
public class UnitCastDialogF extends DialogFragment {

    static final int TAG_PX2DP = 1;
    static final int TAG_PX2SP = 2;
    static final int TAG_DP2PX = 3;
    static final int TAG_SP2PX = 4;

    //内容颜色
    private int mContentColor;

    public static UnitCastDialogF newInstance(int tag) {
        Bundle args = new Bundle();
        args.putInt("tag",tag);
        UnitCastDialogF fragment = new UnitCastDialogF();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.DialogTheme);
        dialog.getWindow().getAttributes().windowAnimations = R.style.TopAnimation;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup container, @Nullable Bundle state) {
        ContextThemeWrapper contextTheme = new ContextThemeWrapper(getContext(), getActivity().getTheme());
        FrameLayout frameLayout = new FrameLayout(contextTheme);
        TypedArray typedArray = contextTheme.obtainStyledAttributes(new int[]{R.attr.colorPrimary});
        int color = typedArray.getColor(0, Color.TRANSPARENT);
        mContentColor = Color.parseColor(isDarkColor(color) ? "#EEFFFFFF" : "#EE333333");
        frameLayout.setBackgroundColor(color);
        frameLayout.addView(LayoutInflater.from(getContext()).inflate(R.layout.dialog_unit_cast, frameLayout, false));
        return frameLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置覆盖状态栏
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        final TextView tvValue = view.findViewById(R.id.text);
        final TextView tvFromUnit = view.findViewById(R.id.text1);
        final TextView tvToUnit = view.findViewById(R.id.text2);
        final EditText etKey = view.findViewById(R.id.edit);
        final ImageView icon = view.findViewById(R.id.image);

        tvValue.setTextColor(mContentColor);
        tvFromUnit.setTextColor(mContentColor);
        tvToUnit.setTextColor(mContentColor);
        etKey.setTextColor(mContentColor);

        etKey.setHintTextColor(Color.argb(135,Color.red(mContentColor),Color.green(mContentColor),Color.blue(mContentColor)));
        icon.setColorFilter(mContentColor);

        final int tag = getArguments().getInt("tag",1);
        if (tag == TAG_PX2DP) {
            tvFromUnit.setText("px");
            tvToUnit.setText("dp");
        }
        if (tag == TAG_PX2SP) {
            tvFromUnit.setText("px");
            tvToUnit.setText("sp");
        }
        if (tag == TAG_SP2PX) {
            tvFromUnit.setText("sp");
            tvToUnit.setText("px");
        }
        if (tag == TAG_DP2PX) {
            tvFromUnit.setText("dp");
            tvToUnit.setText("px");
        }
        etKey.addTextChangedListener(new SimpleTextWatcher(){

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                Integer key ;
                try {
                    key = Integer.valueOf(input);
                } catch (NumberFormatException e) {
                    key = 0;
                }
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                if (tag == TAG_PX2DP) {
                    tvValue.setText(String.valueOf(UnitUtils.dp(key)));
                }
                if (tag == TAG_PX2SP) {
                    tvValue.setText(String.valueOf(UnitUtils.px2sp(key)));
                }
                if (tag == TAG_SP2PX) {
                    int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, key, metrics);
                    tvValue.setText(String.valueOf(value));
                }
                if (tag == TAG_DP2PX) {
                    int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, key, metrics);
                    tvValue.setText(String.valueOf(value));
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setGravity(Gravity.TOP);
        getDialog().getWindow().setLayout(UnitUtils.displayWidth(), Constants.WRAP_CONTENT);

    }
}
